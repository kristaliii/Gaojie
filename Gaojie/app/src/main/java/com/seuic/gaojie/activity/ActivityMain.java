package com.seuic.gaojie.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.seuic.gaojie.R;
import com.seuic.gaojie.application.BaseActivity;
import com.seuic.gaojie.db.BarcodeDbDAO;
import com.seuic.gaojie.utils.LogUtils;
import com.seuic.gaojie.utils.PreferencesUtils;
import com.seuic.gaojie.utils.ui.DialogUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/3/13.
 */
public class ActivityMain extends BaseActivity {
    private String LOGTAG = "ActivityMain...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        ButterKnife.bind(this);

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/GaoJie");// mtt/sdcard
        if (!dir.exists()) {
            dir.mkdir(); // 来测试一下
        }
        deleteFromDatebase();
        LogUtils.i(LOGTAG,"oncreate");
    }

    @OnClick({R.id.bt_main_export, R.id.bt_main_import,R.id.bt_main_setting,R.id.iv_merge_back})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.bt_main_export://导出
                Intent intent = new Intent(this, ActivityExport.class);
                startActivity(intent);
                break;

            case R.id.bt_main_import://导入
                Intent intent1 = new Intent(this, ActivityImport.class);
                startActivity(intent1);
                break;

            case R.id.bt_main_setting: // 设置
                Intent intent2 = new Intent(this, ActivitySetting.class);
                startActivity(intent2);
                break;

            case R.id.iv_merge_back:
                DialogUtils.hintTwoDialog(this,"确认退出吗？");
                break;
        }
    }

    private void deleteFromDatebase() {
        try {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateFormat = format.format(date); // 当前日期 2017-03-22

            String saveDays = PreferencesUtils.getString(this, "saveDays","7");
            int saveDaysInt = Integer.parseInt(saveDays); // 保存的天数

            String dateStr1 = "2016-03-22";
            String dateStr2 = "2017-04-22";

            Date currentDate = format.parse(dateFormat); // 当前日期

            BarcodeDbDAO barcodeDbDAO = new BarcodeDbDAO(this);
            Cursor c = barcodeDbDAO.query();
            while (c.moveToNext()) { // 循环次数相当多；
                String queryDate = c.getString(c.getColumnIndex("date"));// 数据库中条码存在的日期
                Date date1 = format.parse(queryDate);
                int distance = differentDays(date1, currentDate);// 后面的比前面的大
                if (distance >= saveDaysInt) {
                    int i = barcodeDbDAO.deleteByDate(queryDate);
                    LogUtils.i(LOGTAG, "数据库删除："+i);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把形如 2017-03-22这样格式的日期转成int
     * @param date
     * @return
     */
    private int parseDateToInteger(String date) {
        String[] split = date.split("-");
        String parseDate = split[0]+split[1]+split[2];
        return Integer.parseInt(parseDate);
    }

    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public  int differentDays(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        // date1 = 2017-03-22;
        int day1 = cal1.get(Calendar.DAY_OF_YEAR); // 从开年到现在的天数 82 天
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR); // 2017
        int year2 = cal2.get(Calendar.YEAR);

        if (year1 != year2) {// 不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {// 闰年
                    timeDistance += 366;
                } else {// 不是闰年
                    timeDistance += 365;
                }
            }
            int i = timeDistance + (day2 - day1);
            LogUtils.i(LOGTAG, "(不同年)判断day2 - day1 : " +i);
            return timeDistance + (day2 - day1);
        } else {// 同年
            LogUtils.e(LOGTAG,"(同年)判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 再按一次则退出
     */
    boolean isExit;
    @Override
    public void onBackPressed() {
        if (!isExit) {
            Toast.makeText(this, "再点一次则退出", Toast.LENGTH_SHORT).show();
            isExit = true;
            Handler h = new Handler(Looper.getMainLooper());
            // h.sendMessageDelayed(msg, delayMillis)
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }
}
