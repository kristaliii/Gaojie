package com.seuic.gaojie.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seuic.gaojie.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 2017/3/15.
 */

public class EditAdapter extends BaseAdapter {
    ArrayList<String> list ;
    Context mContext;

    public EditAdapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_edit, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String item = list.get(position);
        holder.tv_item_edit.setGravity(Gravity.CENTER);
        holder.tv_item_edit.setText(item);
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.tv_item_edit)TextView tv_item_edit;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
