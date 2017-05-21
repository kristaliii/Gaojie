package com.seuic.yapei;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 2017/4/13.
 */

public class ActivityData extends AppCompatActivity {
    @BindView(R.id.tv_merge_title)TextView tv_merge_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);
        tv_merge_title.setText("数据管理");

    }
}
