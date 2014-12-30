package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

/**
 * Created by JARP on 12/16/14.
 */
public class SizeCreator {

    public static ContentValues getFromModel(SizeModel size)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Size._ID,size.getIdSize());
        values.put(OpenClothesContract.Size.SIZE,size.getSizeDescription());
        return values;
    }

    public static SizeModel getFromCursor (Cursor cursor)
    {
        SizeModel sizeModel = new SizeModel();
        return sizeModel;
    }

    public static SizeModel getFromId(int idSize , ContentResolver resolver)
    {
        Uri uriSizeId = OpenClothesContract.Size.buildSizeUri(idSize);
        Cursor cursor = resolver.query(uriSizeId,null,null,null,null);
        SizeModel model = getFromCursor(cursor);
        cursor.close();
        return model;
    }
}
