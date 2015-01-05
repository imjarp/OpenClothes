package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 12/9/14.
 */
public class OutcomeType
{
    int idOutcome;

    String description;

    public OutcomeType(int idOutcome, String description) {
        this.idOutcome = idOutcome;
        this.description = description;
    }

    public OutcomeType() {
    }

    public int getIdOutcome() {
        return idOutcome;
    }

    public void setIdOutcome(int idOutcome) {
        this.idOutcome = idOutcome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}