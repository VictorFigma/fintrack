package com.victorfigma.fintrack.utils;

import static com.victorfigma.fintrack.MainActivity.pythonGetPriceScrip;

import android.content.Context;
import android.widget.Toast;

public class CommonMethods {

    /**
     * Checks if a given stock code is valid based on length and existence.
     *
     * @param context the context for accessing resources and displaying toasts.
     * @param code the stock code to validate.
     * @return true if the code is valid, false otherwise.
     */
    public static boolean isValidStock(Context context, String code){
        if(code.length() > 9){
            CommonMethods.showToast(context, code + " is too long!");
            return false;
        }

        Float price = Float.parseFloat(pythonGetPriceScrip.getPrice(code));
        if(price == -1) {
            CommonMethods.showToast(context, code + " symbol not found!");
            return false;
        }

        return true;
    }

    /**
     * Displays a toast message on the screen.
     *
     * @param context the application context required for displaying the toast.
     * @param text the message to be displayed in the toast.
     */
    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}