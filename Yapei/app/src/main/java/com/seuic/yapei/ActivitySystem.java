package com.seuic.yapei;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 2017/4/13.
 */

public class ActivitySystem extends AppCompatActivity {
    @BindView(R.id.iv_merge_delete)ImageView iv_merge_delete;
    @BindView(R.id.tv_merge_title)TextView tv_merge_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        ButterKnife.bind(this);
        iv_merge_delete.setVisibility(View.INVISIBLE);
        tv_merge_title.setText("系统管理");


    }
}
