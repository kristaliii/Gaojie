package com.example.demo.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.example.demo.R;
import com.example.demo.activity.ClockActivity;
import com.example.demo.activity.FifthActivity;
import com.example.demo.activity.FourthActivity;
import com.example.demo.activity.MultiChoiceActivity;
import com.example.demo.activity.SecondActivity;
import com.example.demo.activity.SoapActivityTest;
import com.example.demo.activity.ThirdActivity;

public class PopupMenuEngine {
/**
 * 点击ActionBar栏上的3个点，显示PopupMenu
 * @param context
 */
	public static  void popuMenu(final Context context) {
		
		PopupMenu popupMenu = new PopupMenu(context,((Activity) context).findViewById(R.id.action_settings));
		popupMenu.inflate(R.menu.popup_menu);
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.button_2:
					Intent intent2 = new Intent(context, SecondActivity.class);
					context.startActivity(intent2);
					break;
					
				case R.id.button_3:
					Intent intent3 = new Intent(context, ThirdActivity.class);
					context.startActivity(intent3);
					break;
					
				case R.id.button_4:
					Intent intent4 = new Intent(context, FourthActivity.class);
					context.startActivity(intent4);
					break;
					
				case R.id.button_multi:
					Intent intent = new Intent(context, MultiChoiceActivity.class);
					context.startActivity(intent);
					break;
					
				case R.id.button_5:
					Intent intent5 = new Intent(context, FifthActivity.class);
					context.startActivity(intent5);
					break;
					
				case R.id.button_1:
					System.exit(0);
					break;
					
				case R.id.clock:
					Intent intent6 = new Intent(context, ClockActivity.class);
					context.startActivity(intent6);
					break;
					
				case R.id.button_0:
					Intent intent7 = new Intent(context, SoapActivityTest.class);
					context.startActivity(intent7);
					break;
				}
				return false;
			}
		});
		popupMenu.show();
	}
	
}
