package com.mx.kanjo.openclothes.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.SlidingTabFragment;

public class StockActivity extends ActionBarActivity {

    private Toolbar mainToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabFragment fragment = new SlidingTabFragment();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }

        configureToolbar();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {


       MenuInflater menuInflater =  getMenuInflater();

       menuInflater.inflate(R.menu.stock_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchComponent = new ComponentName(this,SearchActivity.class);

        if(null != menuItem.getActionView()) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(searchComponent));
            searchView.setIconifiedByDefault(false);

        }


        return super.onCreateOptionsMenu(menu);
    }*/


    private void configureToolbar() {
        mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("My Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}
