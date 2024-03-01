package com.victorfigma.fintrack;

public class StocksListData {
    String stock, price;

    public StocksListData(String stock, int price){
        this.stock = stock;
        this.price = String.valueOf(price);
    }
}
