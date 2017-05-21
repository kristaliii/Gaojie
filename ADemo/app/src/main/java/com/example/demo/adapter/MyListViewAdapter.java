package com.example.demo.adapter;

import com.example.demo.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter {
	int mSelect;
	String [] objects;
	Context context;
	
	public MyListViewAdapter(Context context,String[] objects) {
		this.objects = objects;
		this.context = context;
	}
	public void changeSelected(int positon){ //刷新方法
		if(positon != mSelect){
	      mSelect = positon;
	     notifyDataSetChanged();
	     }
	}
	
	@Override
	public int getCount() {
		return objects.length;
	}

	@Override
	public Object getItem(int position) {
		return objects[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater factory = LayoutInflater.from(context);
        View v = (View) factory.inflate(R.layout.item_lsv, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_item_lsv);
        tv.setText("test");
//     }
        if(mSelect==position){    
         v.setBackgroundResource(R.color.huise);  //选中项背景
        }else{
         v.setBackgroundResource(R.color.huise);  //其他项背景
        }
        
        return v;
	}

}
