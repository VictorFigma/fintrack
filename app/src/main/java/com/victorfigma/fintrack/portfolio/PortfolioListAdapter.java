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

    /**
     * Gets a view for a specific position in the list adapter.
     *
     * @param position the position of the item in the dataset.
     * @param view the existing view to reuse, or null if not available.
     * @param parent the parent ViewGroup that owns this view.
     * @return a configured view for the specified position.
     * @throws NullPointerException if context is null.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listed_item_portfolio, parent, false);
        }

        configPortfolioItem(position, view);

        return view;
    }

    /**
     * Configures a portfolio item view with data from the given position.
     *
     * @param position the position of the item in the dataset.
     * @param view the view to configure.
     */
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

    /**
     * Sets up a listener on the delete button to remove a portfolio.
     *
     * @param view the view containing the button.
     * @param listData the StringFloatPair object associated with the item to be deleted.
     */
    private void setDeleteListener(View view, StringFloatPair listData) {
        ShapeableImageView deleteButton = view.findViewById(R.id.deleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(listData);
            }
        });
    }

    /**
     * Sets up a listener on the edit button to edit the qtty of a portfolio item.
     *
     * @param view the view containing the button.
     * @param listData the StringFloatPair object associated with the item to be deleted.
     */
    private void setEditListener(View view, StringFloatPair listData){
        ShapeableImageView editButton = view.findViewById(R.id.editItem);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(listData);
            }
        });
    }

    /**
     * Displays a confirmation dialog for deleting a portfolio item.
     *
     * @param selectedItem the portfolio item to be deleted.
     */
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

    /**
     * Displays a confirmation dialog for editing the qtty of a portfolio item.
     *
     * @param selectedItem the portfolio item to be edited
     */
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

    /**
     * Updates the internal portfolio list and notifies the adapter of changes.
     *
     * @param updatedArray the new list of StringFloatPair objects representing the portfolio.
     */
    public void updatePortfolioList(ArrayList<StringFloatPair> updatedArray){
        portfolioItemsArray.clear();
        portfolioItemsArray.addAll(updatedArray);
        notifyDataSetChanged();
    }
}