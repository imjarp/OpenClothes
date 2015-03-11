package com.mx.kanjo.openclothes.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.mx.kanjo.openclothes.util.SelectionBuilder;

import java.util.Arrays;

/**
 * Created by JARP on 12/8/14.
 */
public class OpenClothesProvider extends ContentProvider {



    private final String TAG = OpenClothesProvider.class.getSimpleName();

    private OpenClothesDatabase mOpenHelper;

    private final static UriMatcher sUriMatcher = buildUriMatcher();

    private static final int PRODUCT = 100;
    private static final int PRODUCT_ID = 101;
    private static final int PRODUCT_MODEL = 102;

    private static final int SIZE = 200;
    private static final int SIZE_ID =201;

    private static final int STOCK = 300;
    private static final int STOCK_ID = 301;
    private static final int STOCK_MODEL = 302;
    private static final int STOCK_PRODUCT = 304;
    private static final int STOCK_PRODUCT_SIZE = 305;
    private static final int STOCK_PRODUCT_SIZE_ALL = 306;

    private static final int SALE = 400;
    private static final int SALE_ID = 401;
    private static final int SALE_DATE = 402;
    private static final int SALE_DATE_INTERVAL = 403;


    private static final int SALE_ITEM = 500;
    private static final int SALE_ITEM_ID = 501;
    private static final int SALE_ITEM_HEADER = 502;
    private static final int SALE_ITEM_PRODUCT_ID = 503;

    private static final int PROMISE = 600;
    private static final int PROMISE_ID = 601;
    private static final int PROMISE_DATE = 602;
    private static final int PROMISE_CUSTOMER = 603;
    private static final int PROMISE_DATE_INTERVAL = 604;

    private static final int PROMISE_ITEM = 700;
    private static final int PROMISE_ITEM_ID = 701;
    private static final int PROMISE_ITEM_HEADER_PROMISE = 702;
    private static final int PROMISE_ITEM_PRODUCT = 703;

    private static final int INCOME = 800;
    private static final int INCOME_ID = 801;

    private static final int INCOME_TYPE = 900;
    private static final int INCOME_TYPE_ID = 901;

    private static final int OUTCOME = 1000;
    private static final int OUTCOME_ID = 1001;

    private static final int OUTCOME_TYPE = 1100;
    private static final int OUTCOME_TYPE_ID = 1101;

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OpenClothesContract.CONTENT_AUTHORITY;

        //Product
        matcher.addURI(authority, "product", PRODUCT);
        matcher.addURI(authority, "product/#", PRODUCT_ID);
        matcher.addURI(authority, "product/*/model", PRODUCT_MODEL);

        //Size
        matcher.addURI(authority, "size", SIZE);
        matcher.addURI(authority, "size/#", SIZE_ID);

        //Stock
        matcher.addURI(authority, "stock", STOCK);
        matcher.addURI(authority, "stock/modelAndSize", STOCK_PRODUCT_SIZE_ALL);
        matcher.addURI(authority, "stock/#", STOCK_ID);
        matcher.addURI(authority, "stock/#/idProduct",  STOCK_PRODUCT);
        matcher.addURI(authority, "stock/*/model", STOCK_MODEL);
        matcher.addURI(authority, "stock/#/idProduct/#/idSize", STOCK_PRODUCT_SIZE);



        //Sale
        matcher.addURI(authority, "sale", SALE);
        matcher.addURI(authority, "sale/#", SALE_ID);
        matcher.addURI(authority, "sale/*/date", SALE_DATE);
        matcher.addURI(authority, "sale/interval/*/*",  SALE_DATE_INTERVAL);


        //Sale Item
        matcher.addURI(authority, "sale_item", SALE_ITEM);
        matcher.addURI(authority, "sale_item/#", SALE_ITEM_ID);
        matcher.addURI(authority, "sale_item/#/header", SALE_ITEM_HEADER);
        matcher.addURI(authority, "sale_item/#/idProduct", SALE_ITEM_PRODUCT_ID);

        //Promise
        matcher.addURI(authority, "promise", PROMISE);
        matcher.addURI(authority, "promise/#", PROMISE_ID);
        matcher.addURI(authority, "promise/*/date", PROMISE_DATE);
        matcher.addURI(authority, "promise/*/customer", PROMISE_CUSTOMER);
        matcher.addURI(authority, "promise/interval/*/*", PROMISE_DATE_INTERVAL);

