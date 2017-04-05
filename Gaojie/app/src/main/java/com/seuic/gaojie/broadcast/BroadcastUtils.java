package com.seuic.gaojie.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.seuic.gaojie.Constant;

/**
 * Created by Dell on 2017/3/13.
 */

public class BroadcastUtils {
    /**
     * 初始化广播接收器，AUTOID系列安卓产品上的系统软件扫描工具相对应
     */
    public static void initBroadcastReciever(Context context) {
        // 发送广播到扫描工具内的应用设置项
        Intent intent = new Intent(Constant.BROADCAST_SETTING);
        intent.putExtra(Constant.BROADCAST_VALUE, Constant.CUSTOM_NAME);
        intent.putExtra(Constant.BROADCAST_KEY, Constant.CUSTOM_KEY);
        intent.putExtra(Constant.SEND_KEY, "BROADCAST");
        intent.putExtra(Constant.END_KEY, "NONE");
        intent.putExtra(Constant.SCAN_CONTINUE, false);
        intent.putExtra(Constant.VIBERATE, false);
        intent.putExtra(Constant.SOUND_PLAY, true);
        context.sendBroadcast(intent);
    }

    /**
     * 注册自定义的广播
     */
    public static void registerReceiver(Context context,BroadcastReceiver receiver){
        IntentFilter filter = new IntentFilter(Constant.CUSTOM_NAME);
        context.registerReceiver(receiver, filter);
    }
}
