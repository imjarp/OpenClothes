package com.mx.kanjo.openclothes.model;

/**
 * Created by JARP on 20/01/2015.
 */
public class IncomeTypePurchase {

    public final static int idIncomePurchase = 0 ;

    public final static String descriptionPurchaseIncoming = "Purchase" ;

    private IncomeTypePurchase(){}

    public static IncomeType IncomeTypePurchase()
    {
        return  new IncomeType(idIncomePurchase, descriptionPurchaseIncoming);
    }




}
