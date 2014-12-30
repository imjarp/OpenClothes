package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.Context;

import com.mx.kanjo.openclothes.model.NotificationOrderRequest;
import com.mx.kanjo.openclothes.model.PromiseSale;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.model.StockItem;

import java.util.Set;

/**
 * Created by JARP on 12/10/14.
 */
public class SalesManager {

    private Context mContext;
    private ContentResolver resolver;
    private InventoryManager inventoryManager;

    public SalesManager(Context context)
    {
        if(context == null) {
            throw new IllegalArgumentException("context");
        }

        mContext = context;
        resolver = context.getContentResolver();
        inventoryManager = new InventoryManager(context);
    }

    public void createNewSale(SaleModel sale)
    {
        createNewSale(sale,resolver);
    }

    public void createPromiseSale(PromiseSale promiseSale)
    {

    }

    public void setPromiseSaleToSale(int idPromiseSale)
    {

    }

    public void modifyPromiseSale(int idPromiseSale, final Set<StockItem> itemsToDelete, final Set<StockItem> itemsToUpdate )
    {

    }

    private static NotificationOrderRequest createNewSale(SaleModel sale, ContentResolver resolver)
    {
        //Check the inventory
        NotificationOrderRequest notificationOrderRequest= null;
        return notificationOrderRequest;

    }
}
