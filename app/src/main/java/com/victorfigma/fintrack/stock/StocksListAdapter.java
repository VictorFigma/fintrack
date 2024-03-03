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

    private AlertDialog.Builder mDeleteDialogBuilder;

    public StocksListAdapter(@NonNull Context context, ArrayList<StringFloatPair> dataArrayList){
        super(context, R.layout.listed_item_stocks, dataArrayList);
        mDeleteDialogBuilder = new AlertDialog.Builder(getContext());
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
        listPrice.setText(String.valueOf(listData.qtty));

        View deleteButton = view.findViewById(R.id.deleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(listData);
            }
        });

        return view;
    }

    private void showDeleteDialog(final StringFloatPair selectedItem) {
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
}
