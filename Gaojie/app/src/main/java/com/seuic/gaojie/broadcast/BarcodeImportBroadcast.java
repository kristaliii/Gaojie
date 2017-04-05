package com.seuic.gaojie.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.seuic.gaojie.Constant;
import com.seuic.gaojie.bean.Barcode;
import com.seuic.gaojie.utils.LogUtils;
import com.seuic.gaojie.utils.ui.DialogUtils;
import com.seuic.gaojie.utils.ui.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Dell on 2017/3/13.
 */

public class BarcodeImportBroadcast extends BroadcastReceiver {
    private String LOGTAG = "BarcodeImportBroadcast...";
    IText iText;
    ArrayList<Barcode> list;
    Context context;
    ArrayList<String> listSummary;
    ArrayList<String> tiaoMa = new ArrayList<>();

    public BarcodeImportBroadcast(IText iText) {
        this.iText = iText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (list == null || list.size() == 0) {
            ToastUtils.showToast(context, "还没导入数据");
            return;
        }
        if (tiaoMa.size() != list.size()) {
            for (int i=0;i<list.size();i++) {
                tiaoMa.add(list.get(i).getBarcode());
            }
        }
        String barcode = intent.getStringExtra(Constant.CUSTOM_KEY);
//        iText.setText(barcode);// 此处还是象方法

        if (!tiaoMa.contains(barcode)) {
            ToastUtils.showToast(context, "改条码不存在表格中");
            DialogUtils.disMissHintOne();
            return;
        }
        LogUtils.d(LOGTAG, "集合的长度：" + list.size());
        loop:
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBarcode().equals(barcode)) {// 存在于list中的符合要求的条码
                String type = list.get(i).getBarcodeType();
//                    String type = list.remove(i).getBarcodeType();// 中了就移除
                DialogUtils.hintOneDialog(context, type);
                // 把list中扫描到的条码添加到listSummary集合中
                if (listSummary.size() == 0) {// 首次执行，以后都不会执行
                    listSummary.add(barcode);
                }
                // 判断listSummary里面是否有重码的，重码就跳出大循环
                for (int j = 0; j < listSummary.size(); j++) {
                    if (barcode.equals(listSummary.get(j))) { // 汇总里面有重码
                        Log.i("tag", "BarcodeImportBroadcast...重码了");
                        listSummary = iText.getListSummary();
                        break loop; // 中断大循环
                    }
                }
                Log.e("tag", "BarcodeImportBroadcast...list.get(i).getBarcode():" + list.get(i).getBarcode());
                listSummary.add(barcode);
                listSummary = iText.getListSummary();
            }
        }
        iText.setText(barcode);// 此处还是抽象方法
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setInitList(ArrayList<Barcode> list) {
        this.list = list;
    }

    public void setInitListSumary(ArrayList<String> listSummary) {
        this.listSummary = listSummary;
    }

    // 定义一个接口，让activity来访问broadcast里的方法
    public interface IText {
        // 扫描条码，赋值到文本框
        void setText(String content);

        // 扫描后的匹配条码添加到集合；
        ArrayList<String> getListSummary();
    }
}
