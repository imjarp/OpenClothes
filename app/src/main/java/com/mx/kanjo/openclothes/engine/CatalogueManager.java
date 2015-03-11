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
        this.resolver = mContext.getContentResolver();
    }

    public Uri addNewProduct(ProductModel product)
    {
        return insertNewProduct(resolver,product);

    }

    public int updateProduct(ProductModel product){

        return updateProduct(resolver,product);
    }

    public Set<ProductModel> getCatalogue(){

        return getFromResolver();

    }

    public ProductModel findProductByModel(String model)
    {
        return findProduct(model,resolver);
    }

    private Set<ProductModel> getFromResolver() {

        final Cursor cursor = resolver.query(OpenClothesContract.Product.CONTENT_URI,null,null,null,null);

        Set<ProductModel> products = new HashSet<>(cursor.getCount());

        try {

            if (!cursor.moveToFirst())
            {
                return  products;
            }

            do{
                products.add(ProductCreator.getProductModelFromCursor(cursor));
            }while (cursor.moveToNext());

        } finally {
            if(!cursor.isClosed())
                cursor.close();
        }

        return products;
    }

    private int updateProduct(ContentResolver resolver , ProductModel model){

        Uri uriProduct = OpenClothesContract.Product.buildProductUri( model.getIdProduct() );
        ContentValues values = ProductCreator.getFromProduct(model);
        String where = OpenClothesContract.Product._ID + " = ?";
        String  [] selectionArgs= new String[]{String.valueOf(model.getIdProduct())};

        return resolver.update(uriProduct,values,where,selectionArgs);

    }

    private Uri insertNewProduct(ContentResolver resolver, ProductModel model)
    {
        ContentValues values = ProductCreator.getFromProduct(model);
        return resolver.insert(OpenClothesContract.Product.CONTENT_URI,values);
    }

    private ProductModel findProduct(String model, ContentResolver resolver) {

        ProductModel productModel = null;

        Uri productUri = OpenClothesContract.Product.buildProductUriWithModel(model);

        Cursor cursor = resolver.query(productUri,null,null,null,null);

        try {
            if (!cursor.moveToFirst())
                return productModel;

            productModel = ProductCreator.getProductModelFromCursor(cursor);
        }
        finally {
            if(! cursor.isClosed() )
                cursor.close();
        }

        return productModel;

    }



}
