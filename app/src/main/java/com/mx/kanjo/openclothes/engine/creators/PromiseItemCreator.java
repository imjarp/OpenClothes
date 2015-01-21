package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/30/14.
 */
public class PromiseItemCreator {

    public static ContentValues createPromiseItem(StockItem item, int idPromise)
    {
        ContentValues values = new ContentValues();

        if(item.getStockItemId() > 0)
            values.put(OpenClothesContract.PromiseItem._ID, item.getStockItemId());

        values.put(OpenClothesContract.PromiseItem.ID_PRODUCT, item.getIdProduct());
        values.put(OpenClothesContract.PromiseItem.ID_PROMISE, idPromise);
        values.put(OpenClothesContract.PromiseItem.ID_SIZE, item.getSize().getIdSize());
        values.put(OpenClothesContract.PromiseItem.QUANTITY, item.getQuantity());


        return values;
    }

    public static StockItem createPromiseItemFromCursor(Cursor cursor, ContentResolver resolver)
    {
        return StockItemCreator.fromCursor(cursor, resolver);
    }

    public static StockItem createPromiseItemFromIdPromise(Cursor cursor,ContentResolver resolver)
    {
        int id = cursor.getColumnIndex(OpenClothesContract.PromiseItem._ID);
        int idxProduct = cursor.getColumnIndex(OpenClothesContract.PromiseItem.ID_PRODUCT);
        int idxPromise = cursor.getColumnIndex(OpenClothesContract.PromiseItem.ID_PROMISE);
        int idxSize = cursor.getColumnIndex(OpenClothesContract.PromiseItem.ID_SIZE);
        int idxQuantity = cursor.getColumnIndex(OpenClothesContract.PromiseItem.QUANTITY);

        int idProduct = cursor.getInt(idxProduct);
        int idPromise = cursor.getInt(idxPromise);
        int idSize = cursor.getInt(idxSize);
        int quantity = cursor.getInt(idxQuantity);

        ProductModel productModel =  ProductCreator.getProductModelFromId(idProduct, resolver);

        SizeModel sizeModel = SizeCreator.getFromId(idSize, resolver);

        return  new StockItem(productModel,sizeModel,quantity);

    }

}
