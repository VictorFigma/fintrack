package com.victorfigma.fintrack;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;


public class PortfolioFragment extends Fragment {

    private ListView listView;
    private PortfolioListAdapter listAdapter;
    private ArrayList<StringIntPair> dataArrayList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        SharedPreferencesUtil util = new SharedPreferencesUtil(getActivity(), "my_portfolio");

        /*
        StringIntPair pair1 = new StringIntPair("Item A", 10);
        StringIntPair[] portfolio = new StringIntPair[]{pair1};
        util.setPortfolio(portfolio);
        */

        StringIntPair[] retrievedPortfolio = util.getPortfolio();
        if (retrievedPortfolio != null) {
            for (StringIntPair pair : retrievedPortfolio) {
                dataArrayList.add(pair);
            }
        }

        Collections.sort(dataArrayList, new StringIntPair.StringIntPairComparator());

        listView = (ListView) view.findViewById(R.id.portfolioListView);
        listAdapter = new PortfolioListAdapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        return view;
    }

}