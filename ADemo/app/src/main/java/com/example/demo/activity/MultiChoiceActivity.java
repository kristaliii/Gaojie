package com.example.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.demo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MultiChoiceActivity extends Activity implements OnClickListener {

	private ListView listview;
	private List<String> array = new ArrayList<String>();
	private List<String> selectid = new ArrayList<String>();
	private boolean isMulChoice = false; // 是否多选
	private Adapter adapter;
	private Button cancle, delete;
	private TextView txtcount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_multi);
		listview = (ListView) findViewById(R.id.list);
		txtcount = (TextView) findViewById(R.id.txtcount);
		cancle = (Button) findViewById(R.id.cancle);
		delete = (Button) findViewById(R.id.delete);
		cancle.setOnClickListener(this);
		delete.setOnClickListener(this);
		init();
		adapter = new Adapter(MultiChoiceActivity.this, txtcount);
		listview.setAdapter(adapter);
	}

	public void init() {
		for (int i = 0; i < 25; i++) {
			array.add("小明" + i);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle: // 撤销勾选项
			isMulChoice = false;
			selectid.clear();
			adapter = new Adapter(MultiChoiceActivity.this, txtcount);
			listview.setAdapter(adapter);
			break;
			
		case R.id.delete: // 删除勾选的项
			isMulChoice = false;
			for (int i = 0; i < selectid.size(); i++) {
				for (int j = 0; j < array.size(); j++) {
					if (selectid.get(i).equals(array.get(j))) {
						array.remove(j);
					}
				}
			}
			selectid.clear();
			adapter = new Adapter(MultiChoiceActivity.this, txtcount);
			listview.setAdapter(adapter);
//			layout.setVisibility(View.INVISIBLE);
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * @author ieasy360_1 自定义Adapter
	 */
	class Adapter extends BaseAdapter {
		private LayoutInflater inflater = null;
		public HashMap<Integer, Boolean> ischeck; // 存放 CheckBox 的状态
		private TextView txtcount;

		@SuppressLint("UseSparseArrays")
		public Adapter(Context context, TextView txtcount) {
			this.txtcount = txtcount;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ischeck = new HashMap<Integer, Boolean>();
			for (int i = 0; i < array.size(); i++) {
				ischeck.put(i, false); // 初始化的时候checkbox的状态
			}
		}

		@Override
		public int getCount() {
			return array.size();
		}

		@Override
		public Object getItem(int position) {
			return array.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, // convertView:LinearLayout (一开始为null)
				ViewGroup parent) {// listView
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_listview_forth, null); // convertView:LinearLayout的对象
				TextView txt = (TextView) convertView.findViewById(R.id.txtName);
				final CheckBox ceb = (CheckBox) convertView.findViewById(R.id.check);

				txt.setText(array.get(position));
				ceb.setChecked(ischeck.get(position));
				convertView.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (!isMulChoice) {
							if (ceb.isChecked()) {
								ceb.setChecked(false);
								selectid.remove(array.get(position));
							} else {
								ceb.setChecked(true);
								selectid.add(array.get(position)); // “小明+i”
							}
							txtcount.setText("共选择了" + selectid.size() + "项");
						}
					}
				});
			}else {
				
			}
			return convertView;
		}
	}
	
	static class ViewHolder{
		TextView txt;
		CheckBox cbx;
	}
}