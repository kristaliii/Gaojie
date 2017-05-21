package com.example.demo.activity3;

import com.example.demo.R;
import com.example.demo.adapter.MyListViewAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class LsvActivity extends Activity {

	private ListView lsv_main3_test;
	private MyListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3_lsv);
		lsv_main3_test = (ListView) findViewById(R.id.lsv_main3_test);
		
		String[] objects = {"下面","上面","中间","左边","右边"};
		adapter = new MyListViewAdapter(this, objects);
		lsv_main3_test.setAdapter(adapter);
		
	    //点击监听
		lsv_main3_test.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.changeSelected(position);//刷新
			}
		});
		
		//选中监听
		lsv_main3_test.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.changeSelected(position);//刷新
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
}
