package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class StockItem extends ProductModel
{
    int quantity;

    SizeModel size;

    public int getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(int stockItemId) {
        this.stockItemId = stockItemId;
    }

    int stockItemId;


    public StockItem() {
    }

    public StockItem(ProductModel product, SizeModel size, int quantity) {

        super(product.getIdProduct(), product.getName(), product.getDescription(), product.getModel(),
              product.getDateOperation(), product.getImagePath(), product.isActive(), product.getPrice(), product.getCost());
        this.quantity = quantity;
        this.size = size;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SizeModel getSize() {
        return size;
    }

    public void setSize(SizeModel size) {
        this.size = size;
    }

}