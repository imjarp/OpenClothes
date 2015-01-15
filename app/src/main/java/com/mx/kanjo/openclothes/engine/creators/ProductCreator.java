package com.mx.kanjo.openclothes.engine.creators;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.util.CreatorUtils;

/**
 * Created by JARP on 12/10/14.
 */
public class ProductCreator {

    public static ContentValues getFromProduct(ProductModel productModel)
    {
        ContentValues values = new ContentValues();

        if( productModel.getIdProduct() > 0 )
            values.put(OpenClothesContract.Product._ID,productModel.getIdProduct());

        values.put(OpenClothesContract.Product.NAME,productModel.getName());
        values.put(OpenClothesContract.Product.MODEL,productModel.getModel());
        values.put(OpenClothesContract.Product.IS_ACTIVE,productModel.isActive() ? 1 : 0 );
        values.put(OpenClothesContract.Product.DATE,productModel.getDateOperation());
        values.put(OpenClothesContract.Product.IMAGE_PATH, CreatorUtils.UriToString(productModel.getImagePath()));
        values.put(OpenClothesContract.Product.PRICE,productModel.getPrice());
        values.put(OpenClothesContract.Product.COST,productModel.getCost());


        return values;
    }

    public static ProductModel getProductModelFromCursor(Cursor cursor)
    {
        ProductModel model = new ProductModel();

        int idxIdProduct = cursor.getColumnIndex(OpenClothesContract.Product._ID);
        int idxName = cursor.getColumnIndex(OpenClothesContract.Product.NAME);
        //int idxDescription = cursor.getColumnIndex(OpenClothesContract.Product.DATE);
        int idxModel = cursor.getColumnIndex(OpenClothesContract.Product.MODEL);
        int idxDate = cursor.getColumnIndex(OpenClothesContract.Product.DATE);
        int idxImage = cursor.getColumnIndex(OpenClothesContract.Product.IMAGE_PATH);
        int idxIsActive = cursor.getColumnIndex(OpenClothesContract.Product.IS_ACTIVE);
        int idxPrice = cursor.getColumnIndex(OpenClothesContract.Product.PRICE);
        int idxCost = cursor.getColumnIndex(OpenClothesContract.Product.COST);

        model.setIdProduct(cursor.getInt(idxIdProduct));
        model.setName(cursor.getString(idxName));
        model.setModel(cursor.getString(idxModel));
        model.setDateOperation(cursor.getString(idxDate));
        model.setImagePath(getUri(cursor.getString(idxImage)));
        model.setActive( cursor.getInt(idxIsActive) > 0 ? true : false );
        model.setPrice(cursor.getInt(idxPrice));
        model.setCost(cursor.getInt(idxCost));

        return model;
    }

    public static  ProductModel getProductModelFromId(int idProduct , ContentResolver resolver)
    {
        Uri uriProductId = OpenClothesContract.Product.buildProductUri(idProduct);
        Cursor cursor = resolver.query(uriProductId,null,null,null,null);
        ProductModel productModel = getProductModelFromCursor(cursor);
        cursor.close();
        return productModel;
    }

    private static Uri getUri(String uri)
    {
        try
        {
            return Uri.parse(uri);
        }
        catch (Exception e) {
            return null;
        }
    }
}
