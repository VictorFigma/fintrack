package com.victorfigma.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    private int currentFragment;
    private ActivityHomeBinding binding;
    private FloatingActionButton btnShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

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

        loadTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_navbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.swapTheme) {
            switchTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void loadTheme(){
        SharedPreferences sharedPreferences = getSharedPreferences("THEME_MODE", Context.MODE_PRIVATE);
        boolean phoneMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        boolean darkMode = sharedPreferences.getBoolean("nightMode", phoneMode);

        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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

    private void switchTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("THEME_MODE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        boolean darkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        if(darkMode){
            editor = sharedPreferences.edit();
            editor.putBoolean("nightMode", false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            editor = sharedPreferences.edit();
            editor.putBoolean("nightMode", true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        editor.apply();
    }
}