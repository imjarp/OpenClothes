package com.mx.kanjo.openclothes.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mx.kanjo.openclothes.R;

/**
 * Created by JARP on 3/29/15.
 */
public class SearchActivity extends ActionBarActivity {

    private static final String TAG ="SearchActivity";

    String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String query =  getIntent().getStringExtra(SearchManager.QUERY);

        query = query == null ? "" : query;


        mQuery = query;

        setContentView(R.layout.activity_search);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


}
