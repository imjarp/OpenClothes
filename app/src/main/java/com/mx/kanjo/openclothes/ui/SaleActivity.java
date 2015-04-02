package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.ShowSaleFragment;

public class SaleActivity extends ActionBarActivity {

    int idSale = 0;

    private static String EXTRA_ID_SALE="EXTRA_ID_SALE";

    public static Intent createIntentShowSaleActivity(int idSale, Context context){

        Intent i = new Intent(context,SaleActivity.class);

        i.putExtra(EXTRA_ID_SALE,idSale);

        return i;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sale);

        if(null != getIntent()){
            idSale = getIntent().getIntExtra(EXTRA_ID_SALE,0);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,ShowSaleFragment.newInstance(idSale) )
                    .commit();
        }

        configureToolbar();
    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_sale_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}
