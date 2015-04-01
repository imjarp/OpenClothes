package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mx.kanjo.openclothes.R;

/**
 * Created by JARP on 1/27/15.
 */
public class NavigationFragment extends Fragment implements ListView.OnItemClickListener {

    private NavigationDrawerCallbacks mCallbacks;
    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mListView = (ListView) v.findViewById(R.id.list_ops);

        mListView.setOnItemClickListener(this);

        String [] menuOptions = getResources().getStringArray(R.array.menu_drawer_options);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getActivity(),
                                                           android.R.layout.simple_list_item_1,
                                                           android.R.id.text1,
                                                           menuOptions);

        mListView.setAdapter(mAdapter);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if( null != mCallbacks )
        {
            mCallbacks.onNavigationDrawerSelectedItem(position);
        }
    }


    public static interface NavigationDrawerCallbacks
    {
        void onNavigationDrawerSelectedItem(int position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


}