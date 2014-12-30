package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.engine.creators.IncomeTypeCreator;
import com.mx.kanjo.openclothes.engine.creators.OutcomeTypeCreator;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.HashSet;
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

    public void addIncomeType(IncomeType incomeType)
    {
        insertIncomeType(resolver,incomeType);
    }

    public void addOutcomeType(OutcomeType outcomeType)
    {
        insertOutcomeType(resolver,outcomeType);
    }

    public void modifyIncomeType(int idIncomeType, String incomeDescription )
    {
        updateIncomeType(resolver, new IncomeType(idIncomeType,incomeDescription));
    }

    public void modifyOutcomeType(int idOutcomeType, String outcomeDescription )
    {
        updateOutcomeType(resolver, new OutcomeType(idOutcomeType, outcomeDescription));
    }

    public Set<IncomeType> getIncomeTypes()
    {
        return getIncomesTypesFromResolver(resolver);
    }

    public Set<OutcomeType> getOutcomeTypes()
    {
        return getOutcomeTypesFromResolver(resolver);
    }



    private static void insertIncomeType(ContentResolver resolver , IncomeType incomeType)
    {

        ContentValues values = IncomeTypeCreator.getIncomeType(incomeType);
        resolver.insert(OpenClothesContract.IncomeType.CONTENT_URI,values);

    }

    private static void insertOutcomeType(ContentResolver resolver , OutcomeType outcomeType)
    {
        ContentValues values = OutcomeTypeCreator.getOutcomeType(outcomeType);
        resolver.insert(OpenClothesContract.Outcome.CONTENT_URI, values);
    }

    private static void updateIncomeType(ContentResolver resolver, IncomeType incomeType)
    {
        Uri incomeTypeIdUri = OpenClothesContract.IncomeType.buildIncomeTypeUri(incomeType.getIdIncome());

        ContentValues values = IncomeTypeCreator.getIncomeType(incomeType);

        resolver.update(incomeTypeIdUri,
                        values,
                        String.valueOf(incomeType.getIdIncome()),
                        new String[]{OpenClothesContract.IncomeType._ID});

    }

    private static void updateOutcomeType(ContentResolver resolver, OutcomeType outcomeType)
    {

        Uri outcomeTypeIdUri = OpenClothesContract.OutcomeType.buildOutcomeTypeUri(outcomeType.getIdOutcome());

        ContentValues values = OutcomeTypeCreator.getOutcomeType(outcomeType);

        resolver.update(outcomeTypeIdUri,
                values,
                String.valueOf(outcomeType.getIdOutcome()),
                new String[]{OpenClothesContract.OutcomeType._ID});

    }

    public Set<IncomeType> getIncomesTypesFromResolver(ContentResolver resolver) {

        final Cursor cursor = resolver.query(OpenClothesContract.IncomeType.CONTENT_URI,null,null,null,null);

        Set<IncomeType> incomes = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  incomes;
            }
            while (cursor.moveToNext())
            {
                incomes.add(IncomeTypeCreator.getModel(cursor));
            }

        } finally {
            cursor.close();

        }

        return incomes;

    }

    private Set<OutcomeType> getOutcomeTypesFromResolver(ContentResolver resolver) {
        final Cursor cursor = resolver.query(OpenClothesContract.OutcomeType.CONTENT_URI,null,null,null,null);

        Set<OutcomeType> outcomeTypes = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  outcomeTypes;
            }
            while (cursor.moveToNext())
            {
                outcomeTypes.add(OutcomeTypeCreator.getModel(cursor));
            }

        } finally {
            cursor.close();

        }

        return outcomeTypes;
    }
}
