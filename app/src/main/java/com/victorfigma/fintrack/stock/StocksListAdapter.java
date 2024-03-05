package com.victorfigma.fintrack.stock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.victorfigma.fintrack.R;
import com.victorfigma.fintrack.utils.StringFloatPair;

import java.util.ArrayList;

public class StocksListAdapter extends ArrayAdapter<StringFloatPair> {

    private ArrayList<StringFloatPair> stockItemsArray;

    public StocksListAdapter(@NonNull Context context, ArrayList<StringFloatPair> dataArrayList){
        super(context, R.layout.listed_item_stocks, dataArrayList);
        this.stockItemsArray = dataArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent){
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_stocks, parent, false);
        }

        configStockItem(position, view);

        return view;
    }

    private void configStockItem(int position, View view){
        TextView listStock = view.findViewById(R.id.stocksStockCode);
        TextView listPrice = view.findViewById(R.id.stocksStockPrice);

        StringFloatPair stockItem = getItem(position);

        listStock.setText(stockItem.code);
        String qtty = String.format("%.2f", stockItem.qtty) + "$";
        listPrice.setText(qtty);

        setDeleteListener(view, stockItem);
    }

    private void setDeleteListener(View view, StringFloatPair listData){
        View deleteButton = view.findViewById(R.id.deleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(listData);
            }
        });
    }

    private void showDeleteDialog(final StringFloatPair selectedItem) {
        AlertDialog.Builder mDeleteDialogBuilder = new AlertDialog.Builder(getContext());
        mDeleteDialogBuilder.setTitle("Are you sure you want to delete \"" + selectedItem.code + "\" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManageStockData.removeStock(getContext(), selectedItem.code);
                        remove(selectedItem);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    public void updateStockList(ArrayList<StringFloatPair> updatedArray){
        stockItemsArray.clear();
        stockItemsArray.addAll(updatedArray);
        notifyDataSetChanged();
    }
}
