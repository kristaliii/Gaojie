package com.example.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.activity.BitmapActivity;
import com.example.demo.activity.VPActivity;
import com.example.demo.activity2.BroadcastActivity;
import com.example.demo.activity2.MainActivity2;
import com.example.demo.activity2.SNActivity;
import com.example.demo.activity2.ScanActivity;
import com.example.demo.activity2.StudentActivity;
import com.example.demo.activity2.XmlpullParserctivity;
import com.example.demo.activity3.LsvActivity;
import com.example.demo.engine.PopupMenuEngine;
import com.example.demo.utils.CToast;
import com.example.demo.utils.EnvironmentUtils;
import com.example.demo.utils.LogUtil;
import com.example.demo.utils.NoticeDialogUtil;
import com.example.demo.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

	private static String ACTION ="com.android.server.scannerservice.hayao.broadcast";
	private Button bt_mainactivity_touch, btSdcard;
	private EditText et_main_danjuhao;
	private EditText et_test;
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String extra = intent.getStringExtra("scannerdata");
			et_test.setText(extra);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_main);
		EnvironmentUtils.create360Apk();
		showToast();
		bt_mainactivity_touch = (Button)findViewById(R.id.bt_mainactivity_touch);
		et_main_danjuhao = (EditText)findViewById(R.id.et_main_danjuhao);
		et_test = (EditText)findViewById(R.id.et_test);
		btSdcard = (Button)findViewById(R.id.btSdcard);

		IntentFilter filter = new IntentFilter(ACTION);
		registerReceiver(receiver, filter);
		bt_mainactivity_touch.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					ToastUtil.showToast(getApplicationContext(), "按下");
					break;

				case MotionEvent.ACTION_UP:
					ToastUtil.showToast(getApplicationContext(), "抬起");
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 各种Button的点击事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_main_dialog:
			NoticeDialogUtil.showNoticeDialog(MainActivity.this);
			break;
			
		case R.id.bt_main_vp:
			Intent intent = new Intent(this, VPActivity.class);
			startActivity(intent);
			break;
			
		case R.id.bitmap:
			Intent intent2 = new Intent(this, BitmapActivity.class);
			startActivity(intent2);
			break;
			
		case R.id.bt_mainactivity_sn:
			Intent intent3 = new Intent(this, SNActivity.class);
			startActivity(intent3);
			break;
			
		case R.id.bt_mainactivity_scan:
			Intent intent4 = new Intent(this, ScanActivity.class);
			startActivity(intent4);
			break;
			
		case R.id.bt_mainactivity_service:
			Intent intent5 = new Intent(this, com.example.demo.androidzhishi.MainActivity.class);
			startActivity(intent5);
			break;
			
		case R.id.bt_mainactivity_lsv:
			Intent intent6 = new Intent(this, LsvActivity.class);
			startActivity(intent6);
			
		case R.id.bt_main_time:
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
			String string = format.format(date);
			LogUtil.e("date:" + date);
			LogUtil.e("string:" + string);
			et_main_danjuhao.setText(string);
			et_main_danjuhao.setTextSize(10);
			ToastUtil.showToast(getApplicationContext(), "data:" + date);
			break;
			
		case R.id.bt_main_xmlpullparser:
			Intent intent7 = new Intent(this, XmlpullParserctivity.class);
			startActivity(intent7);
			break;
			
		case R.id.bt_main_stu:
			Intent intent8 = new Intent(this, StudentActivity.class);
			startActivity(intent8);
			break;
			
		case R.id.bt_main_second:
			Intent intent9 = new Intent(this, MainActivity2.class);
			startActivity(intent9);
			break;
			
		case R.id.bt_mainactivity_broadcast:
			Intent intent10 = new Intent(this, BroadcastActivity.class);
			startActivity(intent10);
			break;
		}
	}
	
	/**
	 * 自定义Toast点击事件
	 */
	public void clickButton(View v) {
		customToast();
	}

	public void doClick(View v){
		showToast();
	}

	/**
	 * 自定义Toast
	 */
	private void customToast() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("This is a custom toast");

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();

	}

	/**
	 * 自定义Toast
	 */
	private CToast customToast;
	private void showToast(){
		if (customToast != null) {  
            customToast.hide();  
        }  
		
        customToast = CToast.makeText(getApplicationContext(), "我是不倒吐司", 10000);  
        customToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        customToast.show();
        
	}
	
	/**
	 * 绑定PPT键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 184) {
			ToastUtil.showToast(getApplicationContext(), "PPT");
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 选项菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;// true:表示显示菜单，false：表示不显示菜单
	}

	/**
	 * 点击菜单项的点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			// SpinnerEngine.spinnerMenu(item, MainActivity.this); //
			// 点击3个点，显示spinner
			// MenuDialogEngine.menuDialog(MainActivity.this); // 点击3个点，显示dialog
			PopupMenuEngine.popuMenu(MainActivity.this); // 点击3个点，显示弹出式菜单popupMenu
			break;

		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	/***
	 * 获取Android机器内置存储器根目录
	 */
	int i = 0;
	public void getSdcard(View v){
		String path = Environment.getExternalStorageDirectory().getPath();
		i = i % 2;
		if (i == 0) {
			btSdcard.setText(path);
			i = i + 1;
		}else {
			btSdcard.setText("sdcard目录");
			i = i + 1;
		}
		LogUtil.i("I:"+i);
	}
	
	/**
	 * 再点一次则退出
	 */
	private boolean isExit;// false
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