        //Promise Item
        matcher.addURI(authority, "promise_item", PROMISE_ITEM);
        matcher.addURI(authority, "promise_item/#", PROMISE_ITEM_ID);
        matcher.addURI(authority, "promise_item/#/header_promise", PROMISE_ITEM_HEADER_PROMISE);
        matcher.addURI(authority, "promise_item/#/product", PROMISE_ITEM_PRODUCT);

        //Income
        matcher.addURI(authority, "income", INCOME);
        matcher.addURI(authority, "income/#", INCOME_ID);

        //Income Type
        matcher.addURI(authority, "income_type", INCOME_TYPE);
        matcher.addURI(authority, "income_type/#", INCOME_TYPE_ID);

        //Outcome
        matcher.addURI(authority, "outcome", OUTCOME);
        matcher.addURI(authority, "outcome/#", OUTCOME_ID);

        //Outcome Type
        matcher.addURI(authority, "outcome_type", OUTCOME_TYPE);
        matcher.addURI(authority, "outcome_type/#", OUTCOME_TYPE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new OpenClothesDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase dbReadableDatabase = mOpenHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);

        Log.d(TAG, "uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");

        Cursor cursor = null;

        SelectionBuilder builder;

        switch (match)
        {
            // "product"
            case  PRODUCT :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PRODUCT,projection,
                                                                                     selection,
                                                                                     selectionArgs,
                                                                                     null,
                                                                                     null,
                                                                                     sortOrder);
                break;
            // "product/#"
            case  PRODUCT_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PRODUCT,projection,
                                                                                     OpenClothesContract.Product._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     sortOrder);
                break;


            // "product/*/model"
            case  PRODUCT_MODEL :
                /*
                //TODO: add a query that search a model by a filter or like or contains
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PRODUCT,projection,
                                                                                     OpenClothesContract.Product.MODEL + " LIKE '%" + OpenClothesContract.Product.getModelFromUri(uri) + "%'",
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     sortOrder);*/

                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PRODUCT,projection,
                        OpenClothesContract.Product.MODEL + " = '" + OpenClothesContract.Product.getModelFromUri(uri) + "   '",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case STOCK_PRODUCT_SIZE:
                String idProduct = OpenClothesContract.Stock.getProductIdFromUri(uri);
                String idSize = OpenClothesContract.Stock.getSizeIdFromUriWithProduct(uri);
                String where = OpenClothesContract.Stock.ID_PRODUCT + " = '" + idProduct + "' AND "
                             + OpenClothesContract.Stock.ID_SIZE + " = '" + idSize + "' ";

                 cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.STOCK,projection,
                                                                                    where,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    sortOrder);
                break;
            // "size"
            case  SIZE :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SIZE, projection,
                                                                                   selection,
                                                                                   selectionArgs,
                                                                                   null,
                                                                                   null,
                                                                                   sortOrder);
                break;
            // "size/#"
            case  SIZE_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SIZE, projection,
                                                                                   OpenClothesContract.Size._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   sortOrder);
                break;
            // "stock"
            case  STOCK :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.STOCK, projection,
                                                                                    selection,
                                                                                    selectionArgs,
                                                                                    null,
                                                                                    null,
                                                                                    sortOrder);
                break;

            //stock/modelAndSize
            case STOCK_PRODUCT_SIZE_ALL:

                builder = new SelectionBuilder().table(OpenClothesDatabase.Tables.STOCK_JOIN_PRODUCT_JOIN_SIZE)
                        .mapToTable(OpenClothesContract.Stock._ID,OpenClothesDatabase.Tables.STOCK)
                        .mapToTable(OpenClothesContract.Product._ID, OpenClothesDatabase.Tables.PRODUCT)
                        .mapToTable(OpenClothesContract.Size._ID, OpenClothesDatabase.Tables.SIZE);
                        //.where(OpenClothesDatabase.Qualified.STOCK_PRODUCT_ID + "=?", OpenClothesContract.Stock.getProductIdFromUri(uri));

                cursor = builder
                        .where(selection,selectionArgs)
                        .query(dbReadableDatabase,projection,sortOrder);

                break;
            // "stock/#"
            case  STOCK_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.STOCK, projection,
                                                                                    OpenClothesContract.Stock._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    sortOrder);
                break;
            // "stock/*/model"
            case  STOCK_MODEL :
                builder = new SelectionBuilder().table(OpenClothesDatabase.Tables.PRODUCT_JOIN_STOCK)
                                                                          .mapToTable(OpenClothesContract.Product._ID, OpenClothesDatabase.Tables.PRODUCT)
                                                                          .where(OpenClothesContract.Product.MODEL + "=?", OpenClothesContract.Stock.getModelFromUri(uri));

                    cursor = builder
                        .where(selection,selectionArgs)
                        .query(dbReadableDatabase,projection,sortOrder);


              break;


            // "stock/#/idProduct"
            case STOCK_PRODUCT :

                builder = new SelectionBuilder().table(OpenClothesDatabase.Tables.PRODUCT_JOIN_STOCK)
                                                        .mapToTable(OpenClothesContract.Product._ID, OpenClothesDatabase.Tables.PRODUCT)
                                                        .where(OpenClothesDatabase.Qualified.STOCK_PRODUCT_ID + "=?", OpenClothesContract.Stock.getProductIdFromUri(uri));

                cursor = builder
                        .where(selection,selectionArgs)
                        .query(dbReadableDatabase,projection,sortOrder);

                break;
            // "sale"
            case  SALE :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SALE, projection,
                                                                                   selection,
                                                                                   selectionArgs,
                                                                                   null,
                                                                                   null,
                                                                                   sortOrder);
                break;
            // "sale/#"
            case  SALE_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SALE, projection,
                                                                                   OpenClothesContract.Sale._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   sortOrder);
                break;
            case  SALE_DATE :
                break;
            case  SALE_DATE_INTERVAL :
                break;
            // "sale_item"
            case  SALE_ITEM :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SALE_ITEM, projection,
                                                                                        selection,
                                                                                        selectionArgs,
                                                                                        null,
                                                                                        null,
                                                                                        sortOrder);
                break;
            // "sale_item/#"
            case  SALE_ITEM_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SALE, projection,
                                                                                   OpenClothesContract.SaleItem._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   sortOrder);
                break;
            // "sale_item/#/header"
            case  SALE_ITEM_HEADER :

                boolean withProduct = Boolean.getBoolean(uri.getQueryParameter(OpenClothesContract.SaleItem.WITH_PRODUCT_PARAMETER));
                builder = new SelectionBuilder().table( withProduct ? OpenClothesDatabase.Tables.SALE_JOIN_SALE_ITEM_PRODUCT : OpenClothesDatabase.Tables.SALE_JOIN_SALE_ITEM)
                        .mapToTable(OpenClothesContract.Sale._ID, OpenClothesDatabase.Tables.SALE)
                        .mapToTable(OpenClothesContract.SaleItem.SALE_ID, OpenClothesDatabase.Tables.SALE_ITEM)
                        .where(OpenClothesDatabase.Qualified.SALE_ITEM_SALE_ID + "=?", OpenClothesContract.SaleItem.getSaleItemHeaderFromUri(uri));

                cursor = builder
                        .where(selection,selectionArgs)
                        .query(dbReadableDatabase,projection,sortOrder);

                break;
            // "sale_item/#/idProduct"
            case SALE_ITEM_PRODUCT_ID :
                builder = new SelectionBuilder().table(OpenClothesDatabase.Tables.SALE_ITEM_JOIN_PRODUCT)
                        .mapToTable(OpenClothesContract.Product._ID, OpenClothesDatabase.Tables.PRODUCT)
                        .mapToTable(OpenClothesContract.SaleItem.ID_PRODUCT, OpenClothesDatabase.Tables.SALE_ITEM)
                        .where(OpenClothesDatabase.Qualified.STOCK_PRODUCT_ID + "=?", OpenClothesContract.Stock.getProductIdFromUri(uri));

                cursor = builder
                        .where(selection,selectionArgs)
                        .query(dbReadableDatabase,projection,sortOrder);

                break;
            // "promise"
            case  PROMISE :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PROMISE, projection,
                                                                                      selection,
                                                                                      selectionArgs,
                                                                                      null,
                                                                                      null,
                                                                                      sortOrder);
                break;

            // "promise/#"
            case  PROMISE_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.SALE, projection,
                                                                                   OpenClothesContract.Promise._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   sortOrder);
                break;
            case  PROMISE_DATE :
                break;
            case  PROMISE_CUSTOMER :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PROMISE, projection,
                        OpenClothesContract.PromiseColumns.CUSTOMER + " LIKE '%" + OpenClothesContract.Promise.getCustomerFromUri(uri) + "%'",
                        null,
                        null,
                        null,
                        sortOrder);

                break;
            case  PROMISE_DATE_INTERVAL :
                break;
            case  PROMISE_ITEM :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PROMISE_ITEM, projection,
                                                                                           selection,
                                                                                           selectionArgs,
                                                                                           null,
                                                                                           null,
                                                                                           sortOrder);
                break;
            case  PROMISE_ITEM_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PROMISE_ITEM, projection,
                                                                                           OpenClothesContract.Promise._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           sortOrder);
                break;
            case  PROMISE_ITEM_HEADER_PROMISE :
                withProduct = false;
                builder = new SelectionBuilder().table( withProduct ? OpenClothesDatabase.Tables.PROMISE_JOIN_PROMISE_ITEM_PRODUCT : OpenClothesDatabase.Tables.PROMISE_JOIN_PROMISE_ITEM)
                        .mapToTable(OpenClothesContract.Promise._ID, OpenClothesDatabase.Tables.PROMISE)
                        .mapToTable(OpenClothesContract.PromiseItem.ID_PROMISE, OpenClothesDatabase.Tables.PROMISE_ITEM)
                        .where(OpenClothesDatabase.Qualified.PROMISE_ITEM_PROMISE_ID + "=?", OpenClothesContract.PromiseItem.getPromiseItemHeaderFromUri(uri));

                if(withProduct) {
                    builder.mapToTable(OpenClothesContract.Product._ID, OpenClothesDatabase.Tables.PRODUCT);
                    builder.mapToTable(OpenClothesContract.PromiseItem.ID_PRODUCT, OpenClothesDatabase.Tables.PROMISE_ITEM);
                }

                cursor = builder
                        .where(selection,selectionArgs)
                        .query(dbReadableDatabase,projection,sortOrder);


                break;
            case  INCOME :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.INCOME, projection,
                                                                                     selection,
                                                                                     selectionArgs,
                                                                                     null,
                                                                                     null,
                                                                                     sortOrder);
                break;
            case  INCOME_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.INCOME, projection,
                                                                                     OpenClothesContract.Income._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     sortOrder);
                break;
            case  INCOME_TYPE :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.INCOME_TYPE, projection,
                                                                                          selection,
                                                                                          selectionArgs,
                                                                                          null,
                                                                                          null,
                                                                                          sortOrder);
                break;
            case  INCOME_TYPE_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.INCOME_TYPE, projection,
                                                                                          OpenClothesContract.Income._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                          null,
                                                                                          null,
                                                                                          null,
                                                                                          sortOrder);
                break;
            case  OUTCOME :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.OUTCOME, projection,
                                                                                      selection,
                                                                                      selectionArgs,
                                                                                      null,
                                                                                      null,
                                                                                      sortOrder);
                break;
            case  OUTCOME_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.OUTCOME, projection,
                                                                                      OpenClothesContract.Income._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      sortOrder);
                break;
            case  OUTCOME_TYPE :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.OUTCOME_TYPE, projection,
                                                                                           selection,
                                                                                           selectionArgs,
                                                                                           null,
                                                                                           null,
                                                                                           sortOrder);
                break;
            case  OUTCOME_TYPE_ID :
                cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.OUTCOME_TYPE, projection,
                                                                                           OpenClothesContract.Income._ID + " = '" + ContentUris.parseId(uri) + "'",
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case  PRODUCT :
                return OpenClothesContract.Product.CONTENT_TYPE;
            case  PRODUCT_ID :
                return OpenClothesContract.Product.CONTENT_ITEM_TYPE;
            case  PRODUCT_MODEL :
                return OpenClothesContract.Product.CONTENT_TYPE;
            case  SIZE :
                return  OpenClothesContract.Size.CONTENT_TYPE;
            case  SIZE_ID :
                return  OpenClothesContract.Size.CONTENT_ITEM_TYPE;
            case  STOCK  :
                return  OpenClothesContract.Stock.CONTENT_TYPE;
            case  STOCK_ID :
                return  OpenClothesContract.Stock.CONTENT_ITEM_TYPE;
            case  STOCK_MODEL :
                return  OpenClothesContract.Stock.CONTENT_ITEM_TYPE;
            case STOCK_PRODUCT_SIZE:
                return OpenClothesContract.Product.CONTENT_ITEM_TYPE;
            case  SALE :
                return  OpenClothesContract.Sale.CONTENT_TYPE;
            case  SALE_ID :
                return  OpenClothesContract.Sale.CONTENT_ITEM_TYPE;
            case  SALE_DATE :
                return  OpenClothesContract.Sale.CONTENT_TYPE;
            case  SALE_DATE_INTERVAL :
                return  OpenClothesContract.Sale.CONTENT_TYPE;
            case  SALE_ITEM :
                return  OpenClothesContract.SaleItem.CONTENT_TYPE;
            case  SALE_ITEM_ID :
                return  OpenClothesContract.SaleItem.CONTENT_ITEM_TYPE;
            case  SALE_ITEM_HEADER :
                return  OpenClothesContract.SaleItem.CONTENT_TYPE;
            case  PROMISE :
                return OpenClothesContract.Promise.CONTENT_TYPE;
            case  PROMISE_ID :
                return OpenClothesContract.Promise.CONTENT_ITEM_TYPE;
            case  PROMISE_DATE :
                return OpenClothesContract.Promise.CONTENT_ITEM_TYPE;
            case  PROMISE_CUSTOMER :
                return OpenClothesContract.Promise.CONTENT_ITEM_TYPE;
            case  PROMISE_DATE_INTERVAL :
                return OpenClothesContract.Promise.CONTENT_TYPE;
            case  PROMISE_ITEM  :
                return OpenClothesContract.PromiseItem.CONTENT_TYPE;
            case  PROMISE_ITEM_ID :
                return OpenClothesContract.PromiseItem.CONTENT_ITEM_TYPE;
            case  PROMISE_ITEM_HEADER_PROMISE :
                return OpenClothesContract.PromiseItem.CONTENT_TYPE;
            case  INCOME :
                return OpenClothesContract.Income.CONTENT_TYPE;
            case  INCOME_ID :
                return OpenClothesContract.Income.CONTENT_ITEM_TYPE;
            case  INCOME_TYPE  :
                return OpenClothesContract.IncomeType.CONTENT_TYPE;
            case  INCOME_TYPE_ID  :
                return OpenClothesContract.IncomeType.CONTENT_ITEM_TYPE;
            case  OUTCOME  :
                return OpenClothesContract.Outcome.CONTENT_TYPE;
            case  OUTCOME_ID  :
                return OpenClothesContract.Outcome.CONTENT_ITEM_TYPE;
            case  OUTCOME_TYPE  :
                return OpenClothesContract.OutcomeType.CONTENT_TYPE;
            case  OUTCOME_TYPE_ID  :
                return OpenClothesContract.OutcomeType.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase dbWritableDatabase = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
        Log.d(TAG, "insert(uri=" + uri + ", values=" + values.toString());
        long id=-1;
        Uri returnUri;
        switch (match)
        {
            case  PRODUCT :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.PRODUCT, null, values);
                returnUri =  OpenClothesContract.Product.buildProductUri(id);
                break;
            case  SIZE :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.SIZE, null, values);
                returnUri = OpenClothesContract.Size.buildSizeUri(id);
                break;
            case  STOCK  :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.STOCK, null, values);
                returnUri = OpenClothesContract.Stock.buildStockUri(id);
                break;
            case  SALE :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.SALE, null, values);
                returnUri = OpenClothesContract.Sale.buildSaleUri(id);
                break;
            case  SALE_ITEM :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.SALE_ITEM, null, values);
                returnUri = OpenClothesContract.SaleItem.buildSaleItemUri(id);
                break;
            case  PROMISE :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.PROMISE, null, values);
                returnUri = OpenClothesContract.Promise.buildPromiseUri(id);
                break;
            case  PROMISE_ITEM :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.PROMISE_ITEM, null, values);
                returnUri = OpenClothesContract.PromiseItem.buildPromiseItemUri(id);
                break;
            case  INCOME :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.INCOME, null, values);
                returnUri = OpenClothesContract.Income.buildIncomeUri(id);
                break;
            case  INCOME_TYPE  :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.INCOME_TYPE, null, values);
                returnUri = OpenClothesContract.IncomeType.buildIncomeTypeUri(id);
                break;
            case  OUTCOME  :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.OUTCOME, null, values);
                returnUri = OpenClothesContract.Outcome.buildOutcomeUri(id);
                break;
            case  OUTCOME_TYPE  :
                id = dbWritableDatabase.insertOrThrow(OpenClothesDatabase.Tables.OUTCOME_TYPE, null, values);
                returnUri = OpenClothesContract.OutcomeType.buildOutcomeTypeUri(id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase dbWritableDatabase = mOpenHelper.getWritableDatabase();

        Log.d(TAG, "delete(uri=" + uri + ", values=" + Arrays.toString(selectionArgs));

        int rows=-1;

        switch (match)
        {
            case  PRODUCT :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PRODUCT,selection,selectionArgs);
                break;
            case  PRODUCT_ID :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PRODUCT,selection,selectionArgs);
                break;
            case  SIZE :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.SIZE,selection,selectionArgs);
                break;
            case  STOCK  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.STOCK,selection,selectionArgs);
                break;
            case  STOCK_ID :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.STOCK,selection,selectionArgs);
                break;
            case  SALE :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.SALE,selection,selectionArgs);
                break;
            case  SALE_ID :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.SALE,selection,selectionArgs);
                break;
            case  SALE_ITEM :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.SALE_ITEM,selection,selectionArgs);
                break;
            case  SALE_ITEM_HEADER :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.SALE_ITEM,selection,selectionArgs);
                break;
            case  PROMISE :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PROMISE,selection,selectionArgs);
                break;
            case  PROMISE_ID :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PROMISE,selection,selectionArgs);
                break;
            case  PROMISE_ITEM  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PROMISE_ITEM,selection,selectionArgs);
                break;
            case  PROMISE_ITEM_ID :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PROMISE_ITEM,selection,selectionArgs);
                break;
            case  PROMISE_ITEM_HEADER_PROMISE :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.PROMISE_ITEM,selection,selectionArgs);
                break;
            case  INCOME :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.INCOME,selection,selectionArgs);
                break;
            case  INCOME_ID :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.INCOME,selection,selectionArgs);
                break;
            case  INCOME_TYPE  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.INCOME_TYPE,selection,selectionArgs);
                break;
            case  INCOME_TYPE_ID  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.INCOME_TYPE,selection,selectionArgs);
                break;
            case  OUTCOME  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.OUTCOME,selection,selectionArgs);
                break;
            case  OUTCOME_ID  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.OUTCOME,selection,selectionArgs);
                break;
            case  OUTCOME_TYPE  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.OUTCOME_TYPE,selection,selectionArgs);
                break;
            case  OUTCOME_TYPE_ID  :
                rows = dbWritableDatabase.delete(OpenClothesDatabase.Tables.OUTCOME_TYPE,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rows;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase dbWritableDatabase = mOpenHelper.getWritableDatabase();

        Log.d(TAG, "update(uri=" + uri +"values=" + values.toString() + ", selectionArgs=" + Arrays.toString(selectionArgs));

        int rows=-1;

        switch (match)
        {

            case  PRODUCT_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.PRODUCT,values,selection,selectionArgs);
                break;
            case  SIZE_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.SIZE,values,selection,selectionArgs);
                break;
            case  STOCK_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.STOCK,values,selection,selectionArgs);
                break;
            case  SALE_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.SALE,values,selection,selectionArgs);
                break;
            case  SALE_ITEM_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.SALE_ITEM,values,selection,selectionArgs);
                break;
            case  PROMISE_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.PROMISE,values,selection,selectionArgs);
                break;
            case  PROMISE_ITEM_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.PROMISE_ITEM,values,selection,selectionArgs);
                break;
            case  INCOME_ID :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.INCOME,values,selection,selectionArgs);
                break;
            case  INCOME_TYPE_ID  :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.INCOME_TYPE,values,selection,selectionArgs);
                break;
            case  OUTCOME_ID  :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.OUTCOME,values,selection,selectionArgs);
                break;
            case  OUTCOME_TYPE_ID  :
                rows = dbWritableDatabase.update(OpenClothesDatabase.Tables.OUTCOME_TYPE,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rows;
    }
}
