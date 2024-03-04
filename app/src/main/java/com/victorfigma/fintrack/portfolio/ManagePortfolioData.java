package com.victorfigma.fintrack.portfolio;

import static com.victorfigma.fintrack.MainActivity.pythonGetPriceScrip;

import android.content.Context;
import android.widget.Toast;

import com.victorfigma.fintrack.utils.SharedPreferencesUtil;
import com.victorfigma.fintrack.utils.StringFloatPair;

import java.util.Arrays;

public class ManagePortfolioData {

    public static StringFloatPair[] addPairToArray(StringFloatPair[] pairList, String code, float quantity) {
        int newLength = pairList == null ? 1 : pairList.length + 1;
        StringFloatPair[] updatedPairList = new StringFloatPair[newLength];

        if (pairList != null) {
            System.arraycopy(pairList, 0, updatedPairList, 0, pairList.length);
        }
        updatedPairList[newLength - 1] = new StringFloatPair(code, quantity);
        return updatedPairList;
    }

    public static void addPortfolio(Context context, String code, String qtty){
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

    public static StringFloatPair[] deleteStringFloatPairFromArray(StringFloatPair[] portfolioList, String code) {
        if (portfolioList == null || portfolioList.length == 0) {
            return portfolioList;
        }

        return Arrays.stream(portfolioList)
                .filter(item -> !item.code.equals(code))
                .toArray(StringFloatPair[]::new);
    }

    public static boolean editPortfolio(Context context, String code, String qtty){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();

        if(!isValidQtty(context, qtty)) return false;

        StringFloatPair[] pairList_temp = deleteStringFloatPairFromArray(pairList, code);
        StringFloatPair[] pairList_temp2 = addPairToArray(pairList_temp, code, Float.parseFloat(qtty));
        util.setPortfolio(pairList_temp2);

        showToast(context, code + " successfully edited");
        return true;
    }

    public static boolean isStockPresent(Context context, StringFloatPair array[], String code) {
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

    public static boolean isValidQtty(Context context, String qtty){
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

    public static boolean isValidStock(Context context, String code){
        if(code.length() > 9){
            showToast(context, code + " is too long!");
            return false;
        }

        Float price = Float.parseFloat(pythonGetPriceScrip.getPrice(code));
        if(price == -1) {
            showToast(context, code + " symbol not found!");
            return false;
        }

        return true;
    }

    public static void removePortfolio(Context context, String code){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();

        pairList = deleteStringFloatPairFromArray(pairList, code);
        util.setPortfolio(pairList);

        showToast(context, code + " successfully deleted");
    }

    private static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}