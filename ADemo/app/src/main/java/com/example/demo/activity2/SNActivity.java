package com.example.demo.activity2;

import java.lang.reflect.Method;

import com.example.demo.R;
import com.example.demo.utils.LogUtil;
import com.seuic.misc.Misc;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.EditText;

public class SNActivity extends Activity {

	private EditText et_snactivity_sn;
	private EditText et_snactivity_imei;
	private EditText et_snactivity_seria;
	private EditText textView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sn);
		initView();
		
		// 设备序列号(SN号)
		Misc misc = new Misc();
		String sn = misc.getSN();
		et_snactivity_sn.setText(" 设备序列号SN："+sn);
		
		// 移动通信国际识别码 IMEI
		String imei = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		et_snactivity_imei.setText(" iemi："+imei);
		
		String android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		LogUtil.e(android_id);
		
		// 序列号
		String serialNumber = getSerialNumber();
		et_snactivity_seria.setText(" 序列号："+serialNumber);
		
		textView.setText("Product Model: " + android.os.Build.MODEL + "\n"
                + android.os.Build.VERSION.SDK + "\n"
                + android.os.Build.VERSION.RELEASE +"\n" // Android4.3后面的4.3
                + android.os.Build.MANUFACTURER+"\n" // AUTOID
                + android.os.Build.BOARD + "\n"  // CPU型号
                + android.os.Build.BRAND + "\n" // qcom高通处理器
                + android.os.Build.BOOTLOADER + "\n" 
                + android.os.Build.DEVICE + "\n" // cpu型号
                + android.os.Build.ID + "\n"  // 版本号
                + android.os.Build.HOST + "\n" 
                + android.os.Build.SERIAL + "\n" // 序列号
				);
		
	}
	
	private void initView(){
		et_snactivity_sn = (EditText) findViewById(R.id.et_snactivity_sn);
		et_snactivity_imei = (EditText) findViewById(R.id.et_snactivity_imei);
		et_snactivity_seria = (EditText) findViewById(R.id.et_snactivity_seria);
		textView = (EditText) findViewById(R.id.etTextView);
	}
	
	/**
	 * 获取Android机器的序列号
	 * @return
	 */
	public static String getSerialNumber(){
	    String serial = null;
	    try {
	    Class<?> c = Class.forName("android.os.SystemProperties");
	       Method get = c.getMethod("get", String.class);
	       serial = (String) get.invoke(c, "ro.serialno");
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	    return serial;
	}
}
