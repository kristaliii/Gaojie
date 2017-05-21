package com.example.demo.activity2;


import com.example.demo.R;
import com.example.demo.utils.LogUtil;
import com.example.demo.utils.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class ConnectedActivity extends Activity {

	// 没有网络
	private static final int TYPE_NET_WORK_DISABLED = 0;
	// WIFI 网络
	private static final int TYPE_WIFI = 1;
	private static final int TYPE_CT_WAP = 5;
	private static final int TYPE_CT_WAP_2G = 6;
	private static final int TYPE_CT_NET = 7;
	private static final int TYPE_CT_NET_2G = 8;
	private static final int TYPE_CM_WAP = 9;
	private static final int TYPE_CM_WAP_2G = 10;
	private static final int TYPE_CM_NET = 11;
	private static final int TYPE_CM_NET_2G = 12;
	private static final int TYPE_CU_NET = 13;
	private static final int TYPE_CU_NET_2G = 14;
	private static final int TYPE_CU_WAP = 15;
	private static final int TYPE_CU_WAP_2G = 16;
	private static final int TYPE_OTHER = 17;
	
	private static Uri uri = Uri.parse("content://telephony/carriers/preferapn");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);
		long start = System.currentTimeMillis();
		int type = checkNetworkType(this);
		LogUtil.e(""+(System.currentTimeMillis() - start));
		switch (type) {
		case TYPE_WIFI:
			ToastUtil.showToast(getApplicationContext(), "wifi");
			break;

		case TYPE_NET_WORK_DISABLED:
			ToastUtil.showToast(getApplicationContext(), "no network");
			break;
		
		case TYPE_CT_WAP:
			ToastUtil.showToast(getApplicationContext(), "ctwap");
			break;
			
		case TYPE_CT_WAP_2G:
			ToastUtil.showToast(getApplicationContext(), "ctwap_2g");
			break;
			
		case TYPE_CT_NET:
			ToastUtil.showToast(getApplicationContext(), "ctnet");
			break;
			
		case TYPE_CT_NET_2G:
			ToastUtil.showToast(getApplicationContext(), "ctnet_2g");
			break;
			
		case TYPE_CM_WAP:
			ToastUtil.showToast(getApplicationContext(), "cmwap");
			break;
			
		case TYPE_CM_WAP_2G:
			ToastUtil.showToast(getApplicationContext(), "cmwap_2g");
			break;
			
		case TYPE_CM_NET:
			ToastUtil.showToast(getApplicationContext(), "cmnet");
			break;
			
		case TYPE_CM_NET_2G:
			ToastUtil.showToast(getApplicationContext(), "cmnet_2g");
			break;
			
        case TYPE_CU_NET:  
        	ToastUtil.showToast(getApplicationContext(), "cunet");
            break;  
            
        case TYPE_CU_NET_2G:  
        	ToastUtil.showToast(getApplicationContext(), "cunet_2g");
            break;  
            
        case TYPE_CU_WAP:  
        	ToastUtil.showToast(getApplicationContext(), "cuwap");
            break;  
            
        case TYPE_CU_WAP_2G:  
        	ToastUtil.showToast(getApplicationContext(), "cuwap_2g");
            break;  
			
		case TYPE_OTHER:
			ToastUtil.showToast(getApplicationContext(), "other");
			break;
			
		case -1:
			ToastUtil.showToast(getApplicationContext(), "mobile");
			break;
		}
	}
	
	private static int checkNetworkType(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			return TYPE_NET_WORK_DISABLED;
		}else {
			int netType = networkInfo.getType();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				// wifi net 处理
				return TYPE_WIFI;
			}else if (netType == ConnectivityManager.TYPE_MOBILE) {
				// 判断是否电信wap,电信机器wap接入点比移动联通wap接入点多设置一个用户名和密码
				return -1;
				
//				boolean is3G = isFastMobileNetwork(context);
//				final Cursor c =context.getContentResolver().query(uri, null, null, null, null);
//				if (c != null) {
//					c.moveToFirst();
//					String user = c.getString(c.getColumnIndex("user"));
//					if (!TextUtils.isEmpty(user)) {
//						if (user.startsWith("ctwap")) {
//							return is3G ? TYPE_CT_WAP : TYPE_CT_WAP_2G;
//						}else if (user.startsWith("ctnet")) {
//							return is3G ? TYPE_CT_NET : TYPE_CT_NET_2G;
//						}
//					}
//				}
//				c.close();
//				// 判断是否移动联通wap;
//				String netMode = networkInfo.getExtraInfo();
//				if (netMode != null) {
//					// 通过apn名称判断是否是联通和移动wap
//					netMode = netMode.toLowerCase(Locale.CHINA);
//					if (netMode.equals("cmwap")) {
//						return is3G ? TYPE_CM_WAP : TYPE_CM_WAP_2G;
//					}else if (netMode.equals("cmnet")) {
//						return is3G ? TYPE_CM_NET : TYPE_CM_NET_2G;
//					}else if (netMode.equals("3gnet") || netMode.equals("uninet")) { 
//						return is3G ? TYPE_CU_NET : TYPE_CU_NET_2G;
//					}else if (netMode.equals("3gwap") || netMode.equals("uniwap")) {
//						return is3G ? TYPE_CU_WAP : TYPE_CU_WAP_2G;
//					}
//				}
				
			}
		}
		return TYPE_OTHER;
	}
	
	private static boolean isFastMobileNetwork(Context context){
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (manager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // 50~100kbps

		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // 14-64kbps
			
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // 50-100kbps
			
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return false; // 400-1000kbps
			
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return false; // 600=1400kbps
			
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // 100kpbs
			
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return false; // 2-14Mbps
			
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return false ; // 700-1700kbps
			
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return false; // 1-23Mbps
			
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return false; // 400-7000kbps
			
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return false; // 1-2Mbps
			
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return false; // 5Mbps
			
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return false; // 10-20Mbps
			
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; //25kbps
			
		case TelephonyManager.NETWORK_TYPE_LTE:
			return false ; // 10+Mbps
			
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		}
		return false;
	}
}
