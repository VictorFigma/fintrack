package com.victorfigma.fintrack.stock;

import static com.victorfigma.fintrack.MainActivity.pythonGetPriceScrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.victorfigma.fintrack.R;
import com.victorfigma.fintrack.utils.SharedPreferencesUtil;
import com.victorfigma.fintrack.utils.StringFloatPair;

import java.util.ArrayList;
import java.util.Collections;

public class StocksFragment extends Fragment {

    private ArrayList<StringFloatPair> stockList;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks, container, false);
        setAdapter(view);
        return view;
    }

    private ArrayList<StringFloatPair> retrieveStoredStocks(){
        ArrayList<StringFloatPair> dataArrayList = new ArrayList<>();

        SharedPreferencesUtil util = new SharedPreferencesUtil(getActivity(), "my_stocks");

        String[] retrievedStocks = util.getStocks();

        for (int i = 0; i < retrievedStocks.length; i++) {
            String code = retrievedStocks[i];
            Float qtty = Float.parseFloat(pythonGetPriceScrip.getPrice(code));
            dataArrayList.add(new StringFloatPair(code, qtty));
        }
        Collections.sort(dataArrayList, new StringFloatPair.StringFloatPairComparator());

        return dataArrayList;
    }

    private void setAdapter(View view){
        this.stockList = retrieveStoredStocks();
        ArrayList<StringFloatPair> stockListCopy = new ArrayList<>(stockList); //Stocklist object can't be linked to the adapter because the search filter will edit it
        StocksListAdapter listAdapter = new StocksListAdapter(getActivity(), stockListCopy);
        this.listView = view.findViewById(R.id.stocksListView);
        listView.setAdapter(listAdapter);
    }

    public void updateDisplayedStocks(String textFilter){
        ArrayList<StringFloatPair> results = new ArrayList<>();
        if(textFilter.isEmpty()){
            results = stockList;
        }else{
            for (StringFloatPair pair: stockList){
                if(pair.code.contains(textFilter.toUpperCase().trim())){
                    results.add(pair);
                }
            }
       }
        ((StocksListAdapter) listView.getAdapter()).updateStockList(results);
    }
}