package com.victorfigma.fintrack;

public class PortfolioListData {
    String stock, price;

    public PortfolioListData(String stock, int price){
        this.stock = stock;
        this.price = String.valueOf(price);
    }
}
