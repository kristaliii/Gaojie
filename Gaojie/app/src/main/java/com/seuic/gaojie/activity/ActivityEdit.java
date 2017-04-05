package com.seuic.gaojie.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seuic.gaojie.R;
import com.seuic.gaojie.adapter.EditAdapter;
import com.seuic.gaojie.application.BaseActivity;
import com.seuic.gaojie.db.DbDAO;
import com.seuic.gaojie.utils.ui.DialogEdit;
import com.seuic.gaojie.utils.ui.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2017/3/15.
 */

public class ActivityEdit extends BaseActivity {
    @BindView(R.id.lv_activity_edit) ListView mLvEdit;
    @BindView(R.id.et_activity_edit) EditText mEtEdit;
    @BindView(R.id.iv_merge_menu)ImageView mIvMenu;
    @BindView(R.id.tv_merge_title)TextView mTvTitle;

    private ArrayList<String> list; // 显示item内数据的集合
    private EditAdapter adapter;
    Activity mActivity;
    private DbDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_edit);
        ButterKnife.bind(this);
        mIvMenu.setVisibility(View.GONE);
        mTvTitle.setText("编辑");
        mActivity = this;
        list = new ArrayList<>();
        adapter = new EditAdapter(mActivity, list);
        mLvEdit.setAdapter(adapter);
        dao = new DbDAO(mActivity);
        setLvItemClick();
    }

    /**
     * 先查数据库，拿数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = dao.query();
        while (c.moveToNext()) {
            String text = c.getString(c.getColumnIndex("text"));
            list.add(0,text);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 单击item弹出对话框，进行修改
     */
    String initText;
    void setLvItemClick() {
        mLvEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent ListView
             * @param view  LinearLayout
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initText = list.get(position);
                DialogEdit dialogEdit = new DialogEdit(mActivity,dao, list, adapter, initText);
                dialogEdit.hintEditDialog(position);
            }
        });
    }

    /**
     * 按钮点击事件，点击按钮，插入数据
     * @param view
     */
    @OnClick({R.id.bt_activity_edit,R.id.iv_merge_back})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.bt_activity_edit: // 确定按钮（插入）
                editBarcodeType();
                break;
            case R.id.iv_merge_back: // 返回
                finish();
                break;
        }
    }

    private void editBarcodeType() {
        String content = mEtEdit.getText().toString().trim();
        if (content.equals("")) {
            ToastUtils.showToast(mActivity, "请输入类型");
            return ;
        }
        Cursor c = dao.query();
        while (c.moveToNext()) {
            String text = c.getString(c.getColumnIndex("text"));
            if (text.equals(content)) {
                ToastUtils.showToast(mActivity,"已存在");
                return; // 可以跳出while循环，并且后面不会执行；
            }
        }
        list.add(0,content);
        adapter.notifyDataSetChanged(); // 可以更新UI
        ContentValues values = new ContentValues();
        values.put("text", content);
        dao.insert(values);
        mEtEdit.setText("");
    }


    //可编辑的对话框
    /*private AlertDialog hintEditDialog;
    public void hintEditDialogs(final Context context, String text, final int position){
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        View inflate = View.inflate(context, R.layout.dialog_edit, null);
        TextView tv_dialog_title = (TextView) inflate.findViewById(R.id.tv_dialog_title);
        final EditText et_dialog_message = (EditText) inflate.findViewById(R.id.et_dialog_message);
        Button btn_ok = (Button) inflate.findViewById(R.id.btn_ok);
        Button btn_delete = (Button) inflate.findViewById(R.id.btn_delete);
        tv_dialog_title.setText("编辑");
        et_dialog_message.setText(text);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 修改
                String content = et_dialog_message.getText().toString().trim();
                ContentValues values = new ContentValues();
                values.put("text",content);
                dao.update(values,"text=?",new String[]{initText});
                list.remove(position);
                list.add(position,content);
                adapter.notifyDataSetChanged(); // 可以更新UI
                hintEditDialog.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 删除
                String remove = list.remove(position);
                dao.deleteText(remove);
                adapter.notifyDataSetChanged();
                hintEditDialog.dismiss();
            }
        });
        build.setView(inflate);
        if (hintEditDialog != null) {
            hintEditDialog.dismiss();
        }
        hintEditDialog = build.create();
        hintEditDialog.show();
    }*/
}
