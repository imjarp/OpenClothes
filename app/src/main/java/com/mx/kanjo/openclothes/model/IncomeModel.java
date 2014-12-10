package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class IncomeModel {

    StockItem incomingItem;
    int quantity;
    IncomeType incomeType;
    String date;

    public IncomeModel(StockItem incomingItem, int quantity, IncomeType incomeType, String date) {
        this.incomingItem = incomingItem;
        this.quantity = quantity;
        this.incomeType = incomeType;
        this.date = date;
    }

    public IncomeModel() {
    }

    public StockItem getIncomingItem() {
        return incomingItem;
    }

    public void setIncomingItem(StockItem incomingItem) {
        this.incomingItem = incomingItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}