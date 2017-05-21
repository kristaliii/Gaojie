package com.seuic.yapei;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/4/13.
 */

public class ActivityOutOther extends AppCompatActivity {
    @BindView(R.id.iv_merge_delete)ImageView iv_merge_delete;
    @BindView(R.id.sp_outother_name)Spinner sp_outother_name;
    @BindView(R.id.tv_merge_title)TextView tv_merge_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_other);
        ButterKnife.bind(this);
        iv_merge_delete.setVisibility(View.INVISIBLE);
        tv_merge_title.setText("其他出库");

        ArrayList<String> list = new ArrayList<String>();
        list.add("退货出库");
        list.add("调拨出库");
        list.add("借用出库");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_outother_name.setAdapter(adapter);
        sp_outother_name.setSelection(0);

    }

    @OnClick(R.id.tv_out_other_test)
    void onButtonClick(View view) {
        Intent intent = new Intent(this, ActivityScan.class);
        startActivity(intent);
    }
}
