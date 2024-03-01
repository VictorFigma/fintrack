package com.victorfigma.fintrack;

import java.util.Comparator;

public class PortfolioListData {
    String stock, totalValue;

    public PortfolioListData(String stock, int totalValue){
        this.stock = stock;
        this.totalValue = String.valueOf(totalValue);
    }

    public static class PortfolioComparator implements Comparator<PortfolioListData> {
        @Override
        public int compare(PortfolioListData o1, PortfolioListData o2) {
            return o1.stock.compareTo(o2.stock);
        }
    }
}
