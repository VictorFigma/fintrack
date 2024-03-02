package com.victorfigma.fintrack.portfolio;

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

public class PortfolioFragment extends Fragment {

    private ListView listView;
    private PortfolioListAdapter listAdapter;
    private ArrayList<StringFloatPair> dataArrayList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        SharedPreferencesUtil util = new SharedPreferencesUtil(getActivity(), "my_portfolio");

        StringFloatPair[] retrievedPortfolio = util.getPortfolio();
        if (retrievedPortfolio != null) {
            for (StringFloatPair pair : retrievedPortfolio) {
                dataArrayList.add(pair);
            }
        }
        Collections.sort(dataArrayList, new StringFloatPair.StringFloatPairComparator());

        listView = (ListView) view.findViewById(R.id.portfolioListView);
        listAdapter = new PortfolioListAdapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        return view;
    }
}