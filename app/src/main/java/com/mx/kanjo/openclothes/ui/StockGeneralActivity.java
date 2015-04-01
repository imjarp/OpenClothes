package com.mx.kanjo.openclothes.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.SlidingTabFragment;

public class StockGeneralActivity extends BaseActivity {

    @Override
    protected int getSelfNavDrawItem() {
        return NAV_DRAWER_STOCK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        overridePendingTransition(0, 0);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabFragment fragment = new SlidingTabFragment();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }

        //configureToolbar();

    }

}
