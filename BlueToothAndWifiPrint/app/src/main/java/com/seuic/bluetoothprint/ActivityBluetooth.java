package com.seuic.bluetoothprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.seuic.BaseActivity;
import com.seuic.bluetoothandwifiprint.R;
import com.seuic.bluetoothmain.DialogUtils;
import com.seuic.bluetoothmain.ProgressDlgUtil;
import com.seuic.bluetoothmain.SNBCApplication;
import com.seuic.bluetoothmain.receiver.ReceiverBluetooth;
import com.seuic.bluetoothprint.utils.CommonUtils;
import com.seuic.bluetoothprint.utils.LogPrints;
import com.seuic.bluetoothprint.utils.PrinterAdapter;
import com.seuic.bluetoothprint.utils.PrinterBean;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.connect.connectImpl.BluetoothConnect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.seuic.bluetoothmain.BluetoothStyle.updateDiskSymbol;
import static com.seuic.bluetoothmain.BluetoothStyle.updateOSFontFileArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateOSFormatFileArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateOSImageFileArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateOSImageFileForPrintArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateStoredBuiltinFontArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateStoredCustomFontArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateStoredFormatArray;
import static com.seuic.bluetoothmain.BluetoothStyle.updateStoredImageArray;

/**
 * Created by Dell on 2017/4/6.
 */

public class ActivityBluetooth extends BaseActivity implements ReceiverBluetooth.IList, AdapterView.OnItemClickListener {
    @BindView(R.id.ll_bluetooth_info) LinearLayout ll_bluetooth_info; // BPLZ那些、连接按钮
    @BindView(R.id.lv_bluetooth_search) ListView lv_bluetooth_search;
    @BindView(R.id.sp_bluetooth_instrution_set) Spinner sp_bluetooth_instrution_set;
    @BindView(R.id.btn_bluetooth_connect)Button btn_bluetooth_connect;
    @BindView(R.id.btn_bluetooth_print)Button btn_bluetooth_print;
    @BindView(R.id.bt_bluetooth_search)Button bt_bluetooth_search;

