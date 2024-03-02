package com.victorfigma.fintrack;

import android.content.Context;
import android.widget.Toast;

public class ManagePortfolioData {

    protected static StringFloatPair[] addPairToArray(StringFloatPair[] pairList, String code, float quantity) {
        int newLength = pairList == null ? 1 : pairList.length + 1;
        StringFloatPair[] updatedPairList = new StringFloatPair[newLength];

        if (pairList != null) {
            System.arraycopy(pairList, 0, updatedPairList, 0, pairList.length);
        }
        updatedPairList[newLength - 1] = new StringFloatPair(code, quantity);
        return updatedPairList;
    }

    protected static void addPortfolio(Context context, String code, String qtty){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();

        if(isStockPresent(context, pairList, code)) return;
        if(!isValidStock(context, code)) return;
        if(!isValidQtty(context, qtty)) return;

        pairList = addPairToArray(pairList, code, Float.parseFloat(qtty));
        util.setPortfolio(pairList);

        showToast(context, code + " successfully added");
        showToast(context,"TODO Validation" + code + qtty); //TODO
    }

    protected static boolean isStockPresent(Context context, StringFloatPair array[], String code) {
        if (array == null) {
            return false;
        }

        for (StringFloatPair pair : array) {
            if (pair.code.equals(code)) {
                showToast(context, code + " is already listed!");
                return true;
            }
        }
        return false;
    }

    protected static boolean isValidQtty(Context context, String qtty){
        if(qtty.length() > 9){
            showToast(context, qtty + " is too big!");
            return false;
        }

        try {
            Float.parseFloat(qtty);
            return true;
        }catch (NumberFormatException  e){
            showToast(context, qtty + " is not a valid number!");
            return false;
        }
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