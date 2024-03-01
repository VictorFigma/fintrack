package com.victorfigma.fintrack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PortfolioFragment extends Fragment {

    ListView listView;
    PortfolioListAdapter listAdapter;
    ArrayList<PortfolioListData> dataArrayList= new ArrayList<>();
    PortfolioListData listData;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        String[] defaultListStocks = {"^GSPC", "ETH"};
        int[] defaultListPrices = {3, 4};

        for (int i=0; i < defaultListStocks.length; i++){
            listData = new PortfolioListData(defaultListStocks[i], defaultListPrices[i]);
            dataArrayList.add(listData);
        }

        listView = (ListView) view.findViewById(R.id.portfolioListView);
        listAdapter = new PortfolioListAdapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        return view;
    }

}