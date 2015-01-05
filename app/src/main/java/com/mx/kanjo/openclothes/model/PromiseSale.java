package com.mx.kanjo.openclothes.model;

import java.util.Map;

/**
 * Created by JARP on 12/9/14.
 */
public class PromiseSale
{
    Map<Integer,StockItem> stockItems;
    String customer;
    String datePromise;
    private int id;

    public PromiseSale() {
    }

    public PromiseSale(Map<Integer, StockItem> stockItems, String customer, String datePromise, int id) {
        this.stockItems = stockItems;
        this.customer = customer;
        this.datePromise = datePromise;
        this.id = id;
    }

    public Map<Integer, StockItem> getStockItems() {
        return stockItems;
    }

    public void setStockItems(Map<Integer, StockItem> stockItems) {
        this.stockItems = stockItems;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDatePromise() {
        return datePromise;
    }

    public void setDatePromise(String datePromise) {
        this.datePromise = datePromise;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}