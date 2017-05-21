package com.seuic.bluetoothmain.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.seuic.bluetoothmain.ProgressDlgUtil;
import com.seuic.bluetoothprint.utils.CommonUtils;
import com.seuic.bluetoothprint.utils.LogPrints;
import com.seuic.bluetoothprint.utils.PrinterBean;

import java.util.ArrayList;

/**
 * Created by Dell on 2017/4/8.
 */

public class ReceiverBluetooth extends BroadcastReceiver {

    private Context context;
    private ArrayList<PrinterBean> list;
    private IList iList;

    public ReceiverBluetooth(Context context,ArrayList<PrinterBean> list,IList iList) {
        this.context = context;
        this.list = list;
        this.iList = iList;
    }

    boolean isFirst;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        LogPrints.v("action:"+action);

//        if (!isFirst)
//            ProgressDlgUtil.showProgress("搜索中...",context);
        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            LogPrints.v("搜索完毕");
            ProgressDlgUtil.dismissProgress();
        }
        // 获得已经搜索到的蓝牙设备
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            isFirst = true;
            // 找到了蓝牙设备
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(TextUtils.isEmpty(device.getName())){
                LogPrints.d("打印机的name为:"+device.getName()); // null 的名字蓝牙
                return;
            }
            PrinterBean bean = new PrinterBean();
            bean.setMac(device.getAddress());
            bean.setName(device.getName());
            list.add(bean);

            iList.getList(list);

            LogPrints.d("data", "周边蓝牙设备=" + bean.getName());
        }else if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            Log.i("PAIRING_REQUEST", "PAIRING_REQUEST");
            BluetoothDevice btDevice = intent .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
//                    BluetoothHelper.setPin(btDevice.getClass(), btDevice, mPin); // 手机和蓝牙采集器配对
//                    Log.d("da", "pin2=" + mPin);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING:
                    CommonUtils.toastShowShort(context, "正在配对......");
                    break;
                case BluetoothDevice.BOND_BONDED:
                    CommonUtils.toastShowShort(context, "蓝牙配对成功!");
                    iList.bondedSuccessful();
                    //存储打印的地址
//                        PreferenceUtils.setPrinterAddress(ServiceBluetooth.this, device.getAddress());
                    //存储打印的名称
//                        PreferenceUtils.setPrinterName(ServiceBluetooth.this, device.getName());
                    break;
                case BluetoothDevice.BOND_NONE:
                    CommonUtils.toastShowShort(context, "取消配对");
                default:
                    break;
            }
        }
    }

    public interface IList{
        void getList(ArrayList<PrinterBean> list);

        void bondedSuccessful();
    }
}
