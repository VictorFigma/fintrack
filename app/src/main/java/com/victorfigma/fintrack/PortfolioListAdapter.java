package com.victorfigma.fintrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PortfolioListAdapter extends ArrayAdapter<StringIntPair> {
    public PortfolioListAdapter(@NonNull Context context, ArrayList<StringIntPair> dataArrayList){
        super(context, R.layout.listed_item_portfolio, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent){
        StringIntPair listData = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_portfolio, parent, false);
        }

        TextView listStock = view.findViewById(R.id.portfolioStockCode);
        TextView listPrice = view.findViewById(R.id.portfolioStockPrice);

        listStock.setText(listData.code);
        listPrice.setText(String.valueOf(listData.qtty)); //TODO retrieve stock price and * listData.qtty

        return view;
    }
}