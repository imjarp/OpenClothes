package com.mx.kanjo.openclothes.model;

import java.util.Map;

/**
 * Created by JARP on 12/9/14.
 */
public class SaleModel
{
    Map<Integer,StockItem> saleItems;
    String date;

    String customer;
    int total;
    int id;

    public SaleModel(Map<Integer, StockItem> saleItems, String date, int total, int id, String customer) {
        this.saleItems = saleItems;
        this.date = date;
        this.total = total;
        this.id = id;
        this.customer = customer;
    }

    public SaleModel() {
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, StockItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(Map<Integer, StockItem> saleItems) {
        this.saleItems = saleItems;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}