package com.seuic.gaojie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seuic.gaojie.R;
import com.seuic.gaojie.bean.Barcode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 2017/3/13.
 */

public class BarcodeImportAdapter extends BaseAdapter {
    private String LOGTAG = "BarcodeImportAdapter...";

    Context mContext;
    ArrayList<Barcode> list;

    public BarcodeImportAdapter(Context mContext, ArrayList<Barcode> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Barcode getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addBarcodeItem(Barcode barcode) {
        list.add(barcode);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    /**
     * 加载一个item就执行此方法一次；
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    ViewHolder holder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_import_barcode, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
         Barcode item = getItem(position);
        holder.tv_item_barcode.setText(item.getBarcode());
        holder.tv_item_type.setText(item.getBarcodeType());
        // 新增一个背景属性
        holder.ll_item_barcode.setBackground(item.getColor());
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.tv_item_barcode) TextView tv_item_barcode;
        @BindView(R.id.tv_item_type) TextView tv_item_type;
        @BindView(R.id.ll_item_barcode) LinearLayout ll_item_barcode;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
