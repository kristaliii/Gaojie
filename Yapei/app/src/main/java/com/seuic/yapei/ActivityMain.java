package com.seuic.yapei;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/4/13.
 */

public class ActivityMain extends AppCompatActivity {
    @BindView(R.id.iv_merge_back)ImageView iv_merge_back;
    @BindView(R.id.iv_merge_delete)ImageView iv_merge_delete;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        iv_merge_back.setVisibility(View.INVISIBLE);
        iv_merge_delete.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.ll_main_system,R.id.ll_main_data,R.id.ll_main_in,R.id.ll_main_out,R.id.ll_main_check})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_system:
                Intent intent = new Intent(this, ActivitySystem.class);
                startActivity(intent);
                break;

            case R.id.ll_main_data:
                Intent intent1 = new Intent(this, ActivityData.class);
                startActivity(intent1);
                break;
            case R.id.ll_main_in:
                Intent intent2 = new Intent(this, ActivityIn.class);
                startActivity(intent2);
                break;
            case R.id.ll_main_out:
                Intent intent3 = new Intent(this, ActivityOut.class);
                startActivity(intent3);
                break;
            case R.id.ll_main_check:
                Intent intent4 = new Intent(this, ActivityCheck.class);
                startActivity(intent4);
                break;
        }
    }
}
