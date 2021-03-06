package com.mx.kanjo.openclothes.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.mx.kanjo.openclothes.ui.fragments.NavigationFragment;

public class MainActivity2 extends ActionBarActivity implements
        NavigationFragment.NavigationDrawerCallbacks {

    int lastSelectedPosition;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar mainToolbar;

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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void configureToolbar() {
        mainToolbar = (Toolbar) findViewById(R.id.toolbar);
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

        mDrawerLayout.closeDrawers();
        if(position == 0)
        {

            Intent intent = new Intent(this,StockActivity.class);

            startActivity(intent);
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
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction().addToBackStack(null);
            transaction.replace(R.id.sample_content_fragment, listSalesFragment);
            transaction.commit();
            return;


        }



        if(position==3)
        {
            startActivity(new Intent(this,ConfigurationActivity.class));
            return;
        }

    }


}
