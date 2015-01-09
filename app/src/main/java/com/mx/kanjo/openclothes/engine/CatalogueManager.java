package com.mx.kanjo.openclothes.engine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.engine.creators.ProductCreator;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JARP on 12/10/14.
 */
public class CatalogueManager {

    private Context mContext;
    private ContentResolver resolver;

    public CatalogueManager(Context context) {

        if(context == null) {
            throw new IllegalArgumentException("context");
        }

        mContext = context;
        resolver = context.getContentResolver();
    }

    public Uri addNewProduct(ProductModel product)
    {
        return insertNewProduct(resolver,product);

    }

    public Set<ProductModel> getCatalogue(){

        return getFromResolver();

    }

    private Set<ProductModel> getFromResolver() {

        final Cursor cursor = resolver.query(OpenClothesContract.Product.CONTENT_URI,null,null,null,null);

        Set<ProductModel> products = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  products;
            }
            while (cursor.moveToNext())
            {
                products.add(ProductCreator.getProductModelFromCursor(cursor));
            }

        } finally {
            cursor.close();

        }

        return products;
    }

    private static Uri insertNewProduct(ContentResolver resolver, ProductModel model)
    {
        ContentValues values = ProductCreator.getFromProduct(model);
        return resolver.insert(OpenClothesContract.Product.CONTENT_URI,values);
    }




}
