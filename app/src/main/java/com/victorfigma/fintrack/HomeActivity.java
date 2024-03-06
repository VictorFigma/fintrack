package com.victorfigma.fintrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
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

    private int currentFragmentId;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadHomeLayout();
        loadTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_navbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.swapTheme) { //Moon-Sun top_navbar
            switchTheme();
            return true;
        }else if (item.getItemId() == R.id.searchBar){ //Search top_navbar
            setListenerSearch(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads the home layout (bottom_navbar + top_navabar + "add button" listeners).
     */
    private void loadHomeLayout(){
        setDefaultFragment();
        setListenerFragment();

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setCollapseIcon(R.drawable.baseline_arrow_back_24);
        setSupportActionBar(toolbar);

        setAddButton();
    }

    /**
     * Loads and applies the theme preference stored in the app's shared preferences.
     * If no preference is saved, it checks the user's system theme mode.
     */
    private void loadTheme(){
        SharedPreferences sharedPreferences = getSharedPreferences("THEME_MODE", Context.MODE_PRIVATE);
        boolean phoneMode = AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES;
        boolean darkMode = sharedPreferences.getBoolean("nightMode", phoneMode);

        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Replaces the fragment displayed in the container with a new fragment.
     *
     * @param fragment: the new fragment to be displayed.
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Configures the add button (dialog + listeners) for both stock & portfolio(stock + qtty) items.
     */
    private void setAddButton(){
        Dialog dialog = new Dialog(this);
        FloatingActionButton btnShowDialog;
        btnShowDialog = findViewById(R.id.addButtom);
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragmentId == R.id.stocks) dialog.setContentView(R.layout.add_stock);
                else if (currentFragmentId == R.id.portfolio) dialog.setContentView(R.layout.add_portfolio);
                setAddButtonListeners(dialog);
                dialog.show();
            }
        });
    }

    /**
     * Configures the click actions (cancel/add) for the add stock/portfolio dialog.
     *
     * @param dialog: the dialog instance to configure actions for.
     */
    private void setAddButtonListeners(Dialog dialog){
            setListenerCancel(dialog);

            if (currentFragmentId == R.id.stocks) {
                setListenerAddStock(dialog);
            }else if (currentFragmentId == R.id.portfolio){
                setListenerAddPortfolio(dialog);
            }
    }

    /**
     * Loads the default fragment.
     */
    private void setDefaultFragment(){
        currentFragment = new StocksFragment();
        replaceFragment(currentFragment);
        currentFragmentId = R.id.stocks;
    }

    /**
     * Attaches a click listener that adds a stock item.
     *
     * @param dialog: the dialog where the add button is.
     */
    private void setListenerAddStock(Dialog dialog){
        EditText codeInput = dialog.findViewById(R.id.addCode);
        Button btnAddStock = dialog.findViewById(R.id.btnAdd);
        btnAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeInput.getText().toString().toUpperCase();
                ManageStockData.addStock(HomeActivity.this, code);
                dialog.dismiss();
                currentFragment = new StocksFragment();
                replaceFragment(currentFragment);
            }
        });
    }

    /**
     * Attaches a click listener that dismisses the dialog.
     *
     * @param dialog: the dialog where the cancel button is.
     */
    private void setListenerCancel(Dialog dialog){
        Button btnCancel = dialog.findViewById(R.id.btnCancel); //Used in both stock & portfolio
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Attaches a click listener that changes the fragment.
     */
    private void setListenerFragment(){
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavbarView.setBackground(null);
        binding.bottomNavbarView.setOnItemSelectedListener(item -> {

            //Can't do a switch since constants are not final in ADT 14
            currentFragmentId = item.getItemId();
            if(currentFragmentId == R.id.stocks){
                currentFragment = new StocksFragment();
                replaceFragment(currentFragment);
            }
            else if(currentFragmentId == R.id.portfolio){
                currentFragment =new PortfolioFragment();
                replaceFragment(currentFragment);
            }

            return true;
        });
    }

    /**
     * Attaches a click listener that adds a portfolio(stock + qtty) item.
     *
     * @param dialog: the dialog where the add button is.
     */
    private void setListenerAddPortfolio(Dialog dialog){
        Button btnAddPortfolio = dialog.findViewById(R.id.btnAdd);
        btnAddPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText codeInput = dialog.findViewById(R.id.addCode);
                EditText qttyInput = dialog.findViewById(R.id.addQtty);
                String code = codeInput.getText().toString().toUpperCase();
                String qtty = qttyInput.getText().toString();
                ManagePortfolioData.addPortfolio(HomeActivity.this, code, qtty);
                dialog.dismiss();
                currentFragment = new PortfolioFragment();
                replaceFragment(currentFragment);
            }
        });
    }

    /**
     * Attaches a click listener that updates the stock/portfolio list displayed.
     *
     * @param item: the searchBar.
     */
    private void setListenerSearch(MenuItem item){
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String query) {
                if(currentFragmentId == R.id.stocks){
                    StocksFragment frag = (StocksFragment) currentFragment;
                    frag.updateDisplayedStocks(query);
                } else if (currentFragmentId == R.id.portfolio) {
                    PortfolioFragment frag = (PortfolioFragment) currentFragment;
                    frag.updateDisplayedPortfolio(query);
                }
                return false;
            }
        });
    }

    /**
     * Toggles the app's theme between light and dark mode, saving the current theme preference in SharedPreferences.
     */
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