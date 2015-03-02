package com.mx.kanjo.openclothes.ui;


import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.ListProductFragment;
import com.mx.kanjo.openclothes.ui.fragments.ListSalesFragment;
import com.mx.kanjo.openclothes.ui.fragments.ListStockFragment;
import com.mx.kanjo.openclothes.ui.fragments.NavigationFragment;

public class MainActivity2 extends ActionBarActivity implements
        NavigationFragment.NavigationDrawerCallbacks {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        if (savedInstanceState == null) {
            /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SlidingTabFragment fragment = new SlidingTabFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();*/
        }

        configureToolbar();
        configureDrawer();
    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Open Clothes");
        

        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);

                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });
    }

    private void configureDrawer() {
        // Configure drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNavigationDrawerSelectedItem(int position) {

        if(position == 0)
        {

            ListStockFragment listStockFragment = ListStockFragment.newInstance("", "");
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, listStockFragment);
            transaction.commit();
            return;


        }

        if(position == 1)
        {

            ListProductFragment listProductFragment = ListProductFragment.newInstance("", "");
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, listProductFragment);
            transaction.commit();
            return;


        }

        if(position == 2)
        {

            ListSalesFragment listSalesFragment = ListSalesFragment.newInstance("", "");
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, listSalesFragment);
            transaction.commit();
            return;


        }



        if(position==4)
        {
            startActivity(new Intent(this,ConfigurationActivity.class));
            return;
        }
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        ListProductFragment fragment = ListProductFragment.newInstance("", "");
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }
}
