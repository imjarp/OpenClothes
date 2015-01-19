package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.ContentUris;
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

    private final String whereColumnIdStock = OpenClothesContract.Stock._ID + " = ?";

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

        String selection = OpenClothesContract.Stock.QUANTITY  + " > 0";

        //String [] selectionArgs ;

        Cursor cursor = resolver.query(OpenClothesContract.Stock.CONTENT_URI, null, selection , null, null);

        Set<StockItem> stockItems = new HashSet<>();

        int i = 0 ;

        try {

            if (!cursor.moveToFirst())
            {
                return  stockItems;
            }
            do
            {
                stockItems.add( StockItemCreator.fromCursor(cursor, resolver) );
            } while ( cursor.moveToNext() );

        } finally {
            cursor.close();

        }

        return stockItems;
    }

    public StockItem getStockItemByProductAndSize(int idProduct, int idSize)
    {
        return getStockItemByProduct(idProduct,idSize,resolver);
    }

    public void removeItemFromStock(StockItem itemToDelete)
    {
        //check how many pieces are left from that item
        StockItem stockItem = getStockItemByProduct(itemToDelete.getIdProduct(),itemToDelete.getSize().getIdSize(),resolver);

        //TODO : We have to remove all the pieces or update it?
        if(stockItem.getQuantity() == itemToDelete.getQuantity()){

           deleteStockItem(stockItem);
            return ;
        }

        int piecesLeft = stockItem.getQuantity() - itemToDelete.getQuantity();

        stockItem.setQuantity(piecesLeft);

        updateExistingItemInStock(stockItem);

    }

    /*
        This operation will not add an income
     */
    public void  addItemToStock(StockItem item)
    {
        //TODO:

        //check how many pieces are left from that item
        StockItem stockItem = getStockItemByProduct(item.getIdProduct(),item.getSize().getIdSize(),resolver);

        if(null == stockItem)
        {
            addNewItemToStock(item);
            return;
        }

        int piecesLeft = stockItem.getQuantity() + item.getQuantity();

        stockItem.setQuantity(piecesLeft);

        updateExistingItemInStock(stockItem);

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
            //TODO:Insert this Test in TestProvider
            Uri stockUriWithProductIdAndSizeId = OpenClothesContract.Stock.buildStockUriWithProductIdAndSizeId(idProduct, sizeId);

            cursor = resolver.query(stockUriWithProductIdAndSizeId, null, null, null, null);

            if(cursor.moveToFirst())
                stockItem = StockItemCreator.fromCursor(cursor, resolver);

        }
        finally {

            if( null != cursor && !cursor.isClosed() ) {
                cursor.close();
            }
        }

        return stockItem;

    }

    private void updateExistingItemInStock(StockItem stockItem)
    {
        Uri uriStockItem = OpenClothesContract.Stock.buildStockUri(stockItem.getStockItemId());

        ContentValues values = StockItemCreator.getFromStockModel(stockItem);

        int rows = resolver.update(uriStockItem, values, whereColumnIdStock, new String[]{ String.valueOf(stockItem.getStockItemId())});
    }

    private void addNewItemToStock(StockItem stockItem)
    {

        ContentValues values = StockItemCreator.getFromStockModel(stockItem);

        Uri result = resolver.insert(OpenClothesContract.Stock.CONTENT_URI, values);

        long id = ContentUris.parseId(result);

            /*if(id<0)
                throw Exception()*/

    }

    private void deleteStockItem(StockItem stockItem)
    {
        Uri uriStockItem = OpenClothesContract.Stock.buildStockUri(stockItem.getStockItemId());

        int rows = resolver.delete(uriStockItem, whereColumnIdStock , new String[]{ String.valueOf(stockItem.getStockItemId())});

            /*if(rows<0)
                throw Exception();*/
    }


}


