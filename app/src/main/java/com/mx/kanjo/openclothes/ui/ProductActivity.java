package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.ui.fragments.ProductFragment;


public class ProductActivity extends ActionBarActivity implements ProductFragment.OnFragmentProductListener {

    CatalogueManager catalogueManager ;

    public static final String KEY_UPDATE = "UpdateProduct";
    public static final String KEY_ID_PRODUCT = "IdProduct";
    public int idProduct = -1;

    public static Intent createIntentForUpdate(Context context, int idProduct){

        Intent intent = new Intent(context,ProductActivity.class);
        intent.putExtra(KEY_ID_PRODUCT,idProduct);
        intent.putExtra(KEY_UPDATE,true);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);

        Intent i = getIntent();

        Bundle args = i.getExtras() ;

        boolean updateProduct = false;

        if(args != null){

            updateProduct = args.getBoolean(KEY_UPDATE,false);

            idProduct = args.getInt(KEY_ID_PRODUCT,idProduct);

        }

        if (savedInstanceState == null) {
            if(idProduct>0){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, ProductFragment.createUpdateFragment(idProduct))
                        .commit();
            }
            else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ProductFragment())
                    .commit();
            }
        }

        configureToolbar();

        catalogueManager = new CatalogueManager(this);

    }



    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_product));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onAddProductClick(ProductModel productModel) {

        Uri uriNewProduct =  catalogueManager.addNewProduct(productModel);

        setResult(RESULT_OK, makeAddProductIntent( uriNewProduct ) );

        finish();

    }

    @Override
    public void onUpdateProduct(ProductModel productModel) {

        int rows = catalogueManager.updateProduct(productModel);
        Uri uriProductId = OpenClothesContract.Product.buildProductUri(productModel.getIdProduct());
        setResult( RESULT_OK,  makeUpdateProductIntent(uriProductId));
        finish();

    }

    public Intent makeAddProductIntent(Uri uri)
    {
        Intent intent = new Intent();
        intent.setData(uri);
        return intent;
    }

    public Intent makeUpdateProductIntent(Uri uri){
        Intent intent = new Intent();
        intent.setData(uri);
        return intent;
    }


}
