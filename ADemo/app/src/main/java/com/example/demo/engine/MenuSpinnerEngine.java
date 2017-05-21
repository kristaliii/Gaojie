package com.example.demo.engine;

import java.util.ArrayList;

import com.example.demo.R;
import com.example.demo.activity.SecondActivity;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MenuSpinnerEngine {
	/**
	 * 点击ActionBar栏上的3个点，显示spinner
	 */
	private static ArrayList<String> list = new ArrayList<String>();
	private static Spinner spinner_menu;
	private static ArrayAdapter<String> adapter;
	public static  void spinnerMenu(MenuItem item,final Context context) {
			View view = View.inflate(context, R.layout.menuspinner, null);
			list.add("北京");    
			list.add("上海");    
			list.add("深圳");
			spinner_menu = (Spinner)view.findViewById(R.id.spinner_menu);    // 1
			adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, list);// 2
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 3
			
			spinner_menu.setAdapter(adapter); // 4
			
			item.setActionView(view);
			spinner_menu.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					switch (position) {
					case 0:
						Intent intent0 = new Intent(context, SecondActivity.class);
						context.startActivity(intent0);
						break;
	
					default:
						break;
					}
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		}
}

