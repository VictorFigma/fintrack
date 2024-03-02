package com.victorfigma.fintrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class StocksFragment extends Fragment {

    private ListView listView;
    private StocksListAdapter listAdapter;
    private ArrayList<StringIntPair> dataArrayList = new ArrayList<>();

    private StringIntPair listData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks, container, false);

        SharedPreferencesUtil util = new SharedPreferencesUtil(getActivity(), "my_stocks");

        /*String[] stockSymbols = {"AAPL", "GOOG", "TSLA"};
        util.setStocks(stockSymbols);
        */

        String[] retrievedStocks = util.getStocks();

        for (int i=0; i < retrievedStocks.length; i++){
            listData = new StringIntPair(retrievedStocks[i]);
            dataArrayList.add(listData);
        }

        Collections.sort(dataArrayList, new StringIntPair.StringIntPairComparator());

        listView = (ListView) view.findViewById(R.id.stocksListView);
        listAdapter = new StocksListAdapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        return view;
    }

}