package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 20/01/2015.
 */
public class OutcomeTypeSale  {

    public final static int idOutcomeSale = 0 ;

    public final static String descriptionSaleOutcome = "Sale" ;

    private OutcomeTypeSale(){}

    public static OutcomeType OutcomeTypeSale()
    {
        return  new OutcomeType(idOutcomeSale,descriptionSaleOutcome);
    }




}
