package com.seuic.gaojie.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.seuic.gaojie.R;
import com.seuic.gaojie.application.BaseActivity;
import com.seuic.gaojie.utils.PreferencesUtils;
import com.seuic.gaojie.utils.ui.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/3/20.
 */

public class ActivitySetting extends BaseActivity {
    @BindView(R.id.tv_setting_versionName) TextView mTvVertionName;
    @BindView(R.id.et_setting_day) EditText mEtDay;
    @BindView(R.id.tv_merge_title)TextView mTvTitle;


    private Activity mActivity;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_setting);
        mActivity = this;
        ButterKnife.bind(this);
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText("系统设置");
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        mEtDay.setText(PreferencesUtils.getString(getApplicationContext(),"saveDays","7"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mTvVertionName.setText("ver:"+getVertionName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getVertionName() throws PackageManager.NameNotFoundException {
        return getPackageManager().getPackageInfo(getPackageName(), PackageManager.PERMISSION_GRANTED)
                .versionName;
    }

    @OnClick({R.id.bt_setting_query, R.id.bt_setting_cancel,R.id.iv_merge_back})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.bt_setting_query:
                String days = mEtDay.getText().toString().trim();
                if (days.equals("")) {
                    ToastUtils.showToast(this,"非法保存时间");
                    return;
               }
                int daysInt = (int) Double.parseDouble(days);
                if (daysInt > 7) {
                    ToastUtils.showToast(this,"保存天数介于1-7之间");
                    return;
                }
                if (daysInt<=0 ) {
                    ToastUtils.showToast(this,"保存天数不能小于0");
                }else {
                    sp.edit().putString("saveDays", days).commit();
                    finish();
                }
                break;
            case R.id.bt_setting_cancel:
                finish();
                break;
            case R.id.iv_merge_back:
                finish();
                break;
        }
    }
}
