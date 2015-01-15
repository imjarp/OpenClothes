package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/15/14.
 */
public class IncomeTypeCreator {

    public static ContentValues getIncomeType(IncomeType incomeTypeModel)
    {
        ContentValues values = new ContentValues();

        if(incomeTypeModel.getIdIncome()>0)
            values.put(OpenClothesContract.IncomeType._ID,incomeTypeModel.getIdIncome());

        values.put(OpenClothesContract.IncomeType.DESCRIPTION,incomeTypeModel.getDescription());

        return values;
    }

    public static IncomeType getModel(Cursor cursor)
    {
        int idxId = cursor.getColumnIndex(OpenClothesContract.IncomeType._ID);
        int idxDescription = cursor.getColumnIndex(OpenClothesContract.IncomeType.DESCRIPTION);

        IncomeType incomeTypeModel = new IncomeType();

        incomeTypeModel.setIdIncome(cursor.getInt(idxId));
        incomeTypeModel.setDescription(cursor.getString(idxDescription));

        return incomeTypeModel;

    }

    public static IncomeType getModelFromId(int id, ContentResolver resolver)
    {
        Uri uriIncomeType = OpenClothesContract.IncomeType.buildIncomeTypeUri(id);
        Cursor cursor = resolver.query(uriIncomeType,null,null,null,null);
        IncomeType model = getModel(cursor);
        cursor.close();
        return  model;
    }
}
