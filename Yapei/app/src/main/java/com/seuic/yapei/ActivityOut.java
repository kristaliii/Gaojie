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

public class ActivityOut extends AppCompatActivity {
    @BindView(R.id.iv_merge_delete)ImageView iv_merge_delete;
    @BindView(R.id.iv_merge_back)ImageView iv_merge_back;
    @BindView(R.id.tv_merge_title)TextView tv_merge_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        ButterKnife.bind(this);
        iv_merge_delete.setVisibility(View.INVISIBLE);
        tv_merge_title.setText("出库管理");

    }

    @OnClick({R.id.bt_out_sell,R.id.bt_out_other})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.bt_out_sell:
                Intent intent = new Intent(this, ActivityOutSell.class);
                startActivity(intent);
                break;

            case R.id.bt_out_other:
                Intent intent1 = new Intent(this, ActivityOutOther.class);
                startActivity(intent1);
                break;
        }
    }
}
