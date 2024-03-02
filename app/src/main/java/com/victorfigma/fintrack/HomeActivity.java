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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.victorfigma.fintrack.databinding.ActivityHomeBinding;
import com.victorfigma.fintrack.portfolio.ManagePortfolioData;
import com.victorfigma.fintrack.portfolio.PortfolioFragment;
import com.victorfigma.fintrack.stock.ManageStockData;
import com.victorfigma.fintrack.stock.StocksFragment;

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
                    ManageStockData.addStock(HomeActivity.this, code);
                    replaceFragment(new StocksFragment());
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
                    ManagePortfolioData.addPortfolio(HomeActivity.this, code, qtty);
                    replaceFragment(new PortfolioFragment());
                    dialog.dismiss();
                }
            });
        }
    }

    public void replaceFragment(Fragment fragment){
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
}