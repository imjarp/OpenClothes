package com.mx.kanjo.openclothes.ui.adapters;


import android.support.v4.app.Fragment;

import com.mx.kanjo.openclothes.ui.fragments.ListStockFragment;
import com.mx.kanjo.openclothes.ui.fragments.ListStockGroupModelFragment;

/**
 * Created by JARP on 3/28/15.
 */
public class StockSectionsFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    android.support.v4.app.FragmentManager mFragmentManager;

    private static int COUNT_FRAGMENTS = 2;

    interface PAGE_TITLES{
        String [] titles = new String[]{
            "All",
            "Model"
        };

    }

    public StockSectionsFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
       return  getFragment(position);
    }

    @Override
    public int getCount() {
        return COUNT_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES.titles[position];
    }

    public Fragment getFragment (int pos){

        if(pos==1)
            return ListStockGroupModelFragment.newInstance("", "");
        else
            return ListStockFragment.newInstance("","");

    }
}

