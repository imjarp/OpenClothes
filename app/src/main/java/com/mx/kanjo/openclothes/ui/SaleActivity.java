package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_sale, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_sale_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}
