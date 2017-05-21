package com.seuic.wifiprint;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.seuic.BaseActivity;
import com.seuic.bluetoothandwifiprint.R;
import com.seuic.bluetoothmain.AlertDialogUtil;
import com.seuic.bluetoothmain.ProgressDlgUtil;
import com.seuic.bluetoothmain.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.connect.connectImpl.WifiConnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by bgl on 2017/4/25.
 */

public class WifiActivity extends BaseActivity {

    @BindView(R.id.ll_wifi_info)LinearLayout ll_wifi_info;
    @BindView(R.id.lv_wifi_result) ListView lv_wifi_result;
    @BindView(R.id.sp_wifi_instrution_set)Spinner sp_wifi_instrution_set;
    @BindView(R.id.et_port_number)EditText et_port_number;
    @BindView(R.id.bt_wifi_discover) Button bt_wifi_discover;
    @BindView(R.id.btn_connect_wifi) Button btn_connect_wifi;
    @BindView(R.id.btn_wifi_print) Button btn_wifi_print;

    private static final String DEVICE_NAME = "DEVICE_NAME";
    private static final String DEVICE_IP = "DEVICE_IP";
    private ArrayAdapter<String> instrutionAdapter;
    String wifi_connect_ip;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ButterKnife.bind(this);

        List<String> instrutionList = new ArrayList<String>();
        for (int i = 0; i < InstructionType.values().length; i++) {
            instrutionList.add(InstructionType.values()[i].toString());
        }
        instrutionAdapter = new ArrayAdapter<String>(WifiActivity.this, android.R.layout.simple_spinner_item,
                instrutionList);

        bt_wifi_discover.setOnClickListener(new WifiDiscoverListener());
        btn_connect_wifi.setOnClickListener(new WifiConnectListener());
        btn_wifi_print.setOnClickListener(new BarcodePrinterListener(WifiActivity.this));
        if (ll_wifi_info.getVisibility() == View.VISIBLE) {
            ll_wifi_info.setVisibility(View.GONE);
        }

    }
    ArrayList<Map<String, String>> wifi_devices_info = new ArrayList<Map<String, String>>();
    private class WifiDiscoverListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ProgressDlgUtil.showProgress(null,WifiActivity.this);
            new Thread(){
                @Override
                public void run() {
                    if (!WifiConnect.isWiFiActive(WifiActivity.this)) {
                        AlertDialogUtil.showDialog("please open wifi first", WifiActivity.this);
                        return;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ll_wifi_info.getVisibility() == View.VISIBLE) {
                                ll_wifi_info.setVisibility(View.GONE);
                            }
                        }
                    });

                    Map<String, String> printInfoMap = null;

                    Map<String, String> wifi_item = null;

                    try {
                        printInfoMap = WifiConnect.searchDevice();
                        for (Map.Entry<String, String> entry : printInfoMap.entrySet()) {
                            wifi_item = new HashMap<String, String>();
                            wifi_item.put(DEVICE_NAME, entry.getValue());
                            wifi_item.put(DEVICE_IP, entry.getKey());
                            wifi_devices_info.add(wifi_item);
                        }

                    } catch (Exception e) {
                        AlertDialogUtil.showDialog(e, WifiActivity.this);
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDlgUtil.dismissProgress();
                            SimpleAdapter wifiAdapter = new SimpleAdapter(WifiActivity.this, wifi_devices_info,
                                    R.layout.item_wifi_devices, new String[]{DEVICE_NAME, DEVICE_IP}, new int[]{R.id.wifi_device_name, R.id.wifi_device_ip});
                            lv_wifi_result.setAdapter(wifiAdapter);
                            lv_wifi_result.setSelector(android.R.color.background_light);
                            lv_wifi_result.setOnItemClickListener(new AbsListView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    @SuppressWarnings({"unchecked", "rawtypes"})
                                    Map<String, String> map = (Map) parent.getItemAtPosition(position);
                                    wifi_connect_ip =  map.get(DEVICE_IP);
                                    et_port_number.setText("9100");
                                    sp_wifi_instrution_set.setAdapter(instrutionAdapter);
                                    ll_wifi_info.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
            }.start();

        }
    }

    private class WifiConnectListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (btn_connect_wifi.getText().equals("连接")) {
                if ("".equals(et_port_number.getText().toString())) {
                    Toast.makeText(WifiActivity.this, "please input port number", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    WifiConnect wifiConnect = new WifiConnect(wifi_connect_ip, Integer.parseInt(et_port_number.getText().toString()));
                    SNBCApplication application = (SNBCApplication) WifiActivity.this.getApplication();
                    wifiConnect.DecodeType(application.getDecodeType());
                    wifiConnect.connect();
                    BarPrinter.BarPrinterBuilder builder = new BarPrinter.BarPrinterBuilder();
                    builder.buildDeviceConnenct(wifiConnect);
                    String mType = sp_wifi_instrution_set.getItemAtPosition(sp_wifi_instrution_set.getSelectedItemPosition()).toString();
                    builder.buildInstruction(InstructionType.valueOf(mType));
//                    builder.buildInstruction(InstructionType.valueOf("BPLA"));
                    final BarPrinter printer = builder.getBarPrinter();
                    application.setPrinter(printer);
                    application.setConnect(wifiConnect);

                    updateStoredBuiltinFontArray(application, printer, WifiActivity.this);
                    updateOSFontFileArray(application, printer, WifiActivity.this);
                    updateOSFormatFileArray(application, printer, WifiActivity.this);
                    updateDiskSymbol(application, printer, WifiActivity.this);
                    updateOSImageFileForPrintArray(application, printer, WifiActivity.this);
                    updateStoredCustomFontArray(application, printer, WifiActivity.this);
                    updateStoredImageArray(application, printer, WifiActivity.this);
                    updateStoredFormatArray(application, printer, WifiActivity.this);
                    updateOSImageFileArray(application, printer, WifiActivity.this);
                    btn_connect_wifi.setText("断开连接");

                    if (InstructionType.valueOf(mType) != InstructionType.BPLA) {
                        AlertDialogUtil.showDialog("  Connect to print successful!\r\n The printer's name is " + printer.labelQuery().getPrinterName(), WifiActivity.this);
                    } else {
                        AlertDialogUtil.showDialog("  Connect to print successful!", WifiActivity.this);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialogUtil.showDialog(e, WifiActivity.this);
                        }
                    });

                }
            } else if (btn_connect_wifi.getText().toString().equals("断开连接")) {
                WifiConnect wifiConnect = (WifiConnect) ((SNBCApplication) WifiActivity.this.getApplication()).getConnect();
                if (null != wifiConnect) {
                    try {
                        wifiConnect.disconnect();
                        applicationClean(WifiActivity.this);
                        btn_connect_wifi.setText("连接");
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialogUtil.showDialog(e, WifiActivity.this);
                    }
                }
            }

        }
    }
}
