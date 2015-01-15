package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/15/14.
 */
public class OutcomeTypeCreator {

    public static ContentValues getOutcomeType(OutcomeType outcomeTypeModel)
    {
        ContentValues values = new ContentValues();

        if( outcomeTypeModel.getIdOutcome() > 0 )
            values.put(OpenClothesContract.IncomeType._ID, outcomeTypeModel.getIdOutcome());
        values.put(OpenClothesContract.IncomeType.DESCRIPTION, outcomeTypeModel.getDescription());

        return values;
    }

    public static OutcomeType getModel(Cursor cursor)
    {
        int idxId = cursor.getColumnIndex(OpenClothesContract.OutcomeType._ID);
        int idxDescription = cursor.getColumnIndex(OpenClothesContract.OutcomeType.DESCRIPTION);

        OutcomeType outcomeTypeModel = new OutcomeType();

        outcomeTypeModel.setIdOutcome(cursor.getInt(idxId));
        outcomeTypeModel.setDescription(cursor.getString(idxDescription));

        return outcomeTypeModel;

    }

    public static OutcomeType getModelFromId(int id, ContentResolver resolver)
    {
        Uri uriOutcomeType = OpenClothesContract.OutcomeType.buildOutcomeTypeUri(id);
        Cursor cursor = resolver.query(uriOutcomeType,null,null,null,null);
        OutcomeType model = getModel(cursor);
        cursor.close();
        return  model;
    }
}
