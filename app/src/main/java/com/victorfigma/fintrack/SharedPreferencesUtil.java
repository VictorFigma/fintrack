package com.victorfigma.fintrack;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesUtil {

    private static final String PREFS_NAME = "StoredData";

    private final String key;
    private SharedPreferences prefs;

    public SharedPreferencesUtil(Context context, String key) {
        this.key = key;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public StringFloatPair[] getPortfolio() {
        String json = prefs.getString(key + "_pair_array", null);
        if (json != null) {
            return new Gson().fromJson(json, StringFloatPair[].class);
        }
        return null;
    }

    public String[] getStocks() {
        String json = prefs.getString(key + "_strings", null);
        if (json != null) {
            return new Gson().fromJson(json, String[].class);
        }
        return null;
    }

    public void setPortfolio(StringFloatPair[] pairs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key + "_pair_array", new Gson().toJson(pairs));
        editor.apply();
    }

    public void setStocks(String[] array) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key + "_strings", new Gson().toJson(array));
        editor.apply();
    }
}