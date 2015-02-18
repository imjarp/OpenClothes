package com.mx.kanjo.openclothes.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.ui.fragments.ProductFragment;


public class ProductActivity extends ActionBarActivity implements ProductFragment.OnFragmentProductListener {

    CatalogueManager catalogueManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ProductFragment())
                    .commit();
        }

        configureToolbar();

        catalogueManager = new CatalogueManager(this);

    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_product));

    }


    @Override
    public void onAddProductClick(ProductModel productModel) {

        Uri uriNewProduct =  catalogueManager.addNewProduct(productModel);

        setResult(RESULT_OK, makeAddProductIntent( uriNewProduct ) );

        finish();

    }

    public Intent makeAddProductIntent(Uri uri)
    {
        Intent intent = new Intent();
        intent.setData(uri);
        return intent;
    }


}
