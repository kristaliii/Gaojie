package com.seuic.gaojie.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Dell on 2017/3/13.
 */

public class BaseActivity extends AppCompatActivity {
    protected void onCreate( Bundle savedInstanceState,int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);
    }

    public void LogI(String text) {
        Log.i("tag", text);
    }

    public void LogD(String text) {
        Log.d("tag", text);
    }

    public void LogE(String text) {
        Log.e("tag", text);
    }
}
