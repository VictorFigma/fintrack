package com.victorfigma.fintrack;

import android.content.Context;
import android.widget.Toast;

public class ManageStockData {

    protected static void addStock(Context context, String code){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_stocks");
        String[] stockList = util.getStocks();

        if(isStockPresent(context, stockList, code)) return;
        if(!isValidStock(context, code)) return;

        stockList = addStringtoArray(stockList, code);
        util.setStocks(stockList);

        showToast(context, code + " successfully added");
        showToast(context, "TODO Validation" + code); //TODO
    }

    protected static String[] addStringtoArray(String [] stockList, String string){
        int newLength = stockList == null ? 1 : stockList.length + 1;
        String[] updatedStockList = new String[newLength];

        if (stockList != null) {
            System.arraycopy(stockList, 0, updatedStockList, 0, stockList.length);
        }
        updatedStockList[newLength - 1] = string;
        return updatedStockList;
    }

    protected static boolean isStockPresent(Context context, String array[], String code) {
        if (array == null) {
            return false;
        }

        for (String stock : array) {
            if (stock.equals(code)) {
                showToast(context, code + " is already listed!");
                return true;
            }
        }
        return false;
    }

    protected static boolean isValidStock(Context context, String code){
        if(code.length() > 6){
            showToast(context, code + " is too long!");
            return false;
        }

        if(true) {//TODO check if exists
            return true;
        }
        showToast(context, code + " is not valid!");
        return false;
    }

    private static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}