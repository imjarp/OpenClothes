package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentValues;

import com.mx.kanjo.openclothes.model.PromiseSale;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.Map;

/**
 * Created by JARP on 12/30/14.
 */
public class PromiseHeaderCreator {

    public static ContentValues createPromiseHeader(PromiseSale promiseSale)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Promise.DATE, promiseSale.getDatePromise());
        values.put(OpenClothesContract.Promise.TOTAL, getTotal(promiseSale.getStockItems()));
        values.put(OpenClothesContract.Promise.CUSTOMER, promiseSale.getCustomer());
        return values;
    }



    private static int getTotal(Map<Integer,StockItem> items)
    {
        int total = 0;
        for(Map.Entry<Integer,StockItem> item : items.entrySet())
        {
            total += item.getValue().getPrice();
        }
        return total;
    }
}
