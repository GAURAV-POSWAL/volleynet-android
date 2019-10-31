package com.volleynet.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class DebugToast {
    public static boolean DEBUG = true;
    private static Toast toast;

    /**
     * Creating the single instance of toast, I guess this is the good way to show debug toast.
     * If you think that individual instance is necessary then do change it.
     *
     * @param context context needed to create the toast
     * @param message message to be displayed
     */
    public synchronized static void show(Context context, String message) {
        // If it's not debug then do nothing
        if (!DEBUG) return;

        if (context == null) {
            // if the context is null then we make toast = null and do nothing.
            toast = null;
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        toast.setText("DEBUG: " + message);
        toast.show();
    }

    /**
     * Creating the single instance of toast, I guess this is the good way to show debug toast.
     * If you think that individual instance is necessary then do change it.
     *
     * @param context context needed to create the toast
     * @param resId   resource string to be displayed
     */
    public synchronized static void show(Context context, int resId) {
        // If it's not debug then do nothing
        if (!DEBUG) return;

        if (context == null) {
            // if the context is null then we make toast = null and do nothing.
            toast = null;
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        toast.setText(resId);
        toast.show();
    }
}
