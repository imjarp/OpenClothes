package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.engine.creators.IncomeTypeCreator;
import com.mx.kanjo.openclothes.engine.creators.OutcomeTypeCreator;
import com.mx.kanjo.openclothes.engine.creators.SizeCreator;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.util.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JARP on 12/10/14.
 */
public class ConfigurationInventoryManager {

    private  Context mContext;

    private ContentResolver resolver;

    public ConfigurationInventoryManager(Context context)
    {
        if(context == null) {
            throw new IllegalArgumentException("context");
        }

        mContext = context;
        resolver = context.getContentResolver();
    }

    public IncomeType addIncomeType(IncomeType incomeType)
    {
        //TODO: search for and income description
        return insertIncomeType(resolver,incomeType);
    }

    public OutcomeType addOutcomeType(OutcomeType outcomeType)
    {
        //TODO: search for and outcome description
        return insertOutcomeType(resolver,outcomeType);
    }

    public IncomeType findIncomeTypeByDescription(String description)
    {
        return findIncomeTypeByDescription(description,resolver);
    }

    public OutcomeType findOutcomeTypeByDescription(String description)
    {
        return findOutcomeTypeByDescription(description,resolver);
    }

    public void modifySize(int idSize, String sizeDescription )
    {
        updateSize(resolver, new SizeModel(idSize, sizeDescription));
    }



    public void modifyIncomeType(int idIncomeType, String incomeDescription )
    {
        updateIncomeType(resolver, new IncomeType(idIncomeType,incomeDescription));
    }

    public void modifyOutcomeType(int idOutcomeType, String outcomeDescription )
    {
        updateOutcomeType(resolver, new OutcomeType(idOutcomeType, outcomeDescription));
    }

    public SizeModel findSizeByDescription(String sizeDescription)
    {
        return findByDescription(sizeDescription,resolver);
    }

    public Set<IncomeType> getIncomeTypes()
    {
        return getIncomesTypesFromResolver(resolver);
    }

    public Set<OutcomeType> getOutcomeTypes()
    {
        return getOutcomeTypesFromResolver(resolver);
    }

