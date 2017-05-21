package com.example.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class FifthActivity extends Activity {
    private FifthActivity mContext;
    private int lastPress = 0;
    private boolean delState = false;
    private List<String> curList = new ArrayList<String>();

    private ListView curListView;
    private CurAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_fifth);
        init();
    }

    private void init() {
        mContext = this;
        String[] strings = {"aa", "bb", "cc", "dd", "ee"};
        for (String s : strings) {
            curList.add(s);
        }

        curListView = (ListView) findViewById(R.id.lv_contents);
        adapter = new CurAdapter();
        curListView.setAdapter(adapter);

        curListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (delState) {
                    if (lastPress < parent.getCount()) {
                        View delview = parent.getChildAt(lastPress).findViewById(R.id.linear_del);
                        if (null != delview) {
                            delview.setVisibility(View.GONE);
                        }
                    }
                    delState = false;
                    return;
                } else {
                    Log.d("click:", position + "");
                }
//                lastPress = position;
            }
        });
        curListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            private View delview;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (lastPress < parent.getCount()) {
                    delview = parent.getChildAt(lastPress).findViewById(R.id.linear_del);
                    if (null != delview) {
                        delview.setVisibility(View.GONE);
                    }
                }

                delview = view.findViewById(R.id.linear_del);
                delview.setVisibility(View.VISIBLE);

                delview.findViewById(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delview.setVisibility(View.GONE);
                        curList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                delview.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delview.setVisibility(View.GONE);
                    }
                });

                lastPress = position;
                delState = true;
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			finish();
		}
    	return super.onOptionsItemSelected(item);
    }
    
    private class CurAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return curList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_fifth, null);
            }

            ((TextView) convertView.findViewById(R.id.tv_content)).setText(curList.get(position));
            return convertView;
        }
    }
}
