package com.seuic.gaojie.utils.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.seuic.datepicker.MonthDateView;
import com.seuic.gaojie.R;
import com.seuic.gaojie.db.DbDAO;
import com.seuic.gaojie.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by Dell on 2017/3/16.
 */

public class DialogEdit {
    private String LOGTAG = "DialogEdit...";
    private Context mContext;
    private DbDAO dao;
    private ArrayList<String> list; // 显示item内数据的集合
    private BaseAdapter adapter;
    private String initText; // 点击item显示的原始数据

    public DialogEdit(Context context) {
        mContext = context;
    }

    public DialogEdit(Context context,DbDAO dao, ArrayList<String> list, BaseAdapter adapter, String initText) {
        mContext = context;
        this.dao = dao;
        this.list = list;
        this.adapter = adapter;
        this.initText = initText;
    }
    /**
     * 可编辑的对话框
     */
    private AlertDialog hintEditDialog;
    public void hintEditDialog( final int position){
        AlertDialog.Builder build = new AlertDialog.Builder(mContext);
        View inflate = View.inflate(mContext, R.layout.dialog_edit, null);
        TextView tv_dialog_title = (TextView) inflate.findViewById(R.id.tv_dialog_title);
        final EditText et_dialog_message = (EditText) inflate.findViewById(R.id.et_dialog_message);
        Button btn_ok = (Button) inflate.findViewById(R.id.btn_ok);
        Button btn_delete = (Button) inflate.findViewById(R.id.btn_delete);
        tv_dialog_title.setText("编辑");
        et_dialog_message.setText(initText);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 修改
                String content = et_dialog_message.getText().toString().trim();
                ContentValues values = new ContentValues();
                values.put("text",content);
                dao.update(values,"text=?",new String[]{initText});
                list.remove(position);
                list.add(position,content);
                adapter.notifyDataSetChanged(); // 可以更新UI
                hintEditDialog.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 删除
                String remove = list.remove(position);
                dao.deleteText(remove);
                adapter.notifyDataSetChanged();
                hintEditDialog.dismiss();
            }
        });
        build.setView(inflate);
        if (hintEditDialog != null) {
            hintEditDialog.dismiss();
        }
        hintEditDialog = build.create();
        hintEditDialog.show();
    }

    /**
     * 弹出日历对话框
     */
    private AlertDialog hintCalendar;
    public void hintCalendar(final Handler handler){
        AlertDialog.Builder build = new AlertDialog.Builder(mContext,AlertDialog.THEME_HOLO_LIGHT);
        View inflate = View.inflate(mContext, R.layout.dialog_calendar, null);
        ImageView iv_left = (ImageView)inflate. findViewById(R.id.iv_left);
        ImageView iv_right = (ImageView)inflate. findViewById(R.id.iv_right);
        TextView tv_date = (TextView)inflate. findViewById(R.id.date_text);
        TextView tv_week  =(TextView)inflate. findViewById(R.id.week_text);
        TextView tv_today = (TextView)inflate. findViewById(R.id.tv_today);
        final MonthDateView monthDateView = (MonthDateView)inflate. findViewById(R.id.monthDateView);

        monthDateView.setTextView(tv_date,tv_week);
//        monthDateView.setDaysHasThingList(list);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
//                ToastUtils.showToast(mContext,"点击了：" + monthDateView.getmSelDay());
                String year = ""+monthDateView.getmSelYear();
                int m = monthDateView.getmSelMonth()+1; // (月份)注意，这里需要+1
                String month = ""+m;
                if (m<10) {
                     month = "0"+m;
                }
                int d = monthDateView.getmSelDay(); // (天数)注意，这里需要在单数天前加0；
                String day =""+d;
                if (d<10) {
                    day ="0"+d;
                }
                // 这么写是为了和数据库里面的date达成一致；
                String dateResult = year+"-" + month+"-" + day;
                //2017318
                LogUtils.e(LOGTAG,"dateReult:"+ dateResult);
                if (hintCalendar != null) {
                    hintCalendar.dismiss();
                    Message msg = Message.obtain();
                    msg.obj = dateResult;
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        });
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDateView.onLeftClick();
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDateView.onRightClick();
            }
        });
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });

        build.setView(inflate);
        if (hintCalendar != null) {
            hintCalendar.dismiss();
        }
        hintCalendar = build.create();
        hintCalendar.show();
    }
}
