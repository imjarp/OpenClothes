package com.mx.kanjo.openclothes.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JARP on 12/4/14.
 */
public class OpenClothesContract {


    interface ProductColumns
    {
        String NAME="name";
        String MODEL="model";
        String IS_ACTIVE="isActive";
        String DATE = "date";
        String IMAGE_PATH ="path";
        String PRICE = "price";
        String COST = "cost";
    }

    interface SizeColumns
    {
        String SIZE = "size";
    }

    interface StockColumns
    {
        String ID_PRODUCT="idProduct";
        String ID_SIZE="idSize";
        String QUANTITY="idQuantity";
    }

    interface SaleItemColumns
    {
        String ID_PRODUCT= "idProduct";
        String ID_SIZE= "idSize";
        String QUANTITY="idQuantity";
        String SALE_ID = "idSale";
    }

    interface SaleColumns
    {
        String DATE = "date";
        String TOTAL = "total";
    }

    interface IncomeTypeColumns
    {
        String DESCRIPTION = "description";
    }

    interface IncomeColumns
    {
        String ID_INCOME_TYPE="idIncomeType";
        String ID_PRODUCT="idProduct";
        String ID_SIZE="idSize";
        String QUANTITY="quantity";
        String DATE="date";
    }

    interface OutcomeTypeColumns
    {
        String DESCRIPTION = "description";
    }

    interface OutcomeColumns
    {
        String ID_OUTCOME_TYPE = "idIncomeType";
        String ID_PRODUCT = "idProduct";
        String ID_SIZE = "idSize";
        String QUANTITY = "quantity";
        String DATE = "date";
    }

    interface PromiseItemColumns
    {
        String ID_PRODUCT= "idProduct";
        String ID_SIZE= "idSize";
        String QUANTITY="idQuantity";
        String ID_PROMISE="idPromise";
    }

    interface PromiseColumns
    {
        String DATE = "date";
        String TOTAL = "total";
        String CUSTOMER = "customer";
    }

    public static final String CONTENT_AUTHORITY = "com.mx.kanjo.openclothes";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_PRODUCT = "product";
    private static final String PATH_SIZE = "size";
    private static final String PATH_STOCK = "stock";
    private static final String PATH_SALE_ITEM = "sale_item";
    private static final String PATH_SALE = "sale";
    private static final String PATH_INCOME_TYPE = "income_type";
    private static final String PATH_INCOME = "income";
    private static final String PATH_OUTCOME_TYPE = "outcome_type";
    private static final String PATH_OUTCOME = "outcome";
    private static final String PATH_PROMISE = "promise";
    private static final String PATH_PROMISE_ITEM = "promise_item";


    public static class Product implements ProductColumns, BaseColumns
    {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODUCT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.product";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.product";

        public static Uri buildProductUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildProductUriWithModel(String model)
        {
           return CONTENT_URI.buildUpon().appendPath(model).appendPath("model").build();
        }

        public static String getModelFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

    }

    public static class Size implements SizeColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SIZE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.size";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.size";

        public static Uri buildSizeUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class Stock implements StockColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOCK).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.stock";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.stock";

        public static Uri buildStockUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildStockUriProductSize()
        {
            return CONTENT_URI.buildUpon().appendPath("modelAndSize").build();
        }

        public static Uri buildStockUriWithModel(String model)
        {
            return CONTENT_URI.buildUpon().appendPath(model).appendPath("model").build();
        }

        public static Uri buildStockUriWithProductId(int productId)
        {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(productId)).appendPath("idProduct").build();
        }

        public static Uri buildStockUriWithProductIdAndSizeId(int productId, int sizeId)
        {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(productId)).appendPath("idProduct")
                                          .appendPath(String.valueOf(sizeId)).appendPath("idSize").build();
        }

        public static String getProductIdFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }


        public static String getModelFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getProductIdFromUriWithSize(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getSizeIdFromUriWithProduct(Uri uri)
        {
            return uri.getPathSegments().get(3);
        }

    }

    public static class SaleItem implements SaleItemColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SALE_ITEM).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.sale_item";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.sale_item";


        public static final String WITH_PRODUCT_PARAMETER = "withProduct";

        public static Uri buildSaleItemUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSaleItemHeader(long idHeader , boolean withProduct)
        {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(idHeader)).appendPath("header").appendQueryParameter(WITH_PRODUCT_PARAMETER, withProduct ? "true" : "false").build();
        }

        public static String getSaleItemHeaderFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

    }

    public static class Sale implements SaleColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SALE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.sale";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.sale";

        public static Uri buildSaleUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buidlSaleIntervalUri(String initialDate, String finalDate)
        {
            return CONTENT_URI.buildUpon().appendPath("interval").appendPath(initialDate).appendPath(finalDate).build();
        }

        public static Uri buildSaleDateUri(String date)
        {
            return CONTENT_URI.buildUpon().appendPath(date).appendPath("date").build();
        }

        public static String getInitialDateFromUri(Uri uri)
        {
            return uri.getPathSegments().get(2);
        }

        public static String getFinalDateFromUri(Uri uri)
        {
            return uri.getPathSegments().get(3);
        }


    }

    public static class IncomeType implements IncomeTypeColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INCOME_TYPE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.income_type";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.income_type";

        public static Uri buildIncomeTypeUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class Income implements IncomeColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INCOME).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.income";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.income";

        public static Uri buildIncomeUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class OutcomeType implements OutcomeTypeColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OUTCOME_TYPE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.outcome_type";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.outcome_type";

        public static Uri buildOutcomeTypeUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class Outcome implements OutcomeColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OUTCOME).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.outcome";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.outcome";

        public static Uri buildOutcomeUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


    public static class PromiseItem implements PromiseItemColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROMISE_ITEM).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.promise_item";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.promise_item";
        public static final String WITH_PRODUCT_PARAMETER = "withProduct";

        public static Uri buildPromiseItemUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPromiseItemWithHeader(long idHeaderPromise, boolean withProduct)
        {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(idHeaderPromise))
                                           .appendPath("header_promise")
                                           .appendQueryParameter(WITH_PRODUCT_PARAMETER, withProduct ? "true" : "false").build();
        }

        public static String getPromiseItemHeaderFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

    }

    public static class Promise implements PromiseColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROMISE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.openclothes.promise";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.openclothes.promise";

        public static Uri buildPromiseUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPromiseUriWithDate(String date)
        {
            return CONTENT_URI.buildUpon().appendPath(date).appendPath("date").build();
        }

        public static Uri buildPromiseUriWithCustomer(String customer)
        {
            return CONTENT_URI.buildUpon().appendPath(customer).appendPath("customer").build();
        }

        public static Uri buildPromiseUriWithIntervalDate(String initialDate, String finalDate)
        {
            return CONTENT_URI.buildUpon().appendPath("interval").appendPath(initialDate).appendPath(finalDate).build();
        }

        public static String getDateFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getCustomerFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getInitialDateFromUri(Uri uri)
        {
            return uri.getPathSegments().get(2);
        }

        public static String getFinalDateFromUri(Uri uri)
        {
            return uri.getPathSegments().get(3);
        }


    }


    // Format used for storing dates in the database. ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    /**
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT.
     */
    public static String getDbDateString(Date date)
    {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText)
    {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try{
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }
}
