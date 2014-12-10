package com.mx.kanjo.openclothes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.Date;

/**
 * Created by JARP on 12/9/14.
 */
public class TestProvider extends AndroidTestCase {

    public static final String TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecords()
    {
        ContentResolver resolver = mContext.getContentResolver();

        resolver.delete(OpenClothesContract.Income.CONTENT_URI, null, null);
        resolver.delete(OpenClothesContract.IncomeType.CONTENT_URI, null, null);

        resolver.delete(OpenClothesContract.Outcome.CONTENT_URI, null, null);
        resolver.delete(OpenClothesContract.OutcomeType.CONTENT_URI, null, null);

        resolver.delete(OpenClothesContract.Sale.CONTENT_URI, null, null);
        resolver.delete(OpenClothesContract.SaleItem.CONTENT_URI, null, null);

        resolver.delete(OpenClothesContract.Promise.CONTENT_URI, null, null);
        resolver.delete(OpenClothesContract.PromiseItem.CONTENT_URI, null, null);

        resolver.delete(OpenClothesContract.Size.CONTENT_URI, null, null);

        resolver.delete(OpenClothesContract.Stock.CONTENT_URI, null, null);

        resolver.delete(OpenClothesContract.Product.CONTENT_URI, null, null);
    }


    public void testCRUDResolverOperations()
    {
        ContentResolver resolver = mContext.getContentResolver();

        deleteAllRecords();

        String today = OpenClothesContract.getDbDateString(new Date());

        /* Product Test */

        ContentValues values = TestDB.createProduct("Product1", today, "/somePath/image.jpg", true, "CB-001", 5, 2);
        Uri returnUri = resolver.insert(OpenClothesContract.Product.CONTENT_URI,values);
        long productId = ContentUris.parseId(returnUri);
        Cursor cursor = resolver.query(OpenClothesContract.Product.CONTENT_URI, null,
                                                                                null,
                                                                                null,
                                                                                null);

        TestDB.validateCursor(cursor,values);
        values = TestDB.createProduct("Product2", today, "/somePath/image.jpg", true, "CB-002", 5, 2);
        resolver.insert(OpenClothesContract.Product.CONTENT_URI,values);


        returnUri = OpenClothesContract.Product.buildProductUriWithModel("CB");

        cursor = resolver.query(returnUri, null, null, null, null);
        assertTrue(cursor.getCount()==2);

        /* Size Test*/

        values = TestDB.createSize("XX");
        returnUri = resolver.insert(OpenClothesContract.Size.CONTENT_URI, values);
        long sizeId = ContentUris.parseId(returnUri);
        assertTrue(sizeId != -1);

        /* Stock Test */

        values = TestDB.createStock((int)productId, (int)sizeId, 1);
        returnUri = resolver.insert(OpenClothesContract.Stock.CONTENT_URI, values);
        long stockId = ContentUris.parseId(returnUri);
        assertTrue(stockId != -1);

        // Stock by model

        returnUri = OpenClothesContract.Stock.buildStockUriWithModel("CB-001");
        cursor = resolver.query(returnUri, null, null, null, null);
        assertTrue(cursor.getCount()==1);

        returnUri = OpenClothesContract.Stock.buildStockUriWithModel("ZZ");
        cursor = resolver.query(returnUri, null, null, null, null);
        assertFalse(cursor.getCount() > 0);

        // By idProduct
        returnUri = OpenClothesContract.Stock.buildStockUriWithProductId((int)productId);
        cursor = resolver.query(returnUri, null, null, null, null);
        assertTrue(cursor.getCount() > 0);





    }

    public void testGetType()
    {

    }


}
