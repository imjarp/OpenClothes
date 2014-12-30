package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.model.ConfigurationOrder;
import com.mx.kanjo.openclothes.model.NotificationOrderRequest;
import com.mx.kanjo.openclothes.model.PromiseSale;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.Map;
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

    public void createNewSale(SaleModel sale, ConfigurationOrder configurationOrder)
    {
        createNewSale(sale, resolver, configurationOrder);
    }

    public NotificationOrderRequest createPromise(PromiseSale promiseSale, ConfigurationOrder configurationOrder)
    {
        return createPromise(promiseSale, resolver, configurationOrder);
    }

    public void convertPromiseToSale(PromiseSale promiseSale)
    {

    }

    public void modifyPromise(int idPromiseSale, final Set<StockItem> itemsToDelete, final Set<StockItem> itemsToUpdate)
    {

    }

    private static NotificationOrderRequest createNewSale(SaleModel sale, ContentResolver resolver, ConfigurationOrder configurationOrder)
    {
        //Check the inventory
        NotificationOrderRequest notificationOrderRequest= null;
        return notificationOrderRequest;

    }


    private static NotificationOrderRequest createPromise(PromiseSale promise, ContentResolver resolver, ConfigurationOrder configurationOrder)
    {
        NotificationOrderRequest result = verifyIsCompleteOrder(promise.getStockItems(),resolver);

        if(!configurationOrder.TransactIncompleteOrder)
            return result;

        return result;
    }

    private static NotificationOrderRequest verifyIsCompleteOrder( Map<Integer, StockItem> promiseItems, ContentResolver resolver )
    {
        NotificationOrderRequest result = new NotificationOrderRequest();
        Uri uriItem;
        Cursor cursor;

        for(Map.Entry<Integer, StockItem> item : promiseItems.entrySet())
        {
            uriItem = OpenClothesContract.Stock.buildStockUriWithProductIdAndSizeId(item.getValue().getIdProduct(),
                    item.getValue().getSize().getIdSize());
            cursor = resolver.query(uriItem,null,null,null,null);

            if(cursor.getCount()==0) {
                result.UnavailableProducts.add(item.getValue());
                continue;
            }

            result.AvailableProducts.add(item.getValue());
        }

        return result;

    }

    private static void createHeaderPromise(PromiseSale promiseSale, ContentResolver resolver)
    {


    }
}
