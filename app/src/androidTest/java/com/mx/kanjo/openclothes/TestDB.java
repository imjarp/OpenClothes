    package com.mx.kanjo.openclothes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.provider.OpenClothesDatabase;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by JARP on 12/5/14.
 */
public class TestDB  extends AndroidTestCase{


    private final String TAG = TestDB.class.getSimpleName();


    public void testCreateDB()
    {
        mContext.deleteDatabase(OpenClothesDatabase.DATABASE_NAME);

        SQLiteDatabase database = new OpenClothesDatabase(this.mContext).getWritableDatabase();

        assertEquals(true, database.isOpen());

        database.close();

        this.mContext.deleteDatabase(OpenClothesDatabase.DATABASE_NAME);

    }

    public void testInsertReadDB()
    {

        SQLiteDatabase db = new OpenClothesDatabase(this.mContext).getWritableDatabase();

        String today = OpenClothesContract.getDbDateString(new Date());

        //Product Table

        ContentValues values = createProduct("Product1",today,"/somePath/image.jpg",true,"CB-001",5,2);

        long productId = db.insert(OpenClothesDatabase.Tables.PRODUCT,null,values);

        assertTrue(productId!=-1);

        Log.d(TAG, "Product created with id " + productId);

        Cursor cursor = db.query(OpenClothesDatabase.Tables.PRODUCT,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Size Table

        values = createSize("XX");

        long sizeId = db.insert(OpenClothesDatabase.Tables.SIZE,null,values);

        assertTrue(sizeId!=-1);

        Log.d(TAG, "Size created with id " + sizeId);

        cursor = db.query(OpenClothesDatabase.Tables.SIZE,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Stock Table
        values = createStock((int)productId,(int)sizeId,1);

        long idStock = db.insert(OpenClothesDatabase.Tables.STOCK,null,values);

        assertTrue(idStock!=-1);

        Log.d(TAG, "Stock created with id " + idStock);

        cursor = db.query(OpenClothesDatabase.Tables.STOCK,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Sale Table
        values = createSale(today,55);

        long idHeaderSale = db.insertOrThrow(OpenClothesDatabase.Tables.SALE,null,values);

        assertTrue(idHeaderSale !=-1);

        Log.d(TAG, "Sale created with id " + idHeaderSale);

        cursor = db.query(OpenClothesDatabase.Tables.SALE,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Sale Item Table
        values = createSaleItem((int)idHeaderSale,(int)productId,(int)sizeId,5);

        long saleItemId = db.insert(OpenClothesDatabase.Tables.SALE_ITEM,null,values);

        assertTrue(saleItemId !=-1);

        Log.d(TAG, "Sale Item created with id " + saleItemId);

        cursor = db.query(OpenClothesDatabase.Tables.SALE_ITEM,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Promise Table

        values = createPromise(today,55,"LAURITA");

        long salePromiseHeader = db.insert(OpenClothesDatabase.Tables.PROMISE,null,values);

        assertTrue(salePromiseHeader !=-1);

        Log.d(TAG, "Promise created with id " + salePromiseHeader);

        cursor = db.query(OpenClothesDatabase.Tables.PROMISE,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Promise Item Table

        values = createPromiseItem((int)salePromiseHeader,(int)productId,(int)sizeId,2);

        long salePromiseItem = db.insert(OpenClothesDatabase.Tables.PROMISE_ITEM,null,values);

        assertTrue(salePromiseItem !=-1);

        Log.d(TAG, "Promise created with id " + salePromiseItem);

        cursor = db.query(OpenClothesDatabase.Tables.PROMISE_ITEM,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Income Type Table

        values = createIncomeType("Unknow reason");

        long incomeTypeId = db.insert(OpenClothesDatabase.Tables.INCOME_TYPE,null,values);

        assertTrue(incomeTypeId!=-1);

        Log.d(TAG, "Income type created with id " + incomeTypeId);

        cursor = db.query(OpenClothesDatabase.Tables.INCOME_TYPE,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Income Table

        values = createIncome((int)incomeTypeId,(int)productId,(int)sizeId,4,today);

        long incomeId = db.insert(OpenClothesDatabase.Tables.INCOME,null,values);

        assertTrue(incomeId!=-1);

        Log.d(TAG, "Income type created with id " + incomeId);

        cursor = db.query(OpenClothesDatabase.Tables.INCOME,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Income Type Table

        values = createIncomeType("Unknow reason");

        long outcomeTypeId = db.insert(OpenClothesDatabase.Tables.OUTCOME_TYPE,null,values);

        assertTrue(outcomeTypeId!=-1);

        Log.d(TAG, "Income type created with id " + outcomeTypeId);

        cursor = db.query(OpenClothesDatabase.Tables.OUTCOME_TYPE,null,null,null,null,null,null);

        validateCursor(cursor,values);

        //Income Table

        values = createIncome((int)outcomeTypeId,(int)productId,(int)sizeId,4,today);

        long outcomeId = db.insert(OpenClothesDatabase.Tables.OUTCOME,null,values);

        assertTrue(outcomeId!=-1);

        Log.d(TAG, "Outcome type created with id " + outcomeId);

        cursor = db.query(OpenClothesDatabase.Tables.OUTCOME,null,null,null,null,null,null);

        validateCursor(cursor,values);



    }



    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }

    static ContentValues createProduct(String name,String date, String imagePath, boolean isActive,
                                       String model, int price, int cost)
    {
        ContentValues values = new ContentValues();

        values.put(OpenClothesContract.Product.NAME , name);
        values.put(OpenClothesContract.Product.DATE, date);
        values.put(OpenClothesContract.Product.IMAGE_PATH, imagePath);
        values.put(OpenClothesContract.Product.IS_ACTIVE, isActive ? 1 : 0);
        values.put(OpenClothesContract.Product.PRICE, price);
        values.put(OpenClothesContract.Product.MODEL,model);
        values.put(OpenClothesContract.Product.COST, cost);

        return  values;

    }

    static ContentValues createSize(String name)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Size.SIZE , name);
        return  values;
    }

    static ContentValues createStock(int productId, int sizeId, int quantity)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Stock.ID_PRODUCT, productId);
        values.put(OpenClothesContract.Stock.ID_SIZE,sizeId);
        values.put(OpenClothesContract.Stock.QUANTITY,quantity);
        return  values;
    }

    static ContentValues createSale(String date, int total)
    {

        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Sale.DATE,date);
        values.put(OpenClothesContract.Sale.TOTAL,total);
        return  values;
    }

    static ContentValues createSaleItem(int saleParentId,int productId, int sizeId, int quantity)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.SaleItem.SALE_ID,saleParentId);
        values.put(OpenClothesContract.SaleItem.ID_PRODUCT,productId);
        values.put(OpenClothesContract.SaleItem.ID_SIZE,sizeId);
        values.put(OpenClothesContract.SaleItem.QUANTITY,quantity);
        return  values;
    }

    static ContentValues createIncomeType(String incomeDescription)
    {
        ContentValues values  = new ContentValues();
        values.put(OpenClothesContract.IncomeType.DESCRIPTION,incomeDescription);
        return  values;
    }

    static ContentValues createIncome(int idIncomeType, int idProduct, int idSize, int quantity, String date)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Income.ID_INCOME_TYPE,idIncomeType);
        values.put(OpenClothesContract.Income.ID_PRODUCT,idProduct);
        values.put(OpenClothesContract.Income.ID_SIZE,idSize);
        values.put(OpenClothesContract.Income.QUANTITY,quantity);
        values.put(OpenClothesContract.Income.DATE,date);
        return values;
    }

    static ContentValues createOutcomeType(String outcomeDescription)
    {
        ContentValues values  = new ContentValues();
        values.put(OpenClothesContract.OutcomeType.DESCRIPTION,outcomeDescription);
        return  values;
    }

    static ContentValues createOutcome(int idOutcomeType, int idProduct, int idSize, int quantity, String date)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Outcome.ID_OUTCOME_TYPE,idOutcomeType);
        values.put(OpenClothesContract.Outcome.ID_PRODUCT,idProduct);
        values.put(OpenClothesContract.Outcome.ID_SIZE,idSize);
        values.put(OpenClothesContract.Outcome.QUANTITY,quantity);
        values.put(OpenClothesContract.Outcome.DATE,date);
        return values;
    }

    static ContentValues createPromiseItem(int idHeaderPromise,int productId, int sizeId, int quantity)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.PromiseItem.ID_PRODUCT,productId);
        values.put(OpenClothesContract.PromiseItem.ID_SIZE,sizeId);
        values.put(OpenClothesContract.PromiseItem.QUANTITY,quantity);
        values.put(OpenClothesContract.PromiseItem.ID_PROMISE,idHeaderPromise);
        return values;
    }

    static ContentValues createPromise(String date, int total, String customer)
    {
        ContentValues values = new ContentValues();
        values.put(OpenClothesContract.Promise.DATE,date);
        values.put(OpenClothesContract.Promise.TOTAL,total);
        values.put(OpenClothesContract.Promise.CUSTOMER,customer);
        return values;
    }
    
    
}
