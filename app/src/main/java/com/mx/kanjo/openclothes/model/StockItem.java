package com.mx.kanjo.openclothes.model;

import android.net.Uri;

/**
 * Created by JARP on 12/9/14.
 */
public class StockItem extends ProductModel
{
    int quantity;

    public StockItem(int idProduct, String name, String description, Uri imagePath, boolean isActive, int price, int cost, int quantity) {
        super(idProduct, name, description, imagePath, isActive, price, cost);
        this.quantity = quantity;
    }

    public StockItem() {
    }

    public StockItem(ProductModel product, int quantity) {
        super(product.getIdProduct(), product.getName(), product.getDescription(), product.getImagePath(),
              product.isActive(),product.getPrice(),product.getCost());
        this.quantity = quantity;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}