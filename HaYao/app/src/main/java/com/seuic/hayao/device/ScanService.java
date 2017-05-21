package com.seuic.hayao.device;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.seuic.hayao.R;
import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.sound.SoundManager;
import com.seuic.hayao.util.AndroidInfoGetter;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.IScanner;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

public class ScanService extends Service implements DecodeInfoCallBack {
    private final String LOG_TAG = "ScanService";

    private static final int NOTIFICATION_ID = 0x124452;

    private Thread mThread;

    private Scanner mScanner;

    private boolean isOpened;

    private DataManager mDataManager;

    private boolean isContinueScan = false;

    private ScanSettingReceiver mContinueScanReciver;

    private ScreenOnOffReceiver mScreenOnOffReceiver;

    SoundManager manager = SoundManager.getInstance();

    boolean isScreenOn = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.logo))
                .setAutoCancel(false)
                .setContentTitle("扫描服务启动");

        Notification notification = builder.build();

//        notification.flags |= Notification.FLAG_NO_CLEAR; // | 如果相对应位都是0，则结果为0，否则为1
        notification.flags = Notification.FLAG_NO_CLEAR; // 2017年4月14日11:24:37 我改的，和上面一样的效果，我觉得；
        /**
         * 前台进程、可视进程、服务进程、后台进程、空进程
         *  服务进程变成了前台进程
         */
        this.startForeground(NOTIFICATION_ID, notification);

        mDataManager = DataManager.getInstance();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isOpened) {
            isOpened = mScanner.open();
        }

        mContinueScanReciver = new ScanSettingReceiver();
        IntentFilter ContinueScanFilter = new IntentFilter();
        ContinueScanFilter.addAction(ScannerHelper.ACTION_SCANNER_APP_CONTINUNE_SETTINGS);
        registerReceiver(mContinueScanReciver, ContinueScanFilter);

        mScreenOnOffReceiver = new ScreenOnOffReceiver();
        IntentFilter screenOffIntentFilter = new IntentFilter();
        screenOffIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenOffIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOnOffReceiver, screenOffIntentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void init() {

        mScanner = ScannerFactory.getScanner(getApplicationContext());
        mScanner.setDecodeInfoCallBack(this);
        isOpened = mScanner.open();

        if (mScanner != null) {
            mThread = new Thread(runnable);
            mThread.start();
        }
    }


    @Override
    public void onDestroy() {
        this.stopForeground(true);

        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }

        if (mScanner != null) {
            mScanner.setDecodeInfoCallBack(null);
            mScanner.close();
        }
        isOpened = false;
        if (mContinueScanReciver != null) {
            unregisterReceiver(mContinueScanReciver);
        }
        if (mScreenOnOffReceiver != null) {
            unregisterReceiver(mScreenOnOffReceiver);
        }

        super.onDestroy();
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            int ret = ScannerKey.open();
            if (ret > -1) {
                while (true) {
                    ret = ScannerKey.getKeyEvent();
                    if (isScreenOn) {
                        switch (ret) {
                            case ScannerKey.KEY_DOWN:
                                if (isContinueScan) {
                                    if (!ContinueDecode.isRunning()) {
                                        ContinueDecode.start(ScanService.this);
                                    } else {
                                        ContinueDecode.terminate();
                                    }
                                } else {
                                    mScanner.startScan();
                                }
                                break;
                            case ScannerKey.KEY_UP:
                                if (!isContinueScan) {
                                    mScanner.stopScan();
                                }
                                break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onDecodeComplete(DecodeInfo info) {
        sendBarcode(info);
    }

    void sendBarcode(DecodeInfo info) {
        Log.d("hu", manager + info.barcode);
        if (manager != null) {
            manager.playScan();
        }
        if (info != null && mDataManager != null) {
            mDataManager.onBarCodeReceive(filterInvisibleChars(info.barcode));
        }
    }

    private String filterInvisibleChars(String barcode) {
        StringBuilder stb = new StringBuilder(barcode);
        int index = 0;
        while (index < stb.length()) {
            char c = stb.charAt(index);
            if (c <= 0x20) {
                stb.deleteCharAt(index);
            } else {
                index++;
            }
        }

        return stb.length() == barcode.length() ? barcode : stb.toString();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ScanService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidInfoGetter.isServiceRunning(context, ScanService.class);
    }

    public class ScanSettingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isContinueScan = intent.getIntExtra("value", 0) == 0 ? false : true;
            if (isContinueScan) {
                mScanner.setParams(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS, 1);
            } else {
                ContinueDecode.terminate();
                mScanner.stopScan();
                mScanner.setParams(IScanner.ParamCode.CONTINUOUS_SCAN_FLAGS, 0);
            }
        }
    }

    public class ScreenOnOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                isScreenOn = true;
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                isScreenOn = false;
            }
        }
    }

}
