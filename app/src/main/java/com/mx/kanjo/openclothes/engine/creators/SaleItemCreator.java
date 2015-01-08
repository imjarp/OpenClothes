package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/31/14.
 */
public class SaleItemCreator {

    public static ContentValues createSaleModel ( StockItem item, int idSale)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.SaleItem.ID_PRODUCT, item.getIdProduct());
        values.put(OpenClothesContract.SaleItem.SALE_ID, idSale);
        values.put(OpenClothesContract.SaleItem.ID_SIZE, item.getSize().getIdSize());
        values.put(OpenClothesContract.SaleItem.QUANTITY, item.getQuantity());

        return values;
    }

    public static StockItem createSaleItemFromCursor(Cursor cursor,ContentResolver resolver)
    {
        int id = cursor.getColumnIndex(OpenClothesContract.SaleItem._ID);
        int idxProduct = cursor.getColumnIndex(OpenClothesContract.SaleItem.ID_PRODUCT);
        int idxPromise = cursor.getColumnIndex(OpenClothesContract.SaleItem.SALE_ID);
        int idxSize = cursor.getColumnIndex(OpenClothesContract.SaleItem.ID_SIZE);
        int idxQuantity = cursor.getColumnIndex(OpenClothesContract.SaleItem.QUANTITY);

        int idProduct = cursor.getInt(idxProduct);
        int idSale = cursor.getInt(idxPromise);
        int idSize = cursor.getInt(idxSize);
        int quantity = cursor.getInt(idxQuantity);

        ProductModel productModel =  ProductCreator.getProductModelFromId(idProduct, resolver);

        SizeModel sizeModel = SizeCreator.getFromId(idSize, resolver);

        return  new StockItem(productModel,sizeModel,quantity);

    }


}