package com.mx.kanjo.openclothes.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.SalesManager;
import com.mx.kanjo.openclothes.model.ConfigurationOrder;
import com.mx.kanjo.openclothes.model.NotificationOrderRequest;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.ui.fragments.NewSaleFragment;

public class NewSaleActivity extends ActionBarActivity implements NewSaleFragment.SaveSaleCallback {

    SalesManager mSalesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, NewSaleFragment.newInstance("",""))
                    .commit();
        }
        mSalesManager = new SalesManager(this);
        configureToolbar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_sale, menu);
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

    @Override
    public void onSaveSaleListener(SaleModel saleModel) {


        ConfigurationOrder c = new ConfigurationOrder();

        c.TransactIncompleteOrder = false;

        NotificationOrderRequest result =  mSalesManager.createNewSale(saleModel, c);

        if(result.isCompleteOrder()){
            Toast.makeText(this,"New Sale Created ! ",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }

    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_add_new_sale));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }




}