    private Set<IncomeType> getIncomesTypesFromResolver(ContentResolver resolver) {

        final Cursor cursor = resolver.query(OpenClothesContract.IncomeType.CONTENT_URI,null,null,null,null);

        Set<IncomeType> incomes = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  incomes;
            }
            do
            {
                incomes.add(IncomeTypeCreator.getModel(cursor));
            }while (cursor.moveToNext());

        } finally {
            cursor.close();

        }

        return incomes;

    }

    public SizeModel addSizeItem(SizeModel sizeModel)
    {
        SizeModel sizeItem =  findByDescription(sizeModel.getSizeDescription(),resolver);
        if(sizeItem != null)
            return sizeItem;
        return insertSizeItem(resolver,sizeModel);
    }

    public List<SizeModel> getSizeCatalogue()
    {
        return getSizeCatalogueFromResolver(resolver);
    }

    private IncomeType insertIncomeType(ContentResolver resolver , IncomeType incomeType)
    {

        ContentValues values = IncomeTypeCreator.getIncomeType(incomeType);
        Uri resultUri = resolver.insert(OpenClothesContract.IncomeType.CONTENT_URI,values);
        long id = ContentUris.parseId(resultUri);
        incomeType.setIdIncome((int) id);
        return incomeType;
    }

    private OutcomeType insertOutcomeType(ContentResolver resolver , OutcomeType outcomeType)
    {
        ContentValues values = OutcomeTypeCreator.getOutcomeType(outcomeType);
        Uri uriResult = resolver.insert(OpenClothesContract.OutcomeType.CONTENT_URI, values);
        long id = ContentUris.parseId(uriResult);
        outcomeType.setIdOutcome((int) id);

        return outcomeType;

    }

    private void updateSize(ContentResolver resolver, SizeModel sizeModel) {

        Uri sizeUri = OpenClothesContract.Size.buildSizeUri(sizeModel.getIdSize());

        ContentValues values = SizeCreator.getFromModel(sizeModel);


        String selection = OpenClothesContract.Size._ID + " = ?";

        String [] selectionArgs = new String[]{ String.valueOf(sizeModel.getIdSize()) };

        resolver.update(sizeUri,
                values,
                selection,
                selectionArgs);

    }

    private static void updateIncomeType(ContentResolver resolver, IncomeType incomeType)
    {
        Uri incomeTypeIdUri = OpenClothesContract.IncomeType.buildIncomeTypeUri(incomeType.getIdIncome());

        ContentValues values = IncomeTypeCreator.getIncomeType(incomeType);

        String selection = OpenClothesContract.IncomeType._ID + " = ?";

        String [] selectionArgs = new String[]{ String.valueOf(incomeType.getIdIncome()) };


        resolver.update(incomeTypeIdUri,
                        values,
                        selection,
                        selectionArgs);

    }

    private static void updateOutcomeType(ContentResolver resolver, OutcomeType outcomeType)
    {

        Uri outcomeTypeIdUri = OpenClothesContract.OutcomeType.buildOutcomeTypeUri(outcomeType.getIdOutcome());

        ContentValues values = OutcomeTypeCreator.getOutcomeType(outcomeType);

        String selection = OpenClothesContract.OutcomeType._ID + " = ?";

        String [] selectionArgs = new String[]{ String.valueOf(outcomeType.getIdOutcome()) };

        resolver.update(outcomeTypeIdUri,
                values,
                selection,
                selectionArgs);

    }

    private static SizeModel insertSizeItem(ContentResolver resolver, SizeModel sizeModel)
    {
        ContentValues values = SizeCreator.getFromModel(sizeModel);

        Uri uriResult = resolver.insert(OpenClothesContract.Size.CONTENT_URI,values);

        long id = ContentUris.parseId(uriResult);

        sizeModel.setIdSize((int) id);

        return sizeModel;
    }

    private Set<OutcomeType> getOutcomeTypesFromResolver(ContentResolver resolver) {
        final Cursor cursor = resolver.query(OpenClothesContract.OutcomeType.CONTENT_URI,null,null,null,null);

        Set<OutcomeType> outcomeTypes = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  outcomeTypes;
            }

            do
            {
                outcomeTypes.add(OutcomeTypeCreator.getModel(cursor));
            }while (cursor.moveToNext());

        } finally {
            cursor.close();

        }

        return outcomeTypes;
    }

    private static List<SizeModel> getSizeCatalogueFromResolver(ContentResolver resolver)
    {
        List sizeModels = Lists.newArrayList();

        Cursor cursor = null;

        try {

            cursor = resolver.query(OpenClothesContract.Size.CONTENT_URI, null, null, null, null);


            if(!cursor.moveToFirst())
                return sizeModels;

            do{
                sizeModels.add(SizeCreator.getFromCursor(cursor));
              } while (cursor.moveToNext());



        }finally {

            if(null != cursor && !cursor.isClosed())
                cursor.close();
            return sizeModels;

        }


    }

    private static SizeModel findByDescription(String sizeDescription, ContentResolver resolver)
    {
        SizeModel result = null;

        Uri uriSizeDescription = OpenClothesContract.Size.CONTENT_URI;

        String selection = OpenClothesContract.Size.SIZE + " = ? ";

        String [] selectionArgs = new String[]{sizeDescription};

        Cursor cursor  = resolver.query(uriSizeDescription, null, selection  , selectionArgs, null);

        if( ! cursor.moveToFirst() )
            return result;

        result = SizeCreator.getFromCursor(cursor);

        cursor.close();

        return result;

    }

    private static IncomeType findIncomeTypeByDescription(String description, ContentResolver resolver)
    {
        IncomeType result = null;

        Cursor cursor = null;

        Uri uriDescription = OpenClothesContract.IncomeType.CONTENT_URI;

        String selection = OpenClothesContract.IncomeType.DESCRIPTION + " = ? ";

        String [] selectionArgs = new String[]{description};
        try {

            cursor = resolver.query(uriDescription, null, selection, selectionArgs, null);

            if (!cursor.moveToFirst())
                return null;

            result = IncomeTypeCreator.getModel(cursor);
        }
        finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return result;

    }

    private static OutcomeType findOutcomeTypeByDescription(String description, ContentResolver resolver)
    {
        OutcomeType result = null;

        Cursor cursor = null;

        Uri uriDescription = OpenClothesContract.OutcomeType.CONTENT_URI;

        String selection = OpenClothesContract.OutcomeType.DESCRIPTION + " = ? ";

        String [] selectionArgs = new String[]{description};
        try {

            cursor = resolver.query(uriDescription, null, selection, selectionArgs, null);

            if (!cursor.moveToFirst())
                return null;

            result = OutcomeTypeCreator.getModel(cursor);
        }
        finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return result;

    }


}
