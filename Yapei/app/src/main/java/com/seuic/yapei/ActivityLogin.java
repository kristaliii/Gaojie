package com.seuic.yapei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_login_login)
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login_login:
                Intent intent = new Intent(this, ActivityMain.class);
                startActivity(intent);

                break;
        }
    }
}
