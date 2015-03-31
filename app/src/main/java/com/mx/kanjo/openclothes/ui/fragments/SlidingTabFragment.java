package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.ui.adapters.StockSectionsFragmentAdapter;
import com.mx.kanjo.openclothes.ui.widgets.SlidingTabLayout;

/**
 * Created by JARP on 3/28/15.
 */
public class SlidingTabFragment extends Fragment {

    StockSectionsFragmentAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sliding,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new StockSectionsFragmentAdapter(getFragmentManager());
        mViewPager.setAdapter( adapter);

        Resources res = getResources();
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.accent));
        mSlidingTabLayout.setViewPager(mViewPager);

    }




}
