package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class IncomeType
{
    int idIncome;
    String description;

    public IncomeType(int idIncome, String description) {
        this.idIncome = idIncome;
        this.description = description;
    }

    public IncomeType() {
    }

    public int getIdIncome() {
        return idIncome;
    }

    public void setIdIncome(int idIncome) {
        this.idIncome = idIncome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}