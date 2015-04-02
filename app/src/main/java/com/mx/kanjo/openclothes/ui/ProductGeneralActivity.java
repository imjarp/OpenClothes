package com.mx.kanjo.openclothes.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.fragments.ListProductFragment;

public class ProductGeneralActivity extends BaseActivity {

    @Override
    protected int getSelfNavDrawItem() {
        return NAV_DRAWER_PRODUCT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        overridePendingTransition(0, 0);

        if (savedInstanceState == null) {
            ListProductFragment listProductFragment = ListProductFragment.newInstance();
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, listProductFragment);
            transaction.commit();
        }
    }
}
