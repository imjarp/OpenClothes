package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/29/14.
 */
public class StockItemCreator {

    public static ContentValues getFromStockModel(StockItem model)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Stock._ID,model.getStockItemId());
        values.put(OpenClothesContract.Stock.ID_PRODUCT, model.getIdProduct());
        values.put(OpenClothesContract.Stock.ID_SIZE, model.getSize().getIdSize());
        values.put(OpenClothesContract.Stock.QUANTITY, model.getQuantity());

        return values;

    }

    public static StockItem fromCursor(Cursor cursor, ContentResolver resolver)
    {
        int idxId = cursor.getColumnIndex(OpenClothesContract.Stock._ID);
        int idxProduct = cursor.getColumnIndex(OpenClothesContract.Stock.ID_PRODUCT);
        int idxSize = cursor.getColumnIndex(OpenClothesContract.Stock.ID_SIZE);
        int idxQuantity = cursor.getColumnIndex(OpenClothesContract.Stock.QUANTITY);


        int id = cursor.getInt(idxId);
        int idProduct = cursor.getInt(idxProduct);
        int idSize = cursor.getInt(idxSize);
        int quantity = cursor.getInt(idxQuantity);

        ProductModel productModel = ProductCreator.getProductModelFromId(idProduct,resolver);

        SizeModel sizeModel = SizeCreator.getFromId(idSize,resolver);

        StockItem model = new StockItem(productModel,sizeModel,quantity);

        model.setStockItemId(id);

        return  model;

    }
}
