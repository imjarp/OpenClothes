package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class IncomeModel extends StockItem {

    IncomeType incomeType;
    String dateOperation;

    public IncomeModel() {
    }

    public IncomeModel(ProductModel product, SizeModel size, int quantity, IncomeType incomeType, String dateOperation) {
        super(product, size, quantity);
        this.incomeType = incomeType;
        this.dateOperation = dateOperation;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }




}