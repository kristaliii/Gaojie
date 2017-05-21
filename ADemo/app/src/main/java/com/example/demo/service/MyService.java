package com.example.demo.service;

import java.util.Locale;

import com.example.demo.R;
import com.example.demo.androidzhishi.MainActivity;
import com.example.demo.androidzhishi.MyAIDLService.Stub;
import com.example.demo.utils.LogUtil;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		NotificationManager nmManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.btn_game_tie_n); // 必须设有小图标
		builder.setContentTitle("标题");
		builder.setContentText("内容");
		builder.setContentIntent(pIntent);
		builder.setAutoCancel(true); // 点击通知后会在通知栏消失
		Notification notification = builder.build();
		nmManager.notify(1, notification);
		LogUtil.d("onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(){
			public void run() {
				LogUtil.d("onStartCommand");
			}
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i("onDestroy");
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return stub;
	}

	Stub stub = new Stub() {
		
		@Override
		public String toUpperCase(String str) throws RemoteException {
			if (str != null) {
				return str.toUpperCase(Locale.CHINA);
			}
			return null;
		}
		
		@Override
		public int plus(int a, int b) throws RemoteException {
			return a + b;
		}
	};
	
	public class MyBinder extends Binder{
		 public void startDownload(){
			 new Thread(){
				 public void run() {
					 LogUtil.d("开始下载");
				 }
			 }.start();
		}
	}
}
