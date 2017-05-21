package com.seuic.yapei;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/4/13.
 */

public class ActivityOutSell extends AppCompatActivity {
    @BindView(R.id.iv_merge_delete)ImageView iv_merge_delete;
    @BindView(R.id.tv_merge_title)TextView tv_merge_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_sell);
        ButterKnife.bind(this);
        iv_merge_delete.setVisibility(View.INVISIBLE);
        tv_merge_title.setText("销售出库");

    }

    @OnClick(R.id.tv_out_sell_test)
    void onButtonClick(View view) {
        Intent intent = new Intent(this, ActivityScan.class);
        startActivity(intent);
    }
}
