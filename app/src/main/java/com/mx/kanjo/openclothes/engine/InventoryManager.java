package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.engine.creators.IncomeCreator;
import com.mx.kanjo.openclothes.engine.creators.OutcomeCreator;
import com.mx.kanjo.openclothes.engine.creators.StockItemCreator;
import com.mx.kanjo.openclothes.model.IncomeModel;
import com.mx.kanjo.openclothes.model.OutcomeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JARP on 12/10/14.
 */
public class InventoryManager {

    private Context mContext;

    private ContentResolver resolver;

    public InventoryManager(Context context)
    {
        if(context == null) {
            throw new IllegalArgumentException("context");
        }

        mContext = context;
        resolver = context.getContentResolver();

    }

    public void addIncome(IncomeModel income) {

        addIncome(income,resolver);

    }

    public void addOutcome(OutcomeModel outcome){

        addOutcome(outcome,resolver);
    }

    public Set<StockItem> getStock()
    {
        final Cursor cursor = resolver.query(OpenClothesContract.Stock.CONTENT_URI,null,null,null,null);

        Set<StockItem> stockItems = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  stockItems;
            }
            while (cursor.moveToNext())
            {
                stockItems.add(StockItemCreator.fromCursor(cursor,resolver));
            }

        } finally {
            cursor.close();

        }

        return stockItems;
    }

    public StockItem getStockItemByProductAndSize(int idProduct, int idSize)
    {
        return getStockItemByProduct(idProduct,idSize,resolver);
    }

    public void removeItemFromStock(StockItem item)
    {
        //TODO:
    }

    public void  addItemToStock(StockItem item)
    {
        //TODO:
    }

    private static void addIncome(IncomeModel income, ContentResolver resolver)
    {
        ContentValues values = IncomeCreator.getFromIncomeModel(income);
        resolver.insert(OpenClothesContract.Income.CONTENT_URI,values);
    }

    private static void addOutcome(OutcomeModel outcome, ContentResolver resolver)
    {
        ContentValues values = OutcomeCreator.getFromOutcomeModel(outcome);
        resolver.insert(OpenClothesContract.Outcome.CONTENT_URI,values);
    }

    private static StockItem getStockItemByProduct(int idProduct, int sizeId, ContentResolver resolver)
    {

        StockItem stockItem = null;

        Cursor cursor = null;

        try
        {
            //TODO:Test this URI
            Uri stockUriWithProductIdAndSizeId = OpenClothesContract.Stock.buildStockUriWithProductIdAndSizeId(idProduct,sizeId);

            cursor = resolver.query(stockUriWithProductIdAndSizeId,null,null,null,null);

            if(cursor.moveToFirst())
                stockItem = StockItemCreator.fromCursor(cursor,resolver);

        }
        finally {

            if(null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return stockItem;

    }


}


