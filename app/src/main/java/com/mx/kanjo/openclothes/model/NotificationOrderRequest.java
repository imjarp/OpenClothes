package com.mx.kanjo.openclothes.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by JARP on 12/30/14.
 */
public class NotificationOrderRequest {

    public NotificationOrderRequest()
    {
        AvailableProducts = new ArrayList<>();
        UnavailableProducts = new ArrayList<>();
    }

    public Collection<StockItem> AvailableProducts;

    public Collection<StockItem> UnavailableProducts;

    public boolean isCompleteOrder()
    {
        if(UnavailableProducts.size()>0)
            return false;
        return true;
    }
}
