package com.victorfigma.fintrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.victorfigma.fintrack.utils.GetPrice;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_TIMER = 1303;

    public static GetPrice pythonGetPriceScrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pythonGetPriceScrip = new GetPrice(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMER);
    }
}