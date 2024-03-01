package com.victorfigma.fintrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StocksFragment extends Fragment {

    ListView listView;
    StocksListAdapter listAdapter;
    ArrayList<StocksListData> dataArrayList= new ArrayList<>();
    StocksListData listData;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks, container, false);

        String[] defaultListStocks = {"^GSPC", "ETH", "ETH", "ETH", "ETH", "ETH", "ETH", "ETH", "ETH", "ETH", "ETH", "a", "d", "END"};
        int[] defaultListPrices = {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 ,3 ,4 ,5};

        for (int i=0; i < defaultListStocks.length; i++){
            listData = new StocksListData(defaultListStocks[i], defaultListPrices[i]);
            dataArrayList.add(listData);
        }

        listView = (ListView) view.findViewById(R.id.stocksListView);
        listAdapter = new StocksListAdapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        return view;
    }

}