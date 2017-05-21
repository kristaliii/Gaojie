package com.seuic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.seuic.bluetoothandwifiprint.R;
import com.seuic.bluetoothprint.ActivityBluetooth;
import com.seuic.wifiprint.WifiActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by someone on 2017/4/24.
 */

public class MainActivity extends Activity{

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_main_bluetooth, R.id.bt_main_wifi})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.bt_main_bluetooth: // 蓝牙打印
                startActivity(new Intent(getApplicationContext(), ActivityBluetooth.class));
                break;
            case R.id.bt_main_wifi: // wifi打印
                startActivity(new Intent(getApplicationContext(), WifiActivity.class));
                break;
        }
    }
}
