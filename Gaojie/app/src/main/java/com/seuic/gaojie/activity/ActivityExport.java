package com.seuic.gaojie.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seuic.gaojie.Constant;
import com.seuic.gaojie.R;
import com.seuic.gaojie.adapter.BarcodeAdapter;
import com.seuic.gaojie.application.BaseActivity;
import com.seuic.gaojie.bean.Barcode;
import com.seuic.gaojie.broadcast.BarcodeExportBroadcast;
import com.seuic.gaojie.broadcast.BroadcastUtils;
import com.seuic.gaojie.db.BarcodeDbDAO;
import com.seuic.gaojie.db.DbDAO;
import com.seuic.gaojie.sound.SoundPlayUtuils;
import com.seuic.gaojie.utils.ExcelTip;
import com.seuic.gaojie.utils.MyAsyncTask;
import com.seuic.gaojie.utils.ui.DialogEdit;
import com.seuic.gaojie.utils.ui.DialogUtils;
import com.seuic.gaojie.utils.ui.ProgressDlgUtil;
import com.seuic.gaojie.utils.ui.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityExport extends BaseActivity implements BarcodeExportBroadcast.IText {

    @BindView(R.id.etScan) EditText mEtScan;
    @BindView(R.id.tvType) TextView mTvType;
    @BindView(R.id.lvBarcode)   ListView lvBarcode;
    @BindView(R.id.iv_merge_menu) ImageView mIvMenu;
    @BindView(R.id.tv_merge_title) TextView mTvTitle;
    @BindView(R.id.tv_export_calendar) TextView mTvCalendar;

    private String LOGTAG = "ActivityExport...";
    BarcodeExportBroadcast receiver;
    Activity mActivity;
    private ArrayList<Barcode> list;  // 扫描条码，添加条码到此集合
    private ArrayList<String> listExtra; // 条码类型，从数据库获取
    private String[] items; // 文本框选择项
    private DbDAO dao;// 条码类型 数据库
    private BarcodeDbDAO barcodeDbDAO; // 条码类 数据库
    private BarcodeAdapter adapter;
    private ArrayList<Barcode> listCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_export);
        ButterKnife.bind(this);
        mIvMenu.setVisibility(View.VISIBLE);
        mTvTitle.setText("导出");
        mActivity = this;
        list = new ArrayList<>();
        listExtra = new ArrayList<>();
        selectItems = new ArrayList<>();
        listCalendar = new ArrayList<>();

        BroadcastUtils.initBroadcastReciever(this);
        receiver = new BarcodeExportBroadcast(this);
        BroadcastUtils.registerReceiver(this, receiver);
        receiver.setContext(mActivity);

        dao = new DbDAO(mActivity); // 条码类型 数据库
        barcodeDbDAO = new BarcodeDbDAO(mActivity); // 条码类  数据库

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatDate = format.format(date);
        mTvCalendar.setText(formatDate);

        setItemClick();

        SoundPlayUtuils.initplaySound(getApplicationContext(),R.raw.error1);//重码
    }

    @Override
    protected void onResume() {
        super.onResume();
        listExtra.clear();
        Cursor c = dao.query();
        while (c.moveToNext()) {
            String text = c.getString(c.getColumnIndex("text"));
            listExtra.add(0, text);
        }
        items = new String[listExtra.size()];
        for (int i = 0; i < listExtra.size(); i++) {
            items[i] = listExtra.get(i);
        }

    }

    /**
     * 点击item执行事件
     */
    private void setItemClick() {
        lvBarcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.showToast(mActivity, "position:"+position);
                Barcode item = adapter.getItem(position);
                if (item.isChoice()) {
                    item.setChoice(false);
                }else {
                    item.setChoice(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * ITest的子类实现
     * @param content
     */
    @Override
    public void setText(String content) {
        String type = mTvType.getText().toString(); // 条码类型
        if (type.equals("")) {
            ToastUtils.showToast(mActivity, "请选择类型");
            return;
        }
        mEtScan.setText(content);

        if (listCalendar.size() != 0) {
            listCalendar.clear();
        }
        String barcode = mEtScan.getText().toString(); // 条码
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i ++) {
                boolean equals = list.get(i).getBarcode().equals(barcode);
                if (equals) {
                    SoundPlayUtuils.playSound();
                    ToastUtils.showToast(mActivity, "重码！");
                    return;
                }
            }
        }
        Cursor c = barcodeDbDAO.queryByBarcode(barcode);
        while (c.moveToNext()) {
            String barcodeDatebase = c.getString(c.getColumnIndex("barcode"));
            String dateDatebase = c.getString(c.getColumnIndex("date"));
            if (barcodeDatebase.equals(barcode)) {
                DialogUtils.hintOneDialog(mActivity, "该条码已在"+dateDatebase+"导出");
                return;
            }
        }
//                ToastUtils.showToast(mActivity, "添加成功");
        if (barcode.equals("")) {
            return;
        }
        Barcode item = new Barcode();
        item.setBarcode(barcode);
        item.setBarcodeType(type);
        // 新加一个item的背景属性 ，这里是默认的背景，白色
        item.setColor(getResources().getDrawable(R.drawable.shape_editext));
        list.add(0, item);
        adapter = new BarcodeAdapter(mActivity, list);
        lvBarcode.setAdapter(adapter);
        mEtScan.setText("");
    }

    /**
     * 按钮点击事件
     * @param v
     */
    @OnClick({R.id.tvType, R.id.btExport, R.id.iv_merge_menu, R.id.bt_export_calendar,
    R.id.iv_merge_back,R.id.bt_export_delete})
    void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.tvType: // 条码类型
                setTypeDialog();
                break;
            case R.id.btExport: // 导出---保存为excel表格
                exportExcel();
                break;
            case R.id.iv_merge_menu:// 编辑
                Intent intent = new Intent(this, ActivityEdit.class);
                startActivity(intent);
                break;
            case R.id.bt_export_calendar: // 日历按钮
                DialogEdit dialogEdit = new DialogEdit(mActivity);
                dialogEdit.hintCalendar(handler);
                break;
            case R.id.bt_export_delete: // 删除
                if (list.size() == 0) { // 删除日历
                    delete(listCalendar);
                    deleteFromDatabase();
                    selectItems.clear();
                }else {// 删除扫描的集合
                    delete(list);
                    selectItems.clear();
                }
                break;
            case R.id.iv_merge_back: // 返回
                if (list.size() != 0) {
                    DialogUtils.hintTwoDialog(mActivity, "数据未导出,确认退出吗？");
                }else {
                    finish();
                }
                break;
        }
    }
    /**
     * 点击---条码类型
     */
    private void setTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setItems(items,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (listCalendar.size() != 0) {
//                    listCalendar.clear();
//                }
                mTvType.setText(items[which]);
//                String barcode = mEtScan.getText().toString(); // 条码
//                if (list.size() != 0) {
//                    for (int i = 0; i < list.size(); i ++) {
//                        boolean equals = list.get(i).getBarcode().equals(barcode);
//                       if (equals) {
//                            SoundPlayUtuils.playSound();
//                            ToastUtils.showToast(mActivity, "重码！");
//                            return;
//                        }
//                    }
//                }
//                Cursor c = barcodeDbDAO.queryByBarcode(barcode);
//                while (c.moveToNext()) {
//                    String barcodeDatebase = c.getString(c.getColumnIndex("barcode"));
//                    String dateDatebase = c.getString(c.getColumnIndex("date"));
//                    if (barcodeDatebase.equals(barcode)) {
//                        DialogUtils.hintOneDialog(mActivity, "该条码已在"+dateDatebase+"导出");
//                        return;
//                    }
//                }
////                ToastUtils.showToast(mActivity, "添加成功");
//                if (barcode.equals("")) {
//                    return;
//                }
//                String type = mTvType.getText().toString(); // 条码类型
//                Barcode item = new Barcode();
//                item.setBarcode(barcode);
//                item.setBarcodeType(type);
//                // 新加一个item的背景属性 ，这里是默认的背景，白色
//                item.setColor(getResources().getDrawable(R.drawable.shape_editext));
//                list.add(0, item);
//                adapter = new BarcodeAdapter(mActivity, list);
//                lvBarcode.setAdapter(adapter);
//                mEtScan.setText("");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    /**
     * 接收DialogEdit里面的日历对话框dismiss后return的结果
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (list.size() !=0) {
                    DialogUtils.hintOneDialog(mActivity, "还没导出数据");
                    return;
                }
                receiveCalendarData(msg);
            } else if (msg.what == 1) {
                adapter.notifyDataSetChanged();
            }
        }
    };
    /**
     * 点击日历---日历的日期返回后更新主界面listview
     * @param msg
     */
    private void receiveCalendarData(Message msg) {
        if (list.size() != 0 || listCalendar.size() !=0) {
            list.clear();
            listCalendar.clear();
        }
        String dateMsg = (String) msg.obj;
        mTvCalendar.setText(dateMsg);
        Cursor c = barcodeDbDAO.queryByDate(dateMsg);
        while (c.moveToNext()) {
            Barcode item = new Barcode();
            item.setBarcode(c.getString(c.getColumnIndex("barcode")));
            item.setBarcodeType(c.getString(c.getColumnIndex("barcodeType")));
            item.setDate(dateMsg);
            // 这里表示已经导出成功的条码
            item.setColor(getResources().getDrawable(R.drawable.shape_edit_done));
            listCalendar.add(0,item);
        }
        adapter = new BarcodeAdapter(mActivity, listCalendar);
        lvBarcode.setAdapter(adapter);
    }
    /**
     * 点击导出---导出 ，导出为excel表格
     */
    private void exportExcel() {
        if (list.size() == 0) {
            ToastUtils.showToast(mActivity, "数据为空");
        } else {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            final String excelName = format.format(date);
            new MyAsyncTask() {
                @Override
                public void preTask() {
                    ProgressDlgUtil.showProgress(null,mActivity);
                }
                @Override
                public void doInBack() {
                    ExcelTip.writeExcelUseFormat(mActivity, Constant.EXPORT_EXCEL_NAME + excelName+".xls",
                            "test", list);
                    exportDB();
                }
                @Override
                public void postTask() {
                    ProgressDlgUtil.dismissProgress();
//            list.clear(); // 暂时先这么计划，后续可能要用finish来替换；
                    ToastUtils.showToast(mActivity,"导出成功");
                    finish();
                }
            }.excuted();
        }
    }

    /**
     * 点击导出---导出，把条码类插入数据库
     */
    private void exportDB() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = format.format(date);
        for (int i = 0; i < list.size(); i++) {
            Barcode item = list.get(i);
            ContentValues values = new ContentValues();
            values.put("barcode", item.getBarcode());
            values.put("barcodeType", item.getBarcodeType());
            values.put("date", currentDate);
            barcodeDbDAO.insert(values);
        }
    }

    /**
     * 点击---删除
     * @param list
     */
    ArrayList<Barcode> selectItems;
    private void delete(ArrayList<Barcode> list) {
        if ( list.size() == 0) {
            return;
        }
        for (int i = 0;i<list.size();i++) { // i = 0,1,2,3
            Barcode item = list.get(i);
            if (item.isChoice()) {
                selectItems.add(item);
//                LogUtils.i(LOGTAG,"长度："+list.size());
            }
        }
        list.removeAll(selectItems);
        handler.sendEmptyMessage(1);
    }

    private void deleteFromDatabase() {
        for(int i=0;i<selectItems.size();i++) {
            // 此处删除数据库中的数据是根据条码来删除，所以若果有相同条码，即使不在selectItems里面，也会被删除
            // 如果用户需要重码，那么就得想一个唯一标识来表示每一个listview的item；
            barcodeDbDAO.deleteByBarcode(selectItems.get(i).getBarcode());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (list.size() != 0) {
            DialogUtils.hintTwoDialog(mActivity, "数据未导出,确认退出吗？");
        }else {
            finish();
        }
    }
}