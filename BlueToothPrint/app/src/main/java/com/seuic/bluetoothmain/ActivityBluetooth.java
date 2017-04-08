package com.seuic.bluetoothmain;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.seuic.bluetoothmain.receiver.ReceiverBluetooth;
import com.seuic.bluetoothmain.service.ConnectThread;
import com.seuic.bluetoothprint.CommonUtils;
import com.seuic.bluetoothprint.LogPrints;
import com.seuic.bluetoothprint.PrinterAdapter;
import com.seuic.bluetoothprint.PrinterBean;
import com.seuic.bluetoothprint.R;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.connect.connectImpl.BluetoothConnect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class ActivityBluetooth extends Activity implements ReceiverBluetooth.IList, AdapterView.OnItemClickListener {
    @BindView(R.id.ll_bluetooth_info)
    LinearLayout ll_bluetooth_info; // BPLZ那些、连接按钮
    @BindView(R.id.lv_bluetooth_result)
    ListView lv_bluetooth_result;
    @BindView(R.id.lv_bluetooth_search)
    ListView lv_bluetooth_search;
    @BindView(R.id.sp_bluetooth_instrution_set)
    Spinner sp_bluetooth_instrution_set;
    @BindView(R.id.btn_bluetooth_connect)
    Button btn_bluetooth_connect;
    @BindView(R.id.btn_bluetooth_discover)
    Button btn_bluetooth_discover;

    private static final String DEVICE_NAME = "DEVICE_NAME";
    private static final String DEVICE_IP = "DEVICE_IP";
    private String bluetooth_connect_ip;
    private String bluetooth_connect_name;
    private ArrayAdapter<String> instrutionAdapter;
    private List<String> instrutionList = new ArrayList<String>();
    private ArrayList<PrinterBean> listBean = new ArrayList<>();
    private PrinterAdapter printerAdapter;
    private ReceiverBluetooth receiverBluetooth;
    private BluetoothAdapter bluetoothAdapter;

    public Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int id = msg.what;
            switch (id) {
                //BLUETOOTH item click
                case 1:
                    sp_bluetooth_instrution_set.setAdapter(instrutionAdapter);
                    ll_bluetooth_info.setVisibility(View.VISIBLE);
                    break;

                case ConnectThread.CONNECTSUCCESS: //连接成功
//                    connectBondedBT();
                    sp_bluetooth_instrution_set.setAdapter(instrutionAdapter);
                    ll_bluetooth_info.setVisibility(View.VISIBLE);
                default:
                    break;
            }
        }
    };

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
        // 发现已系统已配对的蓝牙
        btn_bluetooth_discover.setOnClickListener(new BluetoothDiscoverListener());
        // 连接已配对的蓝牙
        btn_bluetooth_connect.setOnClickListener(new BluetoothConnectListener());
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

    /**
     * activity 作为ReceiverBluetooth.IList子类执行此方法；
     *
     * @param list
     */
    @Override
    public void getList(ArrayList<PrinterBean> list) {
        listBean = list;
        printerAdapter.notifyDataSetChanged();
    }

    @Override
    public void bondedSuccessful() {
        sp_bluetooth_instrution_set.setAdapter(instrutionAdapter);
        ll_bluetooth_info.setVisibility(View.VISIBLE);
    }

    private BluetoothDevice btDev;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        btDev = bluetoothAdapter.getRemoteDevice(listBean.get(position).getMac());
        if (bluetoothAdapter.isDiscovering()) // 如果还在搜索蓝牙设备，那么先取消搜索
            bluetoothAdapter.cancelDiscovery();
        LogPrints.e("状态："+btDev.getBondState());
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) { // 10
            try {
                Method createBond = BluetoothDevice.class.getMethod("createBond");
                createBond.invoke(btDev);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogPrints.e("执行了");
        }
        if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) { // 12
            ConnectThread connectThread = new ConnectThread(btDev, bluetoothAdapter, myHandler);
            BluetoothSocket clientSocket = connectThread.getClientSocket();
            connectThread.start();
        }
    }

    /**
     * 发现蓝牙(已配对的系统蓝牙)
     */
    private class BluetoothDiscoverListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            BluetoothAdapter blueToothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!blueToothAdapter.isEnabled()) {
                blueToothAdapter.enable();
            }
            if (ll_bluetooth_info.getVisibility() == View.VISIBLE) {
                ll_bluetooth_info.setVisibility(View.GONE);
            }
            // 获取已经绑定好的蓝牙设备
            Set<BluetoothDevice> blueToothDevices = blueToothAdapter.getBondedDevices();
            List<Map<String, String>> blueTooth_devices_info = new ArrayList<Map<String, String>>();
            if (blueToothDevices.size() > 0) {
                Map<String, String> blueTooth_item = null;
                for (BluetoothDevice device : blueToothDevices) {
                    blueTooth_item = new HashMap<String, String>();
                    blueTooth_item.put(DEVICE_NAME, device.getName());
                    blueTooth_item.put(DEVICE_IP, device.getAddress());
                    blueTooth_devices_info.add(blueTooth_item);
                }
            } else {
                CommonUtils.toastShowShort(ActivityBluetooth.this, "请先连接蓝牙");
                return;
            }

            SimpleAdapter blueToothDataAdapter = new SimpleAdapter(ActivityBluetooth.this, blueTooth_devices_info,
                    R.layout.item_bluetooth_devices, new String[]{DEVICE_NAME, DEVICE_IP},
                    new int[]{R.id.bluetooth_device_name, R.id.bluetooth_device_ip});
            lv_bluetooth_result.setAdapter(blueToothDataAdapter);
            // 新的API
            lv_bluetooth_result.setSelector(android.R.color.background_light);
            lv_bluetooth_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Map<String, String> map = (Map) parent.getItemAtPosition(position);
                    bluetooth_connect_ip = map.get(DEVICE_IP);
                    bluetooth_connect_name = map.get(DEVICE_NAME);
                    myHandler.sendEmptyMessage(1);
                }
            });
        }
    }

    /**
     * 在软件中连接已经被系统连接过的蓝牙设备
     */
    private class BluetoothConnectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            connectBondedBT();
        }
    }

    private void connectBondedBT() {
//        if (btn_bluetooth_connect.getText().toString().equals("连接")) {
            try {
                // BluetoothConnect 在 SNBCSDKForBarcodePrinter.jar中
//                BluetoothConnect bluetoothConnect = new BluetoothConnect(BluetoothAdapter.getDefaultAdapter(), bluetooth_connect_ip);
                BluetoothConnect bluetoothConnect = new BluetoothConnect(BluetoothAdapter.getDefaultAdapter(), btDev.getAddress());
                SNBCApplication application = (SNBCApplication) ActivityBluetooth.this.getApplication();
                bluetoothConnect.DecodeType(application.getDecodeType());
                bluetoothConnect.connect();
                BarPrinter.BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
                builder.buildDeviceConnenct(bluetoothConnect);
                String mType = sp_bluetooth_instrution_set.getItemAtPosition(sp_bluetooth_instrution_set.getSelectedItemPosition()).toString();
                builder.buildInstruction(InstructionType.valueOf(mType));
                final BarPrinter printer = builder.getBarPrinter();
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
//                    btn_wifi_connect.setText(getResources().getString(R.string.disconnect));
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

    private void applicationClean() {
        SNBCApplication application = (SNBCApplication) ActivityBluetooth.this.getApplication();
        application.setConnect(null);
        application.setFlashDiskSymbol(null);
        application.setOsFontFileArray(null);
        application.setOsFormatFileArray(null);
        application.setOsImageFileArray(null);
        application.setOsImageFileForPrintArray(null);
        application.setPrinter(null);
        application.setRamDiskSymbol(null);
        application.setStoredBuildinFontArray(null);
        application.setStoredCustomFontArray(null);
        application.setStoredFormatArray(null);
        application.setStoredImageArray(null);
    }

    // 与设备解除配对
    public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverBluetooth);
        try {
            boolean b = removeBond(BluetoothDevice.class, btDev);
            LogPrints.e("取消配对："+b);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (btn_bluetooth_connect.getText().toString().equals("取消连接")) {
//            BluetoothConnect bluetoothConnect = (BluetoothConnect) ((SNBCApplication) getApplication()).getConnect();
//            if (null != bluetoothConnect) {
//                try {
//                    bluetoothConnect.disconnect();
//                    applicationClean();
//                    btn_bluetooth_connect.setText("连接");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    CommonUtils.toastShowShort(ActivityBluetooth.this, "" + e);
//                }
//            }
//        }
    }
}
