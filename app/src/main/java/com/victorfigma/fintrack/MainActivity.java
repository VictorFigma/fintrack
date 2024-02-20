package com.victorfigma.fintrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.victorfigma.fintrack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new StocksFragment());
        binding.bottomNavbarView.setBackground(null);

        binding.bottomNavbarView.setOnItemSelectedListener(item -> {

            //Can't do a switch since constants are not final in ADT 14
            int id = item.getItemId();
            if(id == R.id.stocks) replaceFragment(new StocksFragment());
            else if(id == R.id.portfolio) replaceFragment(new PortfolioFragment());

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}