package com.volleynet.utils;

import android.util.Log;

public class AppLog {
    public static boolean DEBUG = true;
    public static String TAG = "LoggingTag"; //default tag for Log methods

    public static void d(Object message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            if (message == null) {
                Log.d(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + "null");
            } else {
                Log.d(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + message.toString());
            }
        }
    }

    public static void v(Object message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            if (message == null) {
                Log.v(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + "null");
            } else {
                Log.v(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + message.toString());
            }
        }
    }

    public static void e(Exception e) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            if (e == null || e.getMessage() == null) {
                Log.e(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + "null");
            } else {
                Log.e(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + e.getMessage());
            }
        }
    }

    public static void e(Object message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            if (message == null) {
                Log.e(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + "null");
            } else {
                Log.e(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + message.toString());
            }
        }
    }

    public static void w(Object message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            if (message == null) {
                Log.w(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + "null");
            } else {
                Log.w(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + message.toString());
            }
        }
    }

    public static void i(Object message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            if (message == null) {
                Log.i(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + "null");
            } else {
                Log.i(TAG, className + "." + methodName + "()|#" + lineNumber + "|" + message.toString());
            }
        }
    }
}