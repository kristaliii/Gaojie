package com.seuic.gaojie.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seuic.gaojie.Constant;
import com.seuic.gaojie.R;
import com.seuic.gaojie.adapter.BarcodeImportAdapter;
import com.seuic.gaojie.application.BaseActivity;
import com.seuic.gaojie.bean.Barcode;
import com.seuic.gaojie.broadcast.BarcodeImportBroadcast;
import com.seuic.gaojie.broadcast.BroadcastUtils;
import com.seuic.gaojie.utils.ExcelTip;
import com.seuic.gaojie.utils.LogUtils;
import com.seuic.gaojie.utils.MyAsyncTask;
import com.seuic.gaojie.utils.ui.DialogUtils;
import com.seuic.gaojie.utils.ui.ProgressDlgUtil;
import com.seuic.gaojie.utils.ui.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/3/13.
 */
public class ActivityImport extends BaseActivity implements BarcodeImportBroadcast.IText,AbsListView.OnScrollListener{
    @BindView(R.id.lv_import_barcode)  ListView lvBarcode;
    @BindView(R.id.et_import_scan)  EditText etBarcode;
    @BindView(R.id.iv_merge_menu) ImageView mIvMenu;
    @BindView(R.id.tv_merge_title) TextView mTvTitle;
    @BindView(R.id.tv_import_noMatch) TextView mTvNoMatch;
    @BindView(R.id.iv_merge_back) ImageView mIvBack;

    private static String LOGTAG = "ActivityImport...";

    ActivityImport mActivity;
    private ArrayList<Barcode> list; // 导入excel表格后封装好的list
    private ArrayList<String> listSummary ; // 汇总扫入的存在于excel表格中的条码
    private ArrayList<Barcode> listNoMatch; // excel表格中还有这么单子没有扫

//    int lastItemid = lvBarcode.getLastVisiblePosition(); // 获取当前屏幕最后Item的ID
    private boolean isLoading;//表示是否正在加载

