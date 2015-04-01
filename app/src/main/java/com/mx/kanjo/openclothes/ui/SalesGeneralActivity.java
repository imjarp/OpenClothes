package com.mx.kanjo.openclothes.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.ListSalesFragment;

public class SalesGeneralActivity extends BaseActivity {

    @Override
    protected int getSelfNavDrawItem() {
        return NAV_DRAWER_SALES;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        overridePendingTransition(0, 0);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ListSalesFragment fragment = ListSalesFragment.newInstance("","");
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }

    }

}
