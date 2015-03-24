package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentValues;
import android.database.Cursor;

import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/31/14.
 */
public class SaleHeaderCreator {

    public static ContentValues createSaleModel ( SaleModel saleModel)
    {
        ContentValues values = new ContentValues();
        if( saleModel.getId() > 0 )
            values.put(OpenClothesContract.Sale._ID,saleModel.getId());
        values.put(OpenClothesContract.Sale.DATE,saleModel.getDate());
        values.put(OpenClothesContract.Sale.TOTAL, PromiseHeaderCreator.getTotal(saleModel.getSaleItems()));
        values.put(OpenClothesContract.Sale.CUSTOMER, saleModel.getCustomer());
        return values;
    }

    public static SaleModel createSaleHeaderFromCursor(Cursor cursor)
    {
        SaleModel saleModel = new SaleModel() ;

        int idx = cursor.getColumnIndex(OpenClothesContract.Sale._ID);
        int idxDate = cursor.getColumnIndex(OpenClothesContract.Sale.DATE);
        int idxTotal = cursor.getColumnIndex(OpenClothesContract.Sale.TOTAL);
        int idxCustomer = cursor.getColumnIndex(OpenClothesContract.Sale.CUSTOMER);


        int id = cursor.getInt(idx);
        String date = cursor.getString(idxDate);
        int total = cursor.getInt(idxTotal);
        String customer = cursor.getString(idxCustomer);

        saleModel.setId(id);
        saleModel.setDate(date);
        saleModel.setTotal(idxTotal);
        saleModel.setCustomer(customer);

        return saleModel;
    }






}
