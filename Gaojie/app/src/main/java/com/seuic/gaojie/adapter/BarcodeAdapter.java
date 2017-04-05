package com.seuic.gaojie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seuic.gaojie.R;
import com.seuic.gaojie.bean.Barcode;
import com.seuic.gaojie.utils.LogUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 2017/3/13.
 */

public class BarcodeAdapter extends BaseAdapter {
    private String LOGTAG = "BarcodeAdapter...";

    Context mContext;
    ArrayList<Barcode> list;
    //用于存储CheckBox选中状态
//    HashMap<Integer,Boolean> mCBFlag;

    public BarcodeAdapter(Context mContext,ArrayList<Barcode> list) {
        this.mContext = mContext;
        this.list = list;
//        mCBFlag = new HashMap<Integer, Boolean>();
//        init();
    }

//    //初始化CheckBox状态
//    void init(){
//        for (int i = 0; i < list.size(); i++) {
//            mCBFlag.put(i, false);
//        }
//    }

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
            convertView = View.inflate(mContext, R.layout.item_barcode, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            LogUtils.i(LOGTAG,"position:"+position);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Barcode item = getItem(position);
        holder.tv_item_barcode.setText(item.getBarcode());
        holder.tv_item_type.setText(item.getBarcodeType());
        // 新增一个背景属性
        holder.ll_item_barcode.setBackground(item.getColor());

        if (item.isChoice()) {
            holder.cb_item_choose.setChecked(true);
        }else {
            holder.cb_item_choose.setChecked(false);
        }

//        //状态保存  CheckBox监听事件必须放在setChecked之前，否则后果自负
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (item.isChoice()) {
//                    mCBFlag.put(position, false);
//                    item.setChoice(false);
//                    notifyDataSetChanged();
//                } else {
//                    mCBFlag.put(position, true);
//                    item.setChoice(true);
//                    notifyDataSetChanged();
//                }
//            }
//        });
//        holder.cb_item_choose.setChecked(mCBFlag.get(position));
        //状态保存  CheckBox监听事件必须放在setChecked之前，否则后果自负
        /*holder.cb_item_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.setChoice(true);
                }else{
                    item.setChoice(false);
                }
            }
        });
        holder.cb_item_choose.setChecked(mCBFlag.get(position));*/
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.tv_item_barcode) TextView tv_item_barcode;
        @BindView(R.id.tv_item_type) TextView tv_item_type;
        @BindView(R.id.ll_item_barcode) LinearLayout ll_item_barcode;
        @BindView(R.id.cb_item_choose)  CheckBox cb_item_choose;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
