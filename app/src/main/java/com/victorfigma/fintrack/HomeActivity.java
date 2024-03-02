package com.victorfigma.fintrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.victorfigma.fintrack.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FloatingActionButton btnShowDialog;
    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnShowDialog = findViewById(R.id.addButtom);
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        replaceFragment(new StocksFragment());
        currentFragment = R.id.stocks;
        binding.bottomNavbarView.setBackground(null);
        binding.bottomNavbarView.setOnItemSelectedListener(item -> {

            //Can't do a switch since constants are not final in ADT 14
            int id = item.getItemId();
            if(id == R.id.stocks) replaceFragment(new StocksFragment());
            else if(id == R.id.portfolio) replaceFragment(new PortfolioFragment());
            currentFragment = id;

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        if(currentFragment == R.id.stocks) dialog.setContentView(R.layout.add_stock);
        else if(currentFragment == R.id.portfolio) dialog.setContentView(R.layout.add_portfolio);
        buttomns_actions(dialog);
        dialog.show();
    }

    private void showToast(String text){
        Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void buttomns_actions(Dialog dialog){
        Button btnCancel = dialog.findViewById(R.id.btnCancel); //Used in both stock & portfolio
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        EditText codeInput = dialog.findViewById(R.id.addCode);
        if (currentFragment == R.id.stocks) {
            Button btnAddStock = dialog.findViewById(R.id.btnAdd);
            btnAddStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = codeInput.getText().toString().toUpperCase();
                    addStock(code);
                    showToast("TODO Validation" + code); //TODO
                    dialog.dismiss();
                }
            });
        }else if (currentFragment == R.id.portfolio){
            Button btnAddPortfolio = dialog.findViewById(R.id.btnAdd);
            btnAddPortfolio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText qttyInput = dialog.findViewById(R.id.addQtty);
                    String code = codeInput.getText().toString().toUpperCase();
                    String qtty = qttyInput.getText().toString();
                    addPortfolio(code, qtty);
                    showToast("TODO Validation" + code + qtty); //TODO
                    dialog.dismiss();
                }
            });
        }
    }

    private void addStock(String code){
        SharedPreferencesUtil util = new SharedPreferencesUtil(this, "my_stocks");
        String[] stockList = util.getStocks();
        if(isStockPresent(stockList, code)) return;
        if(!isValidStock(code)) return;
        stockList = addStringtoArray(stockList, code);
        util.setStocks(stockList);
        replaceFragment(new StocksFragment());
        showToast(code + " successfully added");
    }

    private void addPortfolio(String code, String qtty){
        SharedPreferencesUtil util = new SharedPreferencesUtil(this, "my_portfolio");
        StringFloatPair[] pairList = util.getPortfolio();
        if(isStockPresent(pairList, code)) return;
        if(!isValidStock(code)) return;
        if(!isValidQtty(qtty)) return;
        pairList = addPairToArray(pairList, code, Float.parseFloat(qtty));
        util.setPortfolio(pairList);
        replaceFragment(new PortfolioFragment());
        showToast(code + " successfully added");
    }

    private StringFloatPair[] addPairToArray(StringFloatPair[] pairList, String code, float quantity) {
        int newLength = pairList == null ? 1 : pairList.length + 1;
        StringFloatPair[] updatedPairList = new StringFloatPair[newLength];

        if (pairList != null) {
            System.arraycopy(pairList, 0, updatedPairList, 0, pairList.length);
        }

        updatedPairList[newLength - 1] = new StringFloatPair(code, quantity);
        return updatedPairList;
    }

    private String[] addStringtoArray(String [] stockList, String string){

        int newLength = stockList == null ? 1 : stockList.length + 1;
        String[] updatedStockList = new String[newLength];

        if (stockList != null) {
            System.arraycopy(stockList, 0, updatedStockList, 0, stockList.length);
        }
        updatedStockList[newLength - 1] = string;
        return updatedStockList;
    }

    public boolean isValidQtty(String qtty){
        if(qtty.length() > 9){
            showToast(qtty + " is too big!");
            return false;
        }
        try {
            Float.parseFloat(qtty);
            return true;
        }catch (NumberFormatException  e){
            showToast(qtty + " is not a valid number!");
            return false;
        }
    }
    public boolean isValidStock(String code){
        if(code.length() > 6){
            showToast(code + " is too long!");
            return false;
        }
        if(true) {//TODO check if exists
            return true;
        }
        showToast(code + " is not valid!");
        return false;
    }

    public boolean isStockPresent(String array[], String code) {
        if (array == null) {
            return false;
        }

        for (String stock : array) {
            if (stock.equals(code)) {
                showToast(code + " is already listed!");
                return true;
            }
        }
        return false;
    }

    public boolean isStockPresent(StringFloatPair array[], String code) {
        if (array == null) {
            return false;
        }

        for (StringFloatPair pair : array) {
            if (pair.code.equals(code)) {
                showToast(code + " is already listed!");
                return true;
            }
        }
        return false;
    }
}