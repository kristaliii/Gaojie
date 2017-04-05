package com.seuic.gaojie.utils.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.seuic.gaojie.R;
import com.seuic.gaojie.bean.Barcode;

import java.util.ArrayList;

public class DialogUtils {
	/**
	 * 提示对话框，一个 确认 按钮
	 */
	private  static AlertDialog hintDialog;
	public static void hintOneDialog(final Context context, String text){
		Builder build = new Builder(context);
		View inflate = View.inflate(context, R.layout.dialog_white, null);
		TextView tv_dialog_title = (TextView) inflate.findViewById(R.id.tv_dialog_title);
		TextView tv_dialog_message = (TextView) inflate.findViewById(R.id.tv_dialog_message);
		Button btn_ok = (Button) inflate.findViewById(R.id.btn_ok);
		tv_dialog_title.setText("提示");
		if (!text.equals("放行")) {
			tv_dialog_message.setTextColor(Color.RED);
			tv_dialog_message.setText(text);
		}
		tv_dialog_message.setText(text);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hintDialog.dismiss();
			}
		});
		build.setView(inflate);
		if (hintDialog != null) {
			hintDialog.dismiss();
		}
		hintDialog = build.create();
		hintDialog.show();
	}

	public static void disMissHintOne() {
		if (hintDialog != null) {
			hintDialog.dismiss();
		}
	}
	
	/**
	 * 提示对话框 两个按钮
	 */
	private static Dialog dialog;
	public static  void hintTwoDialog(final Context context,String message) {
		Builder build = new Builder(context);
		build.setCancelable(true);
		View view = View.inflate(context, R.layout.dialog_whitess, null);
		TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
		TextView tv_dialog_message = (TextView) view.findViewById(R.id.tv_dialog_message);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		tv_dialog_title.setText("提示");
		tv_dialog_message.setText(message);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				((Activity) context).finish();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		build.setView(view);
		dialog = build.create();
		dialog.show();
	}

	/**
	 * 提示对话框，一个 确认 按钮 显示listview
	 */ 
	private static AlertDialog hintDialogList;
	public static  void hintDialogList(final Context context, BaseAdapter adapter,ArrayList<Barcode> listNoMatch){
		Builder build = new Builder(context);
		View inflate = View.inflate(context, R.layout.dialog_whitelist, null);
		TextView tv_dialog_title = (TextView) inflate.findViewById(R.id.tv_dialog_title);
		ListView lv_dialog_message = (ListView) inflate.findViewById(R.id.lv_dialog_message);
		Button btn_ok = (Button) inflate.findViewById(R.id.btn_ok);
		tv_dialog_title.setText("未提交个数:"+listNoMatch.size());
		lv_dialog_message.setAdapter(adapter);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hintDialogList.dismiss();
			}
		});
		build.setView(inflate);
		hintDialogList = build.create();
		hintDialogList.show();
	}
	
}
