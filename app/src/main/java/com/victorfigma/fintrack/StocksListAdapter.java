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

public class StocksListAdapter extends ArrayAdapter<StringFloatPair> {
    public StocksListAdapter(@NonNull Context context, ArrayList<StringFloatPair> dataArrayList){
        super(context, R.layout.listed_item_stocks, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent){
        StringFloatPair listData = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_stocks, parent, false);
        }

        TextView listStock = view.findViewById(R.id.stocksStockCode);
        TextView listPrice = view.findViewById(R.id.stocksStockPrice);

        listStock.setText(listData.code);
        listPrice.setText("-1");  //TODO retrieve stock price

        return view;
    }
}