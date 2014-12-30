package com.mx.kanjo.openclothes.model;

import java.util.Collection;

/**
 * Created by JARP on 12/30/14.
 */
public class NotificationOrderRequest {

    public Collection<StockItem> AvailableProducts;

    public Collection<StockItem> UnavailabeProducts;

    public boolean isCompleteOrder()
    {
        if(UnavailabeProducts.size()>0)
            return false;
        return true;
    }
}
