package com.victorfigma.fintrack.utils;

import android.content.Context;
import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class GetPrice {

    private static Python py;

    public GetPrice(Context context){
        Python.start(new AndroidPlatform(context));
        this.py = Python.getInstance();
    }

    public static String getPrice(String s){
        try {
            PyObject price = py.getModule("stock_price").callAttr("get_current_price", s);
            return price.toString();
        } catch (Exception e) {
            Log.e("PythonError", e.getMessage());
            return "-1";
        }
    }
}
