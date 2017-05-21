package com.example.demo.adapter;

import java.util.ArrayList;

import com.example.demo.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * listview显示数据的apdter 
 * @author Administrator
 */
public class MyAdapter extends BaseAdapter {
	
	private ArrayList<String> list = new ArrayList<String>();
	private Context context;
	
	public MyAdapter(Context context) {
		this.context = context;
		list.add("1818818291");
		list.add("82234u922734");
		list.add("1hdhfaha3292u9829");
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private ViewHolder holder = null;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_listview, null);
			holder.ll_item_lsv = (LinearLayout) convertView.findViewById(R.id.ll_item_lsv);
			holder.tv_item_lsv = (TextView) convertView.findViewById(R.id.tv_item_lsv);
			holder.cb_item_lsv = (CheckBox) convertView.findViewById(R.id.cb_item_lsv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ll_item_lsv.setOnLongClickListener( new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(context, "v:"+position, Toast.LENGTH_SHORT).show();
				if (holder.cb_item_lsv.isChecked()) {
					list.remove(position);
					notifyDataSetChanged();
				}
				return false;
			}
		});
		holder.tv_item_lsv.setText(list.get(position));
		return convertView;
	}

	class ViewHolder {
		LinearLayout ll_item_lsv;
		TextView tv_item_lsv;
		CheckBox cb_item_lsv;
	}
}