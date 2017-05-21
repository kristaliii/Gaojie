package com.example.demo.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.R;
import com.example.demo.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ThirdActivity extends Activity {
	Button show;
	Button select;
	Button deselect;
	ListView lv;
	Context mContext;
	MyListAdapter adapter;

	List<Integer> selected = new ArrayList<Integer>(); // 要勾选的项
	private List<Item> items; // listView 的每一项数据

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_third);
		mContext = ThirdActivity.this;
		select = (Button) findViewById(R.id.select);
		deselect = (Button) findViewById(R.id.deselect);
		show = (Button) findViewById(R.id.show);
		lv = (ListView) findViewById(R.id.lv);

		items = new ArrayList<Item>();

		for (int i = 0; i < 20; i++) {
			Item item = new Item();
			item.name = "hyh" + i;
			item.address = "bj";
			item.checked = false;
			items.add(item);
		}

		adapter = new MyListAdapter(items);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { // 点击每一个 item 改变 checkbox 的状态
				items.get(position).checked = !items.get(position).checked;
				adapter.notifyDataSetChanged();
				// ToastUtil.showToast(mContext, "单击:"+items.get(position).name
				// +",id:"+id);
			}
		});

		select.setOnClickListener(new View.OnClickListener() { // 全选操作

			@Override
			public void onClick(View v) {
				for (int i = 0; i < items.size(); i++) {
					items.get(i).checked = true;
				}
				adapter.notifyDataSetChanged();
			}
		});

		deselect.setOnClickListener(new View.OnClickListener() { // 反选操作

			@Override
			public void onClick(View v) {
				for (int i = 0; i < items.size(); i++) {
					items.get(i).checked = false;
				}
				adapter.notifyDataSetChanged();
			}
		});

		show.setOnClickListener(new View.OnClickListener() { // 勾选已经删除的选项

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						for (int position = 0; position < items.size(); position++) {
							if (items.get(position).checked) {
								items.remove(position);
							}
						}
						Message msg = Message.obtain();
						msg.obj = items;
						Handler handler = new Handler(getMainLooper()){
							public void handleMessage(android.os.Message msg) {
								@SuppressWarnings("unchecked")
								ArrayList<Item> item = (ArrayList<Item>)msg.obj;
								adapter = new MyListAdapter(item);
								lv.setAdapter(adapter);
							}
						};
						handler.sendMessage(msg);
					}
				}.start();
			}
		});
	}

	// 自定义ListView适配器
	class MyListAdapter extends BaseAdapter {

		// private Context context;
		LayoutInflater inflater;
		public List<Item> items;

		public MyListAdapter(List<Item> items) {
			this.items = items;

			inflater = LayoutInflater.from(mContext);

		}

		@Override
		public int getCount() {
			// 返回值控制该Adapter将会显示多少个列表项
			return items == null ? 0 : items.size();
		}

		@Override
		public Object getItem(int position) {
			// 返回值决定第position处的列表项的内容
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// 返回值决定第position处的列表项的ID
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			Item item = items.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_listview_third,null);
				holder = new ViewHolder();
				holder.btnDel = (Button) convertView.findViewById(R.id.btnDel);
				holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cbCheck);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
				holder.tvName.setText(item.name);
				holder.tvAddress.setText(item.address);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.cbCheck.setChecked(item.checked);
				holder.tvName.setText(item.name);
				holder.tvAddress.setText(item.address);

			}
			holder.btnDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (items.get(position).checked) {
						// 删除list中的数据
						items.remove(position);
						// 通知列表数据修改
						adapter.notifyDataSetChanged();
					} else {
						ToastUtil.showToast(mContext, "请勾选");
					}
				}

			});
//			holder.cbCheck.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					CheckBox cb = (CheckBox) v;
//					items.get(position).checked = cb.isChecked();
//					ToastUtil.showToast(mContext, "狗子哥");
//				}
//			});
			return convertView;
		}
	}

	static class ViewHolder {
		public CheckBox cbCheck;
		public TextView tvName;
		public TextView tvAddress;
		public Button btnDel;

	}

	class Item {
		private String name;
		private String address;
		private Boolean checked;
	}

	/**
	 * 选项菜单上的单个菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