    private ArrayAdapter<String> instrutionAdapter;
    private List<String> instrutionList = new ArrayList<String>();
    private ArrayList<PrinterBean> listBean = new ArrayList<>();
    private PrinterAdapter printerAdapter;
    private ReceiverBluetooth receiverBluetooth;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice btDev;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);

        receiverBluetooth = new ReceiverBluetooth(this, listBean, this);
        initReceiver();
        initBlueTooth();

        printerAdapter = new PrinterAdapter(this, listBean);
        lv_bluetooth_search.setAdapter(printerAdapter);

        lv_bluetooth_search.setOnItemClickListener(this);

        for (int i = 0; i < InstructionType.values().length; i++) { // sdk里面封装好
            instrutionList.add(InstructionType.values()[i].toString());
        }
        instrutionAdapter = new ArrayAdapter<String>(ActivityBluetooth.this, android.R.layout.simple_spinner_item,
                instrutionList);


        //新的API
        instrutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 连接已配对的蓝牙
        btn_bluetooth_connect.setOnClickListener(new BluetoothConnectListener());

        btn_bluetooth_print.setOnClickListener(new BarcodePrinterListener(ActivityBluetooth.this));
        ProgressDlgUtil.showProgress("搜索中...",this);
        ProgressDlgUtil.setBack();
    }

    /**
     * 蓝牙广播注册
     */
    private void initReceiver() {
        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiverBluetooth, mFilter);
    }

    /**
     * 蓝牙初始化
     */
    void initBlueTooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) { // 判断蓝牙是否关闭
            bluetoothAdapter.enable(); // 打开蓝牙开关
            bluetoothAdapter.startDiscovery();// 开始搜索蓝牙
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void getList(ArrayList<PrinterBean> list) {
        listBean = list;
        printerAdapter.notifyDataSetChanged();
    }


    @Override
    public void bondedSuccessful() {
        sp_bluetooth_instrution_set.setAdapter(instrutionAdapter);
        ll_bluetooth_info.setVisibility(View.VISIBLE);
        lv_bluetooth_search.setVisibility(View.GONE);
        bt_bluetooth_search.setVisibility(View.GONE);
    }

    /**
     * 点击item进行蓝牙连接
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        btDev = bluetoothAdapter.getRemoteDevice(listBean.get(position).getMac());

        if (bluetoothAdapter.isDiscovering()) // 如果还在搜索蓝牙设备，那么先取消搜索
            bluetoothAdapter.cancelDiscovery();

        LogPrints.e("状态："+btDev.getBondState());
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) { // 10 由系统蓝牙和设备绑定
            try {
                Method createBond = BluetoothDevice.class.getMethod("createBond");
                createBond.invoke(btDev);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogPrints.e("执行了");
        }
        if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) { // 12
            boolean b = removeBond(BluetoothDevice.class, btDev);
            LogPrints.d("取消连接："+b);
            // 下面没执行
            try {
                Method createBond = BluetoothDevice.class.getMethod("createBond");
                createBond.invoke(btDev);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            ConnectThread connectThread = new ConnectThread(btDev, bluetoothAdapter);
//            BluetoothSocket clientSocket = connectThread.getClientSocket();
//            connectThread.start();
        }
    }

    @OnClick(R.id.bt_bluetooth_search)
    void doClick(View view) {
       /* if (!bluetoothAdapter.isEnabled()) { // 判断蓝牙是否关闭
            bluetoothAdapter.enable(); // 打开蓝牙开关
            bluetoothAdapter.startDiscovery();// 开始搜索蓝牙
        } else {
            bluetoothAdapter.startDiscovery();
        }*/
       ProgressDlgUtil.showProgress("搜索中...",this);
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 在软件中连接已经被系统连接过的蓝牙设备
     */
    private class BluetoothConnectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (btn_bluetooth_connect.getText().toString().equals("连接"))
                connectBondedBT();
            else  if (btn_bluetooth_connect.getText().toString().equals("取消连接"))
                cancleConnect();
        }
    }

    /**
     * 连接已经绑定好的蓝牙设备
     */
    private void connectBondedBT() {
        try {
            // BluetoothConnect 在 SNBCSDKForBarcodePrinter.jar中
            BluetoothConnect bluetoothConnect = new BluetoothConnect(BluetoothAdapter.getDefaultAdapter(), btDev.getAddress());
            SNBCApplication application = (SNBCApplication) ActivityBluetooth.this.getApplication();
            bluetoothConnect.DecodeType(application.getDecodeType());
            bluetoothConnect.connect();
            BarPrinter.BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
            builder.buildDeviceConnenct(bluetoothConnect);
            String mType = sp_bluetooth_instrution_set.getItemAtPosition(sp_bluetooth_instrution_set.getSelectedItemPosition()).toString();
            builder.buildInstruction(InstructionType.valueOf(mType));
            BarPrinter printer = builder.getBarPrinter();
            // 打印机实例初始化
            application.setPrinter(printer);
            application.setConnect(bluetoothConnect);

            updateStoredBuiltinFontArray(application, printer, ActivityBluetooth.this);
            updateOSFontFileArray(application, printer, ActivityBluetooth.this);
            updateOSFormatFileArray(application, printer, ActivityBluetooth.this);
            updateDiskSymbol(application, printer, ActivityBluetooth.this);
            updateOSImageFileForPrintArray(application, printer, ActivityBluetooth.this);
            updateStoredCustomFontArray(application, printer, ActivityBluetooth.this);
            updateStoredImageArray(application, printer, ActivityBluetooth.this);
            updateStoredFormatArray(application, printer, ActivityBluetooth.this);
            updateOSImageFileArray(application, printer, ActivityBluetooth.this);

            // InstructionType 在 SNBCSDKForBarcodePrinter.jar中
            if (InstructionType.valueOf(mType) != InstructionType.BPLA) {
                DialogUtils.showOneButton(ActivityBluetooth.this, "Connect to print successful!\r\n The printer's name is " + printer.labelQuery().getPrinterName());
            } else {
                DialogUtils.showOneButton(ActivityBluetooth.this, "  Connect to print successful");
            }
            btn_bluetooth_connect.setText("取消连接");
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showOneButton(ActivityBluetooth.this, "" + e);
        }
    }

    private void cancleConnect() {
        BluetoothConnect bluetoothConnect = (BluetoothConnect) ((SNBCApplication) getApplication()).getConnect();
        if (null != bluetoothConnect) {
            try {
                bluetoothConnect.disconnect();
                applicationClean(ActivityBluetooth.this);
                btn_bluetooth_connect.setText("连接");
            } catch (IOException e) {
                e.printStackTrace();
                CommonUtils.toastShowShort(ActivityBluetooth.this, "" + e);
            }
        }
    }





    // 与设备解除配对
    public boolean removeBond(Class btClass, BluetoothDevice btDevice)  {
        Boolean returnValue = null;
        try {
            Method removeBondMethod = btClass.getMethod("removeBond");
            returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue.booleanValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverBluetooth);
        try {
            if (btDev != null){
                boolean b = removeBond(BluetoothDevice.class, btDev);
                LogPrints.e("取消配对："+b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        LogPrints.i("keycode:"+keyCode);
//        return true;
//    }
}
