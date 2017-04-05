package com.seuic.gaojie.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.seuic.gaojie.utils.LogUtils;

/**
 * Created by Dell on 2017/3/13.
 */

public class BarcodeExportBroadcast extends BroadcastReceiver {
    private String LOGTAG = "BarcodeExportBroadcast...";
    IText iText;
    Context mContext;

    public BarcodeExportBroadcast(IText iText) {
        this.iText = iText;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
       String barcode = intent.getStringExtra("scannerdata");
        LogUtils.d(LOGTAG,"条码;" + barcode);
        iText.setText(barcode);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    // 定义一个接口，让activity来访问broadcast里的方法
    public interface IText {
         void setText(String content);
    }
}
