package com.victorfigma.fintrack.portfolio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;
import com.victorfigma.fintrack.R;
import com.victorfigma.fintrack.utils.StringFloatPair;

import java.util.ArrayList;

public class PortfolioListAdapter extends ArrayAdapter<StringFloatPair> {

    private AlertDialog.Builder mDeleteDialogBuilder;
    private AlertDialog.Builder mEditDialogBuilder;

    public PortfolioListAdapter(@NonNull Context context, ArrayList<StringFloatPair> dataArrayList) {
        super(context, R.layout.listed_item_portfolio, dataArrayList);
        mDeleteDialogBuilder = new AlertDialog.Builder(getContext());
        mEditDialogBuilder = new AlertDialog.Builder(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent) {
        StringFloatPair listData = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_portfolio, parent, false);
        }

        TextView listStock = view.findViewById(R.id.portfolioStockCode);
        TextView listPrice = view.findViewById(R.id.portfolioStockPrice);
        listStock.setText(listData.code);
        listPrice.setText(String.format("%.2f", listData.qtty)); //TODO retrieve stock price and * listData.qtty

        ShapeableImageView deleteButton = view.findViewById(R.id.deleteItem);
        ShapeableImageView editButton = view.findViewById(R.id.editItem);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(listData);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(listData);
            }
        });

        return view;
    }

    private void showDeleteDialog(final StringFloatPair selectedItem) {
        mDeleteDialogBuilder.setTitle("Are you sure you want to delete \"" + selectedItem.code + "\" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManagePortfolioData.removePortfolio(getContext(), selectedItem.code);
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

    private void showEditDialog(final StringFloatPair selectedItem) {
        EditText inputQtty = new EditText(getContext());
        inputQtty.setHint(String.valueOf(selectedItem.qtty));

        mEditDialogBuilder.setTitle("Input the new quantity for " + selectedItem.code)
                .setView(inputQtty)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newQuantity = inputQtty.getText().toString();
                        ManagePortfolioData.editPortfolio(getContext(), selectedItem.code, newQuantity);
                        selectedItem.qtty = Float.parseFloat(newQuantity);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}