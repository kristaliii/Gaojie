package com.example.demo.activity;


import com.example.demo.R;
import com.example.demo.adapter.MyAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class SecondActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_second);
		ListView second_lv = (ListView) findViewById(R.id.second_lv);
		second_lv.setAdapter(new MyAdapter(SecondActivity.this));
		
	}
	
	/**
	 * 选项菜单上的单个菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
}
