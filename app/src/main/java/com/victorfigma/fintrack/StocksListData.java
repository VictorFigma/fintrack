package com.victorfigma.fintrack;

import java.util.Comparator;

public class StocksListData {
    protected String stock, price;

    public StocksListData(String stock, int price) {
        this.stock = stock;
        this.price = String.valueOf(price);
    }

    public static class StockComparator implements Comparator<StocksListData> {
        @Override
        public int compare(StocksListData o1, StocksListData o2) {
            return o1.stock.compareTo(o2.stock);
        }
    }
}
