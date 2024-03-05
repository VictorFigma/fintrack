package com.victorfigma.fintrack.portfolio;

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

public class PortfolioFragment extends Fragment {
    private ArrayList<StringFloatPair> portfolioList;
    private ListView listView;
    private PortfolioListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        setAdapter(view);
        return view;
    }

    private ArrayList<StringFloatPair> retrieveStoredPortfolio(){
        ArrayList<StringFloatPair> dataArrayList = new ArrayList<>();

        SharedPreferencesUtil util = new SharedPreferencesUtil(getActivity(), "my_portfolio");

        StringFloatPair[] retrievedPortfolio = util.getPortfolio();
        if (retrievedPortfolio != null) {
            for (StringFloatPair pair : retrievedPortfolio) {
                Float qtty = pair.qtty * Float.parseFloat(pythonGetPriceScrip.getPrice(pair.code));
                dataArrayList.add(new StringFloatPair(pair.code, qtty));
            }
        }
        Collections.sort(dataArrayList, new StringFloatPair.StringFloatPairComparator());

        return dataArrayList;
    }

    private void setAdapter(View view){
        this.portfolioList = retrieveStoredPortfolio();
        ArrayList<StringFloatPair> portfolioListCopy = new ArrayList<>(portfolioList); //Stocklist object can't be linked to the adapter because the search filter will edit it

        PortfolioListAdapter listAdapter = new PortfolioListAdapter(getActivity(), portfolioListCopy);
        this.listView = view.findViewById(R.id.portfolioListView);
        this.listView.setAdapter(listAdapter);
    }

    public void updateDisplayedPortfolio(String textFilter){
        ArrayList<StringFloatPair> results = new ArrayList<>();
        if(textFilter.isEmpty()){
            results = portfolioList;
        }else{
            for (StringFloatPair pair: portfolioList){
                if(pair.code.contains(textFilter.toUpperCase().trim())){
                    results.add(pair);
                }
            }
        }
        ((PortfolioListAdapter) listView.getAdapter()).updatePortfolioList(results);
    }
}