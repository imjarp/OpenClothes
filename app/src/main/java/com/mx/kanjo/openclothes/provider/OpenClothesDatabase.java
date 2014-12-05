package com.mx.kanjo.openclothes.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by JARP on 12/4/14.
 */
public class OpenClothesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="openclothes.db";

    private static final int VER_2014_RELEASE_A=122;
    private static final int CUR_DATABASE_VERSION = VER_2014_RELEASE_A;

    private final Context mContext;

    interface Tables {
        String PRODUCT = "product";
        String SIZE = "size";
        String STOCK = "stock";
        String SALE = "sale";
        String SALE_ITEM = "sale_item";
        String INCOME = "income";
        String INCOME_TYPE = "income_type";
        String OUTCOME = "outcome";
        String OUTCOME_TYPE = "outcome_type";
        String PROMISE = "promise";
        String PROMISE_ITEM = "promise_item";
    }

    private interface Qualified {
        //Stock Table
        String STOCK_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String STOCK_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;

        //Sale Item Table
        String SALE_ITEM_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String SALE_ITEM_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;

        //Sale Table
        String SALE_SALE_ITEM_ID = Tables.SALE_ITEM + "."
                + OpenClothesContract.SaleItem._ID;

        //Promise Item Table
        String PROMISE_ITEM_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String PROMISE_ITEM_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;

        //Promise Table
        String PROMISE_PROMISE_ITEM_ID = Tables.PROMISE_ITEM + "."
                + OpenClothesContract.PromiseItem._ID;

        //Income Table
        String INCOME_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String INCOME_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;
        String INCOME_TYPE_ID = Tables.INCOME_TYPE + "."
                + OpenClothesContract.IncomeType._ID;

        //Outcome table
        String OUTCOME_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String OUTCOME_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;
        String OUTCOME_TYPE_ID = Tables.OUTCOME_TYPE + "."
                + OpenClothesContract.OutcomeType._ID;
    }

    private interface References {
        String PRODUCT_ID = " REFERENCES " +  Tables.PRODUCT + "(" + OpenClothesContract.Product._ID + ")";
        String SIZE_ID = " REFERENCES " +  Tables.SIZE + "(" + OpenClothesContract.Size._ID + ")";
        String SALE_ITEM_ID = " REFERENCES " +  Tables.SALE_ITEM + "(" + OpenClothesContract.SaleItem._ID + ")";
        String INCOME_TYPE_ID = " REFERENCES " +  Tables.INCOME_TYPE + "(" + OpenClothesContract.IncomeType._ID + ")";
        String OUTCOME_TYPE_ID = " REFERENCES " +  Tables.OUTCOME_TYPE + "(" + OpenClothesContract.OutcomeType._ID + ")";
        String PROMISE_ITEM_ID = " REFERENCES " +  Tables.PROMISE_ITEM + "(" + OpenClothesContract.PromiseItem._ID + ")";
    }

    public OpenClothesDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,CUR_DATABASE_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.PRODUCT + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.ProductColumns.NAME + "TEXT,"
                + OpenClothesContract.ProductColumns.DATE + "TEXT NOT NULL,"
                + OpenClothesContract.ProductColumns.IMAGE_PATH + "TEXT,"
                + OpenClothesContract.ProductColumns.IS_ACTIVE + " INTEGER,"
                + OpenClothesContract.ProductColumns.MODEL + " TEXT,"
                + OpenClothesContract.ProductColumns.PRICE + " INT" + ")" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
