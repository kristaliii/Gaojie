package com.example.demo.activity2;

import com.example.demo.R;
import com.example.demo.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MainActivity2 extends Activity{

	private EditText editText1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		getActionBar().setDisplayHomeAsUpEnabled(true); // 显示actionbar的返回键
		editText1 = (EditText) findViewById(R.id.editText1);
		setEditetext();
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.bt_main2_connected:
			Intent intent = new Intent(this, ConnectedActivity.class);
			startActivity(intent);
			break;
			
		case R.id.bt_main2_fileManger:
			Intent intent2 = new Intent(this, FileMangerActivity.class);
			startActivity(intent2);
			break;
			
		case R.id.bt_main2_file:
			Intent intent3 = new Intent(this, SdcardfileActivity.class);
			startActivity(intent3);
			break;
			
		default:
			break;
		}
	}
	
	private void setEditetext(){
		editText1.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 7) {
					ToastUtil.showToast(getApplicationContext(), "000");
					return false;
				}
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					ToastUtil.showToast(getApplicationContext(), "登陆");
					return false;
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 0) {
			ToastUtil.showToast(getApplicationContext(), "扫描键");
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
