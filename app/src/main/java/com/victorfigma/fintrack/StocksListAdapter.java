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

public class StocksListAdapter extends ArrayAdapter<StocksListData> {
    public StocksListAdapter(@NonNull Context context, ArrayList<StocksListData> dataArrayList){
        super(context, R.layout.listed_item_stocks, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent){
        StocksListData listData = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_stocks, parent, false);
        }

        TextView listStock = view.findViewById(R.id.stocksStockCode);
        TextView listPrice = view.findViewById(R.id.stocksStockPrice);

        listStock.setText(listData.stock);
        listPrice.setText(listData.price + " $");

        return view;
    }
}
