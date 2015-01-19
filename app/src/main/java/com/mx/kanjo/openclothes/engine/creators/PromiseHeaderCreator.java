package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

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
        if( promiseSale.getId() > 0 )
            values.put(OpenClothesContract.Promise._ID, promiseSale.getId());

        values.put(OpenClothesContract.Promise.DATE, promiseSale.getDatePromise());
        values.put(OpenClothesContract.Promise.TOTAL, getTotal(promiseSale.getStockItems()));
        values.put(OpenClothesContract.Promise.CUSTOMER, promiseSale.getCustomer());
        return values;
    }

    public static PromiseSale createPromiseHeaderFromCursor(Cursor cursor)
    {
        PromiseSale promiseSale = new PromiseSale() ;

        int idx = cursor.getColumnIndex(OpenClothesContract.Promise._ID);
        int idxDate = cursor.getColumnIndex(OpenClothesContract.Promise.DATE);
        int idxTotal = cursor.getColumnIndex(OpenClothesContract.Promise.TOTAL);
        int idxCustomer = cursor.getColumnIndex(OpenClothesContract.Promise.CUSTOMER);


        int id = cursor.getInt(idx);
        String date = cursor.getString(idxDate);
        int total = cursor.getInt(idxTotal);
        String customer = cursor.getString(idxCustomer);

        promiseSale.setId(id);
        promiseSale.setCustomer(customer);
        promiseSale.setDatePromise(date);


        return promiseSale;
    }


    public static int getTotal(Map<Integer,StockItem> items)
    {
        int total = 0;
        for(Map.Entry<Integer,StockItem> item : items.entrySet())
        {
            total += item.getValue().getPrice();
        }
        return total;
    }
}
