package com.volleynet.utils;

import android.content.Context;
import android.widget.Toast;

public class UserToast {
    private static Toast toast;

    /**
     * Creating the single instance of toast, I guess this is the good way to show debug toast.
     * If you think that individual instance is necessary then do change it.
     *
     * @param context context needed to create the toast
     * @param message message to be displayed
     */
    public synchronized static void show(Context context, String message) {
        if (toast == null && context != null) {
            toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
        }
        if (toast != null) {
            toast.setText(message);
            toast.show();
        }
    }

    /**
     * Creating the single instance of toast, I guess this is the good way to show debug toast.
     * If you think that individual instance is necessary then do change it.
     *
     * @param context context needed to create the toast
     * @param resId   resource string to be displayed
     */
    public synchronized static void show(Context context, int resId) {
        if (toast == null && context != null) {
            toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
        }
        if (toast != null) {
            toast.setText(resId);
            toast.show();
        }
    }
}