    private BarcodeImportBroadcast receiver;
    private  int MAX_COUNT ;//表示服务器上总共有MAX_COUNT条数据
    private final int EACH_COUNT = 9;//表示每次加载的条数
    private View mFooterView;
    private Handler handler=new Handler();
    private BarcodeImportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_import);
        ButterKnife.bind(this);
        mIvMenu.setVisibility(View.VISIBLE);
        mIvMenu.setBackground(getResources().getDrawable(R.drawable.selector_export));
        mTvTitle.setText("导入");
        mActivity = this;
        mFooterView = getLayoutInflater().inflate(R.layout.footer, null);

        listSummary = new ArrayList<>();
        listNoMatch = new ArrayList<>();

        receiver = new BarcodeImportBroadcast(this);
        receiver.setContext(this);
        BroadcastUtils.initBroadcastReciever(this);
        BroadcastUtils.registerReceiver(this, receiver);
        receiver.setInitListSumary(listSummary);// 注意；

        lvBarcode.setOnScrollListener(mActivity);

    }

    /**
     * ITest子类实现方法，扫描条码赋值到文本框
     */
    @Override
    public void setText(String content) {
        etBarcode.setText(content);
//        if (listSummary != null && listSummary.size() !=0) {
//            listSummary = getListSummary();
//            mTvNoMatch.setVisibility(View.VISIBLE);
//            for(int i=0;i<listSummary.size();i++){
//                mTvNoMatch.setText("未扫描条码个数："+(list.size() - listSummary.size()));
//            }
//        }
    }

    /**
     * 按钮点击事件
     */
   @OnClick({R.id.bt_import_import,R.id.bt_import_summary,R.id.iv_merge_back,R.id.iv_merge_menu})
     void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.bt_import_import: // 导入excel表格
                importListener();
                break;
            case R.id.bt_import_summary: // 汇总
                summary();
                break;
            case R.id.iv_merge_back: // 返回
                finish();
                break;
            case R.id.iv_merge_menu: // 导出   导出已经扫描的和未扫描的数据
                exportExcel();
                break;
        }
    }
    /**
     * 点击---导入事件
     */
    private void importListener() {
        if (list != null && list.size() != 0) {
            ToastUtils.showToast(mActivity, "已导入，无需重复导入");
        }else {
            String path = Environment.getExternalStorageDirectory() + "/" + Constant.FILE_DIR;
            final File file = new File(path + "/"+ Constant.IMPORT_EXCEL_NAME);
            if (!file.exists()) {
                ToastUtils.showToast(mActivity, "文件不存在");
            } else {
                new MyAsyncTask() {
                    @Override
                    public void preTask() {
                        ProgressDlgUtil.showProgress("请稍等...",mActivity);
                    }
                    @Override
                   public void doInBack() {
                        LogUtils.d(LOGTAG,"导入文件为："+file.getPath());
                        list = ExcelTip.readExcel(file.getPath());
                    }
                    @Override
                    public void postTask() {
                        importExcel();
                    }
                }.excuted();
            }
        }
    }
    /**
     * MyAsync中，子线程之后执行事件，
     * 就是把List传给Receiver，以及分批显示ListView
     */
    private void importExcel() {
        ProgressDlgUtil.dismissProgress();
        if (list.size() == 0) {
            // list集合长度为0
            ToastUtils.showToast(mActivity, "book.xls中没有数据");
            return;
        }
        MAX_COUNT = list.size();
        receiver.setInitList(list);

        // 以下为分批加载步骤
        lvBarcode.addFooterView(mFooterView);// 设置列表底部视图
        ArrayList<Barcode> listOriginal = new ArrayList<>();  // 一个空的集合
        adapter = new BarcodeImportAdapter(mActivity,listOriginal);
        lvBarcode.setAdapter(adapter);
    }

    /**
     * ListView滑动事件监听
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount!=MAX_COUNT) {
            //isLoading = true 表示正在加载，加载完毕设置isLoading =false；
            Log.i("onScroll", "firstVisibleItem:"+firstVisibleItem+" visibleItemCount:"+visibleItemCount+" totalItemCount:"+totalItemCount);
            isLoading = true;
            //如果服务端还有数据，则继续加载更多，否则隐藏底部的加载更多
            LogUtils.d(LOGTAG,"MAX_COUNT:"+MAX_COUNT);
            if (totalItemCount<=MAX_COUNT) {
                //等待2秒之后才加载，模拟网络等待时间为2s
                handler.postDelayed(new Runnable() {
                    public void run() {
                        loadMoreData();
                    }
                },1000);
            }else{
                if (lvBarcode.getFooterViewsCount()!=0) {
                    lvBarcode.removeFooterView(mFooterView);
                }
            }
        }
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}
    /**
     * 分批加载数据（重点）
     */
    private void loadMoreData(){
        int count = adapter.getCount();
        for (int i = 0; i < EACH_COUNT; i++) {
            if (count+i<MAX_COUNT) {
                Barcode item = list.get(count+i);
                item.setBarcode(item.getBarcode());
                item.setBarcodeType(item.getBarcodeType());
                // 新增一个item的背景属性
                item.setColor(getResources().getDrawable(R.drawable.shape_editext));
                adapter.addBarcodeItem(item);
            }else{
                lvBarcode.removeFooterView(mFooterView);
            }
        }
        adapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * 点击---汇总事件
     */
    private void summary() {
        if (list == null || list.size() == 0) {
            ToastUtils.showToast(mActivity, "导入数据为空");
        } else {
            if (listSummary != null) {
                listSummary = getListSummary();
                listNoMatch.clear();
                LogUtils.d(LOGTAG,"listSummary:"+listSummary.size());
            }
            LogUtils.i(LOGTAG,"原集合的大小："+list.size());
            for(int i = 0; i<list.size();i++) {
                String barcode = list.get(i).getBarcode();
                String barcodeType = list.get(i).getBarcodeType();
                // 说明listSummary里面不存在list的条码
                if (!listSummary.contains(barcode)) {
                    Barcode item = new Barcode();
                    item.setBarcode(barcode);
                    item.setBarcodeType(barcodeType);
                    item.setColor(getResources().getDrawable(R.drawable.shape_editextother));
                    listNoMatch.add(item);
                }
            }
            mTvNoMatch.setVisibility(View.VISIBLE);
            mTvNoMatch.setText("未扫描条码："+listNoMatch.size()+"个");
            BarcodeImportAdapter adapterNoMatch = new BarcodeImportAdapter(mActivity, listNoMatch);
            lvBarcode.setAdapter(adapterNoMatch);
            DialogUtils.hintOneDialog(mActivity,"未扫描的条码个数:"+listNoMatch.size());
        }
    }
    /**
     * @return 已经汇总的条码 ---ITest子类
     */
    @Override
    public ArrayList<String> getListSummary() {
        return listSummary;
    }

    /**
     * 点击导出---导出 ，导出为excel表格
     */
    private void exportExcel() {
        if (list == null || list.size() == 0 || listNoMatch.size()==0) { // 二者中有一个为true即true；
            DialogUtils.hintOneDialog(mActivity, "数据为空");
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
                    if (listNoMatch.size() != 0) {// 导出未扫描的
                        ExcelTip.writeExcelInImport(mActivity, Constant.NO_MATCH_NAME
                                + ".xls","sheet", listNoMatch);
                    }
                    boolean b = list.removeAll(listNoMatch);
                    LogUtils.e(LOGTAG, "b...:"+b);
                    ExcelTip.writeExcelInImport(mActivity, Constant.MATHCH_NAME // 导出已扫描的
                                +".xls","sheet",list);
                }
                @Override
                public void postTask() {
                    ProgressDlgUtil.dismissProgress();
//            list.clear(); // 暂时先这么计划，后续可能要用finish来替换；
                    ToastUtils.showToast(mActivity, "导出成功");
                    finish();
                }
            }.excuted();
        }
    }
    /**
     * 每个activity在退出时及时取消注册广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    /**
     * 退出事件
     */
    @Override
    public void onBackPressed() {
        if (listSummary.size() == 0) {
            finish();
            return;
        }
        if (listNoMatch.size() != 0) {
            DialogUtils.hintTwoDialog(mActivity,"有未完成单据，确定退出吗？");
        }
    }
}
