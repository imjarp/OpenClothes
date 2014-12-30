package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.mx.kanjo.openclothes.model.OutcomeModel;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/15/14.
 */
public class OutcomeCreator {

    public static ContentValues getFromOutcomeModel(OutcomeModel outcomeModel)
    {
        ContentValues values = new ContentValues();

        values.put(OpenClothesContract.Income.ID_INCOME_TYPE,outcomeModel.getOutcomeType().getIdOutcome());
        values.put(OpenClothesContract.Income.ID_PRODUCT, outcomeModel.getIdProduct());
        values.put(OpenClothesContract.Income.ID_SIZE,outcomeModel.getSize().getIdSize());
        values.put(OpenClothesContract.Income.QUANTITY, outcomeModel.getQuantity());
        values.put(OpenClothesContract.Income.DATE, outcomeModel.getDateOperation());

        return values;

    }

    public  static OutcomeModel getFromCursor(Cursor cursor, ContentResolver resolver)
    {

        int idxId = cursor.getColumnIndex(OpenClothesContract.Outcome._ID);
        int idxOutcomeType = cursor.getColumnIndex(OpenClothesContract.Outcome.ID_OUTCOME_TYPE);
        int idxProduct = cursor.getColumnIndex(OpenClothesContract.Outcome.ID_PRODUCT);
        int idxSize = cursor.getColumnIndex(OpenClothesContract.Outcome.ID_SIZE);
        int idxQuantity = cursor.getColumnIndex(OpenClothesContract.Outcome.QUANTITY);
        int idxDate = cursor.getColumnIndex(OpenClothesContract.Outcome.DATE);

        int id = cursor.getInt(idxId);
        int idOutcomeType = cursor.getInt(idxOutcomeType);
        int idProduct = cursor.getInt(idxProduct);
        int idSize = cursor.getInt(idxSize);
        int quantity = cursor.getInt(idxQuantity);
        String date = cursor.getString(idxDate);

        OutcomeType outcomeType = OutcomeTypeCreator.getModelFromId(idOutcomeType,resolver);

        ProductModel productModel = ProductCreator.getProductModelFromId(idProduct,resolver);

        SizeModel sizeModel = SizeCreator.getFromId(idSize,resolver);

        OutcomeModel model = new OutcomeModel(productModel,sizeModel,quantity,outcomeType,date);

        return model;

    }
}
