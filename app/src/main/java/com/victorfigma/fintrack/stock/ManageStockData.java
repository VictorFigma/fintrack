package com.victorfigma.fintrack.stock;

import static java.util.Arrays.stream;

import android.content.Context;

import com.victorfigma.fintrack.utils.CommonMethods;
import com.victorfigma.fintrack.utils.SharedPreferencesUtil;

public class ManageStockData {

    /**
     * Adds a VALID, NOT DUPLICATED stock code to the shared preferences.
     *
     * @param context: the context for accessing shared preferences.
     * @param code: the stock code to add.
     */
    public static void addStock(Context context, String code){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_stocks");
        String[] stockList = util.getStocks();

        if(isStockPresent(context, stockList, code)) return;
        if(!CommonMethods.isValidStock(context, code)) return;

        stockList = addStringtoArray(stockList, code);
        util.setStocks(stockList);

        CommonMethods.showToast(context, code + " successfully added");
    }

    /**
     * Adds a new String element to an existing array.
     *
     * @param stockList the existing array of String objects.
     * @param string the stock code that will be added.
     * @return a new String array with the added element.
     */
    public static String[] addStringtoArray(String [] stockList, String string){
        int newLength = stockList == null ? 1 : stockList.length + 1;
        String[] updatedStockList = new String[newLength];

        if (stockList != null) {
            System.arraycopy(stockList, 0, updatedStockList, 0, stockList.length);
        }
        updatedStockList[newLength - 1] = string;
        return updatedStockList;
    }

    /**
     * Removes a specific string from the provided array.
     *
     * @param stockList the array containing stock codes.
     * @param code the code of the stock to remove.
     * @return a new String array without the removed element.
     */
    public static String [] deleteStringFromArray(String [] stockList, String code){
        return stockList == null || stockList.length == 0
                ? stockList : stream(stockList).filter(s -> !s.equals(code)).toArray(String[]::new);
    }

    /**
     * Checks if a given stock code is already present in the provided array.
     *
     * @param context the context for accessing resources.
     * @param array the array containing stock codes.
     * @param code the stock code to search for.
     * @return true if the code is found, false otherwise.
     */
    public static boolean isStockPresent(Context context, String array[], String code) {
        if (array == null) {
            return false;
        }

        for (String stock : array) {
            if (stock.equals(code)) {
                CommonMethods.showToast(context, code + " is already listed!");
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a stock code from the shared preferences list.
     *
     * @param context the context for accessing shared preferences and displaying toasts.
     * @param code the stock code to remove.
     */
    public static void removeStock(Context context, String code){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_stocks");
        String[] stockList = util.getStocks();

        stockList = deleteStringFromArray(stockList, code);
        util.setStocks(stockList);

        CommonMethods.showToast(context, code + " successfully deleted");
    }
}