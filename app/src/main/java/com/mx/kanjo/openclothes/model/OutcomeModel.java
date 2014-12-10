package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class OutcomeModel {

    StockItem outgoingItem;
    int quantity;
    OutcomeType incomeType;
    String date;

    public OutcomeModel(StockItem outgoingItem, int quantity, OutcomeType incomeType, String date) {
        this.outgoingItem = outgoingItem;
        this.quantity = quantity;
        this.incomeType = incomeType;
        this.date = date;
    }

    public OutcomeModel() {
    }

    public StockItem getOutgoingItem() {
        return outgoingItem;
    }

    public void setOutgoingItem(StockItem outgoingItem) {
        this.outgoingItem = outgoingItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OutcomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(OutcomeType incomeType) {
        this.incomeType = incomeType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}