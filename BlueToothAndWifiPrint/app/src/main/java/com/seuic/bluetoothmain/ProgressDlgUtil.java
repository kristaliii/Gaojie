package com.seuic.bluetoothmain;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

public class ProgressDlgUtil {
	private ProgressDlgUtil() {}
	private static ProgressDialog mProgressDlg;

	public static ProgressDialog getInstance() {
		if (mProgressDlg !=null)
			return mProgressDlg;
		return null;
	}
	public  static void showProgress(String info, Context context) {

		if (info == null)
			info = "请稍等...";
		if(context == null){
			return ;
		}
		mProgressDlg = ProgressDialog.show(context, null, info, true, true);
		mProgressDlg.setCancelable(false);
		
		mProgressDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				mProgressDlg = null;
			}
		});
		
		mProgressDlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mProgressDlg = null;
			}
		});
		
//		setBack(mProgressDlg);
		
	}
	public static void dismissProgress() {
		if (mProgressDlg != null) {
			mProgressDlg.dismiss();
			mProgressDlg = null;
		}
	}

	public static void setBack() {
		if (mProgressDlg != null) {
			mProgressDlg.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						mProgressDlg.dismiss();
						setNull();
						doSth();
					}
					return false;
				}
			});
		}
	}

	private static void setNull() {
		mProgressDlg = null;
	}

    public static void doSth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }
}
