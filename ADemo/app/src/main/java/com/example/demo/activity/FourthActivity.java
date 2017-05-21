package com.example.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.demo.R;
import com.example.demo.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author ieasy360_1
 * 
 */
public class FourthActivity extends Activity implements OnClickListener {

	private ListView listview;
	private Context context;
	private List<String> array = new ArrayList<String>();
	private List<String> selectid = new ArrayList<String>();
	private boolean isMulChoice = false; // 是否多选
	private Adapter adapter;
	private RelativeLayout layout;
	private Button cancle, delete;
	private TextView txtcount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_fourth);
		context = FourthActivity.this;
		listview = (ListView) findViewById(R.id.list);
		layout = (RelativeLayout) findViewById(R.id.relative);
		txtcount = (TextView) findViewById(R.id.txtcount);
		cancle = (Button) findViewById(R.id.cancle);
		delete = (Button) findViewById(R.id.delete);
		cancle.setOnClickListener(this);
		delete.setOnClickListener(this);
		init();
		adapter = new Adapter(context, txtcount);
		listview.setAdapter(adapter);
	}

	public void init() {
		for (int i = 0; i < 20; i++) {
			array.add("小明" + i);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle: // 隐藏底部按钮项
			isMulChoice = false;
			selectid.clear();
			adapter = new Adapter(context, txtcount);
			listview.setAdapter(adapter);
			layout.setVisibility(View.INVISIBLE);
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
			adapter = new Adapter(context, txtcount);
			listview.setAdapter(adapter);
			layout.setVisibility(View.INVISIBLE);
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
		private Context context;
		private LayoutInflater inflater = null;
		private HashMap<Integer, View> mView;
		public HashMap<Integer, Integer> visiblecheck;// 用来记录是否显示checkBox
		public HashMap<Integer, Boolean> ischeck;
		private TextView txtcount;

		@SuppressLint("UseSparseArrays")
		public Adapter(Context context, TextView txtcount) {
			this.context = context;
			this.txtcount = txtcount;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = new HashMap<Integer, View>();
			visiblecheck = new HashMap<Integer, Integer>();
			ischeck = new HashMap<Integer, Boolean>();
			if (isMulChoice) {
				for (int i = 0; i < array.size(); i++) {
					ischeck.put(i, false);
					visiblecheck.put(i, CheckBox.VISIBLE);
				}
			} else {
				for (int i = 0; i < array.size(); i++) {
					ischeck.put(i, false);
					visiblecheck.put(i, CheckBox.INVISIBLE);
				}
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = mView.get(position);
			if (view == null) {
				view = inflater.inflate(R.layout.item_listview_forth, null);
				TextView txt = (TextView) view.findViewById(R.id.txtName);
				final CheckBox ceb = (CheckBox) view.findViewById(R.id.check);

				txt.setText(array.get(position));

				ceb.setChecked(ischeck.get(position));
				ceb.setVisibility(visiblecheck.get(position));

				view.setOnLongClickListener(new Onlongclick());

				view.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (isMulChoice) {
							if (ceb.isChecked()) {
								ceb.setChecked(false);
								selectid.remove(array.get(position));
							} else {
								ceb.setChecked(true);
								selectid.add(array.get(position));
							}
							txtcount.setText("共选择了" + selectid.size() + "项");
						} else {
							ToastUtil.showToast(context, "点击了" + array.get(position));
						}
					}
				});
				mView.put(position, view);
			}
			return view;
		}

		class Onlongclick implements OnLongClickListener {
			public boolean onLongClick(View v) {
				isMulChoice = true;
				selectid.clear();
				layout.setVisibility(View.VISIBLE);
				for (int i = 0; i < array.size(); i++) {
					adapter.visiblecheck.put(i, CheckBox.VISIBLE);
				}
				adapter = new Adapter(context, txtcount);
				listview.setAdapter(adapter);
				return true;
			}
		}
	}
}