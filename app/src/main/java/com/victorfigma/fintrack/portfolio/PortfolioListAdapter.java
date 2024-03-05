package com.victorfigma.fintrack.portfolio;

import static com.victorfigma.fintrack.MainActivity.pythonGetPriceScrip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.imageview.ShapeableImageView;
import com.victorfigma.fintrack.R;
import com.victorfigma.fintrack.utils.StringFloatPair;

import java.util.ArrayList;

public class PortfolioListAdapter extends ArrayAdapter<StringFloatPair> {

    private AlertDialog.Builder mDeleteDialogBuilder;
    private AlertDialog.Builder mEditDialogBuilder;
    private ArrayList<StringFloatPair> portfolioItemsArray;

    public PortfolioListAdapter(@NonNull Context context, ArrayList<StringFloatPair> dataArrayList) {
        super(context, R.layout.listed_item_portfolio, dataArrayList);
        this.portfolioItemsArray = dataArrayList;
        mDeleteDialogBuilder = new AlertDialog.Builder(getContext());
        mEditDialogBuilder = new AlertDialog.Builder(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_portfolio, parent, false);
        }

        configPortfolioItem(position, view);

        return view;
    }

    private void configPortfolioItem(int position, View view){
        TextView listStock = view.findViewById(R.id.portfolioStockCode);
        TextView listPrice = view.findViewById(R.id.portfolioStockPrice);

        StringFloatPair portfolioItem = getItem(position);
        String qtty = String.format("%.2f", portfolioItem.qtty) + "$";
        listStock.setText(portfolioItem.code);
        listPrice.setText(qtty);

        setDeleteListener(view, portfolioItem);
        setEditListener(view, portfolioItem);
    }

    private void setDeleteListener(View view, StringFloatPair listData) {
        ShapeableImageView deleteButton = view.findViewById(R.id.deleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(listData);
            }
        });
    }

    private void setEditListener(View view, StringFloatPair listData){
        ShapeableImageView editButton = view.findViewById(R.id.editItem);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(listData);
            }
        });
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
        Float currentPrice = Float.parseFloat(pythonGetPriceScrip.getPrice(selectedItem.code));
        inputQtty.setHint(String.valueOf(selectedItem.qtty/currentPrice));
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) inputQtty.setTextColor(Color.BLACK);

        mEditDialogBuilder.setTitle("Input the new quantity for " + selectedItem.code)
                .setView(inputQtty)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newQuantity = inputQtty.getText().toString();
                        if(ManagePortfolioData.editPortfolio(getContext(), selectedItem.code, newQuantity)){
                            selectedItem.qtty = Float.parseFloat(newQuantity) * currentPrice;
                            notifyDataSetChanged();
                        };
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

    public void updatePortfolioList(ArrayList<StringFloatPair> updatedArray){
        portfolioItemsArray.clear();
        portfolioItemsArray.addAll(updatedArray);
        notifyDataSetChanged();
    }
}