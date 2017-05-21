package com.example.demo.androidzhishi;

import com.example.demo.R;
import com.example.demo.service.MyService;
import com.example.demo.service.MyService.MyBinder;
import com.example.demo.utils.LogUtil;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_androidmain);
	}
	
	private MyAIDLService aidlService;
	private ServiceConnection conn = new ServiceConnection() {
		
//		private MyBinder myBinder;

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
//			myBinder = (MyBinder) service;
//			myBinder.startDownload();
			aidlService = MyAIDLService.Stub.asInterface(service);
			try {
				String upperCase = aidlService.toUpperCase("abcd");
				int plus = aidlService.plus(1, 2);
				LogUtil.e("upperCase:"+upperCase);
				LogUtil.e("plus:"+plus);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};
	
	public void onClick(View v){
		switch (v.getId()) { // 开启服务的第一种方式
		case R.id.bt_mainandroid_start:
			Intent intent = new Intent(this,MyService.class);
			startService(intent);
			break;

		case R.id.bt_mainandroid_stop:
			Intent intent2 = new Intent(this, MyService.class);
			stopService(intent2);
			break;
		
		case R.id.bt_mainandroid_bind: // 开启服务的第二种方式
			Intent intent3 = new Intent(this, MyService.class);
			bindService(intent3, conn , BIND_AUTO_CREATE);
			break;
		
		case R.id.bt_mainandroid_unbind:
			unbindService(conn);
			LogUtil.i("unbindService");
			break;
		}
	}
}
