package com.seuic.pulltorefresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshListView lv;
    private ArrayList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (PullToRefreshListView) findViewById(R.id.lv);

        list = new ArrayList();
        for (int i=0;i<30;i++) {
            list.add("显示数据"+i);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
        lv.setAdapter(adapter);

        lv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onLoadMore() {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        list.add("这是加载出来的数据");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                lv.refreshCompleted();
                            }
                        });
                    }
                }.start();
            }
        });
    }
}
