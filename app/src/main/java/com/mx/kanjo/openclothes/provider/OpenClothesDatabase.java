package com.mx.kanjo.openclothes.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by JARP on 12/4/14.
 */
public class OpenClothesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="openclothes.db";

    private static final int VER_2014_RELEASE_A=122;
    private static final int CUR_DATABASE_VERSION = VER_2014_RELEASE_A;

    private final Context mContext;

    public interface Tables {
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

        String PRODUCT_JOIN_STOCK = "product "
                + "INNER JOIN stock ON product._id = stock.idProduct ";

        String STOCK_JOIN_PRODUCT= "stock "
                + "INNER JOIN product ON product._id = stock.idProduct ";

        String STOCK_JOIN_PRODUCT_JOIN_SIZE = "stock "
                + "INNER JOIN product ON product._id = stock.idProduct "
                + "INNER JOIN size ON size._id = stock.idSize ";;

        String SALE_JOIN_SALE_ITEM = "sale "
                + "INNER JOIN sale_item ON sale._id = sale_item.idSale ";

        String SALE_JOIN_SALE_ITEM_PRODUCT = "sale "
                + "INNER JOIN sale_item ON sale._id = sale_item.idSale "
                + "INNER JOIN product ON sale_item.idProduct = product._id ";

        String SALE_ITEM_JOIN_PRODUCT = "product "
                + "INNER JOIN sale_item ON product._id = sale_item.idProduct ";

        String PROMISE_JOIN_PROMISE_ITEM = "promise "
                + "INNER JOIN promise_item ON promise._id = promise_item.idPromise ";

        String PROMISE_JOIN_PROMISE_ITEM_PRODUCT = "promise "
                + "INNER JOIN promise_item ON promise._id = promise_item.idPromise "
                + "INNER JOIN product ON promise_item.idProduct = product._id ";



    }

    interface Qualified {
        //Stock Table
        String STOCK_PRODUCT_ID = Tables.STOCK + "."
                + OpenClothesContract.Stock.ID_PRODUCT;

        String STOCK_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;

        //Sale Item Table
        String SALE_ITEM_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String SALE_ITEM_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;
        String SALE_ITEM_SALE_ID = Tables.SALE + "."
                + OpenClothesContract.Sale._ID;

        //Promise Item Table
        String PROMISE_ITEM_PRODUCT_ID = Tables.PRODUCT + "."
                + OpenClothesContract.Product._ID;
        String PROMISE_ITEM_SIZE_ID = Tables.SIZE + "."
                + OpenClothesContract.Size._ID;
        String PROMISE_ITEM_PROMISE_ID = Tables.PROMISE + "."
                + OpenClothesContract.Promise._ID;

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
        String SALE_ID = " REFERENCES " +  Tables.SALE + "(" + OpenClothesContract.Sale._ID + ")";
        String INCOME_TYPE_ID = " REFERENCES " +  Tables.INCOME_TYPE + "(" + OpenClothesContract.IncomeType._ID + ")";
        String OUTCOME_TYPE_ID = " REFERENCES " +  Tables.OUTCOME_TYPE + "(" + OpenClothesContract.OutcomeType._ID + ")";
        String PROMISE_ID = " REFERENCES " +  Tables.PROMISE + "(" + OpenClothesContract.Promise._ID + ")";
    }

    public OpenClothesDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.PRODUCT + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.ProductColumns.NAME + " TEXT,"
                + OpenClothesContract.ProductColumns.DATE + " TEXT NOT NULL,"
                + OpenClothesContract.ProductColumns.IMAGE_PATH + " TEXT,"
                + OpenClothesContract.ProductColumns.IS_ACTIVE + " INTEGER,"
                + OpenClothesContract.ProductColumns.MODEL + " TEXT NOT NULL,"
                + OpenClothesContract.ProductColumns.DESCRIPTION + " TEXT,"
                + OpenClothesContract.ProductColumns.PRICE + " INT NOT NULL,"
                + OpenClothesContract.ProductColumns.COST + " INT NOT NULL,"
                + "UNIQUE (" + OpenClothesContract.ProductColumns.MODEL +") ON CONFLICT REPLACE )" );


        db.execSQL("CREATE TABLE " + Tables.SIZE + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.SizeColumns.SIZE + " TEXT NOT NULL,"
                + "UNIQUE (" + OpenClothesContract.SizeColumns.SIZE + ") ON CONFLICT REPLACE )" );

        db.execSQL("CREATE TABLE " + Tables.STOCK + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.StockColumns.ID_PRODUCT + " INTEGER NOT NULL " + References.PRODUCT_ID + ","
                + OpenClothesContract.StockColumns.ID_SIZE + " INTEGER NOT NULL " + References.SIZE_ID + ","
                + OpenClothesContract.StockColumns.QUANTITY + " INTEGER NOT NULL,"
                + "UNIQUE (" + OpenClothesContract.StockColumns.ID_PRODUCT + ","
                    + OpenClothesContract.StockColumns.ID_SIZE + ") ON CONFLICT REPLACE )" );

        db.execSQL("CREATE TABLE " + Tables.SALE + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.SaleColumns.DATE + " TEXT NOT NULL ,"
                + OpenClothesContract.SaleColumns.CUSTOMER + " TEXT NOT NULL ,"
                + OpenClothesContract.SaleColumns.TOTAL + " INTEGER NOT NULL )");

        db.execSQL("CREATE TABLE " + Tables.SALE_ITEM + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.SaleItemColumns.ID_PRODUCT + " INTEGER NOT NULL " + References.PRODUCT_ID + ","
                + OpenClothesContract.SaleItemColumns.ID_SIZE + " INTEGER NOT NULL " + References.SIZE_ID + ","
                + OpenClothesContract.SaleItemColumns.SALE_ID + " INTEGER NOT NULL " + References.SALE_ID + ","
                + OpenClothesContract.SaleItemColumns.MODEL_PRODUCT + " TEXT NOT NULL " + ","
                + OpenClothesContract.SaleItemColumns.SIZE + " TEXT NOT NULL " + ","
                + OpenClothesContract.SaleItemColumns.QUANTITY + " INTEGER NOT NULL,"
                + "UNIQUE (" + BaseColumns._ID + "," +  OpenClothesContract.SaleItemColumns.ID_PRODUCT + ","
                        + OpenClothesContract.SaleItemColumns.ID_SIZE + ","
                        + OpenClothesContract.SaleItemColumns.SALE_ID + ") ON CONFLICT REPLACE ) " );

        db.execSQL("CREATE TABLE " + Tables.PROMISE  + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.PromiseColumns.CUSTOMER + " TEXT NOT NULL,"
                + OpenClothesContract.PromiseColumns.DATE + " TEXT NOT NULL,"
                + OpenClothesContract.PromiseColumns.TOTAL + " INTEGER NOT NULL )" );

        db.execSQL("CREATE TABLE " + Tables.PROMISE_ITEM + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.PromiseItemColumns.ID_PRODUCT + " INTEGER NOT NULL " + References.PRODUCT_ID + ","
                + OpenClothesContract.PromiseItemColumns.ID_SIZE + " INTEGER NOT NULL " + References.SIZE_ID + ","
                + OpenClothesContract.PromiseItemColumns.ID_PROMISE + " INTEGER NOT NULL " + References.PROMISE_ID + ","
                + OpenClothesContract.PromiseItemColumns.QUANTITY + " INTEGER NOT NULL )");

        db.execSQL("CREATE TABLE " + Tables.INCOME_TYPE + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.IncomeTypeColumns.DESCRIPTION + " TEXT NOT NULL,"
                + "UNIQUE (" + OpenClothesContract.IncomeTypeColumns.DESCRIPTION + ") ON CONFLICT REPLACE )");

        db.execSQL("CREATE TABLE " + Tables.INCOME + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.IncomeColumns.ID_PRODUCT + " INTEGER NOT NULL " + References.PRODUCT_ID + ","
                + OpenClothesContract.IncomeColumns.ID_SIZE + " INTEGER NOT NULL " + References.SIZE_ID + "," 
                + OpenClothesContract.IncomeColumns.ID_INCOME_TYPE + " INTEGER NOT NULL " + References.INCOME_TYPE_ID + ","
                + OpenClothesContract.IncomeColumns.QUANTITY + " INTEGER NOT NULL,"
                + OpenClothesContract.IncomeColumns.DATE + " INTEGER NOT NULL )");

        db.execSQL("CREATE TABLE " + Tables.OUTCOME_TYPE + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.OutcomeTypeColumns.DESCRIPTION + " TEXT NOT NULL,"
                + "UNIQUE (" + OpenClothesContract.OutcomeTypeColumns.DESCRIPTION + ") ON CONFLICT REPLACE )");

        db.execSQL("CREATE TABLE " + Tables.OUTCOME + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OpenClothesContract.OutcomeColumns.ID_PRODUCT + " INTEGER NOT NULL " + References.PRODUCT_ID + ","
                + OpenClothesContract.OutcomeColumns.ID_SIZE + " INTEGER NOT NULL " + References.SIZE_ID + ","
                + OpenClothesContract.OutcomeColumns.ID_OUTCOME_TYPE + " INTEGER NOT NULL " + References.OUTCOME_TYPE_ID + ","
                + OpenClothesContract.OutcomeColumns.QUANTITY + " INTEGER NOT NULL,"
                + OpenClothesContract.OutcomeColumns.DATE + " INTEGER NOT NULL )");

        // Insert a sale outcome
        db.execSQL("INSERT INTO " + Tables.OUTCOME_TYPE
                   + " ( " + BaseColumns._ID + "," + OpenClothesContract.OutcomeTypeColumns.DESCRIPTION + " ) "
                   + " VALUES "
                   + "( 0 , 'Sale')");

        // Insert a purchase income
        db.execSQL("INSERT INTO " + Tables.INCOME_TYPE
                + " ( " + BaseColumns._ID + "," + OpenClothesContract.IncomeTypeColumns.DESCRIPTION + " ) "
                + " VALUES "
                + "( 0 , 'Purchase')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
