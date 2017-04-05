package com.seuic.gaojie.utils.ui;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Dell on 2017/3/13.
 */

public class ToastUtils {
    private static Toast mToast;

    private ToastUtils() {}

    public static void setNull() {
        if (mToast != null) {
            mToast = null;
        }
    }

    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}
