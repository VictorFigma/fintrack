package com.victorfigma.fintrack.portfolio;

import android.content.Context;

import com.victorfigma.fintrack.utils.CommonMethods;
import com.victorfigma.fintrack.utils.SharedPreferencesUtil;
import com.victorfigma.fintrack.utils.StringFloatPair;

import java.util.Arrays;

public class ManagePortfolioData {

    /**
     * Adds a new StringFloatPair element to an existing array.
     *
     * @param pairList the existing array of StringFloatPair objects.
     * @param code the stock code for the new pair.
     * @param qtty the quantity for the new pair.
     * @return a new StringFloatPair array with the added element.
     */
    public static StringFloatPair[] addPairToArray(StringFloatPair[] pairList, String code, float qtty) {
        int newLength = pairList == null ? 1 : pairList.length + 1;
        StringFloatPair[] updatedPairList = new StringFloatPair[newLength];

        if (pairList != null) {
            System.arraycopy(pairList, 0, updatedPairList, 0, pairList.length);
        }
        updatedPairList[newLength - 1] = new StringFloatPair(code, qtty);
        return updatedPairList;
    }

    /**
     * Adds a VALID, NOT DUPLICATED portfolio (code + qtty) to the shared preferences.
     *
     * @param context: the context for accessing shared preferences.
     * @param code: the portfolio code to add.
     * @param qtty: the portfolio qtty to add.
     */
    public static void addPortfolio(Context context, String code, String qtty){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();

        if(isStockPresent(context, pairList, code)) return;
        if(!CommonMethods.isValidStock(context, code)) return;
        if(!isValidQtty(context, qtty)) return;

        pairList = addPairToArray(pairList, code, Float.parseFloat(qtty));
        util.setPortfolio(pairList);

        CommonMethods.showToast(context, code + " successfully added");
    }

    /**
     * Removes a specific StringFloatPair element from the provided array.
     *
     * @param portfolioList the array containing StringFloatPair objects.
     * @param code the code of the element to remove.
     * @return a new StringFloatPair array without the removed element.
     */
    public static StringFloatPair[] deleteStringFloatPairFromArray(StringFloatPair[] portfolioList, String code) {
        if (portfolioList == null || portfolioList.length == 0) {
            return portfolioList;
        }

        return Arrays.stream(portfolioList)
                .filter(item -> !item.code.equals(code))
                .toArray(StringFloatPair[]::new);
    }

    /**
     * Edits a portfolio quantity, storing it in shared preferences. Ensures valid qtty.
     *
     * @param context the context for accessing resources and shared preferences.
     * @param code the stock code to edit.
     * @param qtty the new quantity for the stock.
     * @return true if the edit is successful, false otherwise.
     */
    public static boolean editPortfolio(Context context, String code, String qtty){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();

        if(!isValidQtty(context, qtty)) return false;

        StringFloatPair[] pairList_temp = deleteStringFloatPairFromArray(pairList, code);
        StringFloatPair[] pairList_temp2 = addPairToArray(pairList_temp, code, Float.parseFloat(qtty));
        util.setPortfolio(pairList_temp2);

        CommonMethods.showToast(context, code + " successfully edited");
        return true;
    }

    /**
     * Checks if a given stock code is already present in the provided array.
     *
     * @param context the context for accessing resources.
     * @param array the array containing StringFloatPair objects.
     * @param code the stock code to search for.
     * @return true if the code is found, false otherwise.
     */
    public static boolean isStockPresent(Context context, StringFloatPair array[], String code) {
        if (array == null) {
            return false;
        }

        for (StringFloatPair pair : array) {
            if (pair.code.equals(code)) {
                CommonMethods.showToast(context, code + " is already listed!");
                return true;
            }
        }
        return false;
    }

    /**
     * Validates a quantity string, ensuring it's a valid number and within a reasonable length.
     *
     * @param context the context for accessing resources and displaying toasts.
     * @param qtty the quantity string to validate.
     * @return true if the quantity is valid, false otherwise.
     */
    public static boolean isValidQtty(Context context, String qtty){
        if(qtty.length() > 9){
            CommonMethods.showToast(context, qtty + " is too big!");
            return false;
        }

        try {
            Float.parseFloat(qtty);
            return true;
        }catch (NumberFormatException  e){
            CommonMethods.showToast(context, qtty + " is not a valid number!");
            return false;
        }
    }

    /**
     * Removes a portfolio entry (StringFloatPair) in shared preferences.
     *
     * @param context the context for accessing shared preferences and displaying toasts.
     * @param code the code of the portfolio entry to remove.
     */
    public static void removePortfolio(Context context, String code){
        SharedPreferencesUtil util = new SharedPreferencesUtil(context, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();

        pairList = deleteStringFloatPairFromArray(pairList, code);
        util.setPortfolio(pairList);

        CommonMethods.showToast(context, code + " successfully deleted");
    }
}