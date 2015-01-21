package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class OutcomeModel extends StockItem {


    int idOutcomeModel;
    OutcomeType outcomeType;
    String dateOperation;



    public OutcomeModel() {
    }

    public OutcomeModel(int idOutcomeModel, ProductModel product, SizeModel size, int quantity, OutcomeType outcomeType, String dateOperation) {
        super(product, size, quantity);
        this.outcomeType = outcomeType;
        this.dateOperation = dateOperation;
        this.idOutcomeModel = idOutcomeModel;
    }

    public int getIdOutcomeModel() {return idOutcomeModel;}

    public void setIdOutcomeModel(int idOutcomeModel) {this.idOutcomeModel = idOutcomeModel;}


    public OutcomeType getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(OutcomeType outcomeType) {
        this.outcomeType = outcomeType;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }
}