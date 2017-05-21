package com.example.demo.engine;

import com.example.demo.R;
import com.example.demo.activity.SecondActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MenuDialogEngine {
/**
 * 点击ActionBar栏上的3个点，显示Dialog
 */
	private static String items[]={"SecondActivity","上海","深圳","重庆","天津"};
	public static void menuDialog(final Context context) {
		final RadioGroup view = (RadioGroup) View.inflate(context,R.layout.list_item_2, null);
		for (int i = 0; i < items.length; i++) {
			RadioButton btn = new RadioButton(context);
			btn.setId(i);
			btn.setText(items[i]);
			view.addView(btn);
		}
		new AlertDialog.Builder(context).setTitle("请选则").setView(view)// 添加view对象
				.setPositiveButton("进入", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int btnId =view.getCheckedRadioButtonId(); // 得到RadioButton对应的 id 值
						if (btnId == -1){
							return;
						}
						RadioButton btn = (RadioButton) view.findViewById(btnId); // 根据 id 值找到id
						switch (btn.getId()) {
						case 0:
							Intent intent0 = new Intent(context, SecondActivity.class);
							context.startActivity(intent0);
							break;

						case 1:
							Toast.makeText(context, btn.getText(), Toast.LENGTH_SHORT).show();
							break;
						}
					}
				}).create().show();
	}
}
