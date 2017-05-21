package com.example.demo.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class NoticeDialogUtil {
/**
 * 提示更新对话框
 * @param context
 */
	public static void showNoticeDialog(Context context) {
        String tile = "软件版本更新";
        String massage = "暂无版本更新...";
        String negativeButtonText = "确定";
        boolean canUpdate = true;
        
        if(canUpdate){
            StringBuilder sb = new StringBuilder();
            sb.append("发现新版本," +"大小："  + "\n");
            sb.append("更新内容：" +"\n");
            massage = sb.toString();
            negativeButtonText = "以后再说";
        }
        
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle(tile);
        builder.setMessage(massage);
        if(canUpdate) {
            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog noticeDialog = builder.create();
        noticeDialog.show();
    }
}
