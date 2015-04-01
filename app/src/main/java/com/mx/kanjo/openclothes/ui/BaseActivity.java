package com.mx.kanjo.openclothes.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.NavigationFragment;

/**
 * Created by JARP on 3/31/15.
 */
public class BaseActivity  extends ActionBarActivity implements NavigationFragment.NavigationDrawerCallbacks {

    private static final long NAVDRAWER_LAUNCH_DELAY = 250;
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;
    private DrawerLayout mDrawerLayout;
    private Toolbar mainToolbar;
    private ActionBar ab;
    private ActionBarDrawerToggle mDrawerToggle;
    private Handler mHandler;

    protected static final int NAVDRAWER_ITEM_INVALID = -1;

    //int list of possible activities
    protected static final int NAV_DRAWER_STOCK = 0;
    protected static final int NAV_DRAWER_PRODUCT = 1;
    protected static final int NAV_DRAWER_SALES = 2;
    protected static final int NAV_DRAWER_CONFIGURATION = 3;

    protected int getSelfNavDrawItem (){
        return NAVDRAWER_ITEM_INVALID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        configureToolbar();

        View mainContent = findViewById(R.id.sample_content_fragment);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupNavDrawer() {

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

    private void configureToolbar() {
        mainToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mainToolbar);

        ab = getSupportActionBar();

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

    @Override
    public void onNavigationDrawerSelectedItem(final int position) {


        if(position == getSelfNavDrawItem()){
            mDrawerLayout.closeDrawers();
            return;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedItem(position);
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        //container
        View mainContent = findViewById(R.id.sample_content_fragment);
        if (mainContent != null) {
            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }

        mDrawerLayout.closeDrawers();


    }

    private void selectedItem(int position) {
        if(position == NAV_DRAWER_STOCK)
        {
            Intent intent = new Intent(this,StockGeneralActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if(position == NAV_DRAWER_PRODUCT)
        {
            Intent intent = new Intent(this,ProductGeneralActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if(position == NAV_DRAWER_SALES)
        {
            Intent intent = new Intent(this,SalesGeneralActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if(position==NAV_DRAWER_CONFIGURATION)
        {
            startActivity(new Intent(this,ConfigurationActivity.class));
            return;
        }
    }
}
