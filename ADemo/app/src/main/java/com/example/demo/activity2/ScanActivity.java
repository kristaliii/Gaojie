package com.example.demo.activity2;

import com.example.demo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class ScanActivity extends Activity {

	private static String ACTION ="com.android.server.scannerservice.hayao.broadcast";
	private static String ACTION_SETTING ="com.android.scanner.service_settings";
	private static String KEY ="scannerdata";
	private Button bt_scan_scan;
	private TextView tv_scan_scan;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String extra = intent.getStringExtra(KEY);
			tv_scan_scan.setText(extra);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		
//		Intent intent2 = new Intent("com.android.scanner.ENABLED");
//		intent2.putExtra("enabled", false);
//		sendBroadcast(intent2);
		
		bt_scan_scan = (Button) findViewById(R.id.bt_scan_scan);
		tv_scan_scan = (TextView) findViewById(R.id.tv_scan_scan);
		
		IntentFilter filter = new IntentFilter(ACTION);
		registerReceiver(receiver, filter);
		
		// 用户可以在自己的程序中用广播设置扫描服务的功能
		Intent intent = new Intent(ACTION_SETTING);
		intent.putExtra("sound_play", true);
		sendBroadcast(intent);
		
		
		setCustomButtonKey();
		
	}

	/**
	 * 实现 Button 和实际物理键一样的扫描效果(是onTouch方法，不是onKey方法)
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void setCustomButtonKey() {
		bt_scan_scan.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Intent intent = new Intent("com.scan.onStartScan");
					sendBroadcast(intent);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Intent intent2 = new Intent("com.scan.onEndScan");
					sendBroadcast(intent2);
				}
				return false;
			}
		});
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.bt_scan_start: // 用户可以不使用扫描键通过广播来定义按钮扫描
			Intent intent = new Intent("com.scan.onStartScan");
			sendBroadcast(intent);
			break;
			
		case R.id.bt_scan_end: // 用户可以不使用扫描键通过广播来定义按钮停止扫描
			Intent intent2 = new Intent("com.scan.onEndScan");
			sendBroadcast(intent2);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
