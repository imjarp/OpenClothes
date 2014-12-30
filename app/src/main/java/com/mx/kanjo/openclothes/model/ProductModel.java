package com.mx.kanjo.openclothes.model;

import android.net.Uri;

/**
 * Created by JARP on 12/9/14.
 */
public class ProductModel
{
    private int idProduct;
    private String name;
    private String description;
    private String model;
    private String date;
    private Uri imagePath;
    private boolean isActive;
    private int price;
    private int cost;


    public ProductModel() {
    }

    public ProductModel(int idProduct, String name, String description, String model, String date, Uri imagePath, boolean isActive, int price, int cost) {
        this.idProduct = idProduct;
        this.name = name;
        this.description = description;
        this.model = model;
        this.date = date;
        this.imagePath = imagePath;
        this.isActive = isActive;
        this.price = price;
        this.cost = cost;
    }


    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public void setImagePath(Uri imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDateOperation() {
        return date;
    }

    public void setDateOperation(String date) {
        this.date = date;
    }

}