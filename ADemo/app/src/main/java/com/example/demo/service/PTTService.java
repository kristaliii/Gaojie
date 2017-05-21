package com.example.demo.service;

import com.example.demo.R;
import com.example.demo.androidzhishi.MainActivity;
import com.example.demo.broadcastreciever.PTTReciever;
import com.example.demo.utils.LogUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PTTService extends Service {

	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		NotificationManager nmManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.check2); // 必须设有小图标
		builder.setContentTitle("标题");
		builder.setContentText("内容");
		builder.setContentIntent(pIntent);
		builder.setAutoCancel(true); // 点击通知后会在通知栏消失
		Notification notification = builder.build();
		nmManager.notify(1, notification);
		LogUtil.d(PTTService.class, "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Intent intent2 = new Intent(PTTService.this, PTTReciever.class);
		sendBroadcast(intent2);
		LogUtil.d(PTTService.class, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
