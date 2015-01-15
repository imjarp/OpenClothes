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
        if( size.getIdSize() > 0 )
            values.put(OpenClothesContract.Size._ID,size.getIdSize());
        values.put(OpenClothesContract.Size.SIZE,size.getSizeDescription());
        return values;
    }

    public static SizeModel getFromCursor (Cursor cursor)
    {
        SizeModel sizeModel = null;

        int idxId = cursor.getColumnIndex(OpenClothesContract.Size._ID);
        int idxSizeDescription = cursor.getColumnIndex(OpenClothesContract.Size.SIZE);

        int id = cursor.getInt(idxId);
        String description = cursor.getString(idxSizeDescription);

        sizeModel = new SizeModel(id,description);

        return sizeModel;
    }

    public static SizeModel getFromId(int idSize , ContentResolver resolver)
    {
        SizeModel model = null;
        Cursor cursor = null;
        try {
            Uri uriSizeId = OpenClothesContract.Size.buildSizeUri(idSize);
            cursor = resolver.query(uriSizeId, null, null, null, null);
            if( cursor.moveToFirst() )
                model = getFromCursor(cursor);
        }
        finally {
            cursor.close();
        }

        return model;
    }
}
