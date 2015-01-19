package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.mx.kanjo.openclothes.model.IncomeModel;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/15/14.
 */
public class IncomeCreator {

    public static ContentValues getFromIncomeModel(IncomeModel incomeModel)
    {
        ContentValues values = new ContentValues();

        if(incomeModel.getIdIncomeModel() > 0)
            values.put(OpenClothesContract.Income._ID, incomeModel.getIdIncomeModel());

        values.put(OpenClothesContract.Income.ID_INCOME_TYPE, incomeModel.getIncomeType().getIdIncome());
        values.put(OpenClothesContract.Income.ID_PRODUCT, incomeModel.getIdProduct());
        values.put(OpenClothesContract.Income.ID_SIZE, incomeModel.getSize().getIdSize());
        values.put(OpenClothesContract.Income.QUANTITY, incomeModel.getQuantity());
        values.put(OpenClothesContract.Income.DATE, incomeModel.getDateOperation());

        return values;

    }

    public static IncomeModel getFromCursor(Cursor cursor, ContentResolver resolver)
    {

        int idxId = cursor.getColumnIndex(OpenClothesContract.Income._ID);
        int idxIncomeType = cursor.getColumnIndex(OpenClothesContract.Income.ID_INCOME_TYPE);
        int idxProduct = cursor.getColumnIndex(OpenClothesContract.Income.ID_PRODUCT);
        int idxSize = cursor.getColumnIndex(OpenClothesContract.Income.ID_SIZE);
        int idxQuantity = cursor.getColumnIndex(OpenClothesContract.Income.QUANTITY);
        int idxDate = cursor.getColumnIndex(OpenClothesContract.Income.DATE);


        int id = cursor.getInt(idxId);
        int idIncomeType = cursor.getInt(idxIncomeType);
        int idProduct = cursor.getInt(idxProduct);
        int idSize = cursor.getInt(idxSize);
        int quantity = cursor.getInt(idxQuantity);
        String date = cursor.getString(idxDate);


        IncomeType incomeType = IncomeTypeCreator.getModelFromId(idIncomeType,resolver);

        ProductModel productModel = ProductCreator.getProductModelFromId(idProduct,resolver);

        SizeModel sizeModel = SizeCreator.getFromId(idSize,resolver);

        IncomeModel incomeModel = new IncomeModel(id,productModel,sizeModel,quantity,incomeType,date);

        return incomeModel;
    }


}
