package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.provider.OpenClothesDatabase;
import com.mx.kanjo.openclothes.ui.NewSaleActivity;
import com.mx.kanjo.openclothes.ui.SaleActivity;
import com.mx.kanjo.openclothes.ui.adapters.SaleHeaderAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.Lists;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ListSalesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    private interface SaleColumns {
        public static String [] COLUMNS = {
                OpenClothesDatabase.Tables.SALE+ "." +  OpenClothesContract.Sale._ID,
                OpenClothesDatabase.Tables.SALE + "." +  OpenClothesContract.Sale.TOTAL,
                OpenClothesDatabase.Tables.SALE + "." +  OpenClothesContract.Sale.DATE,
                OpenClothesDatabase.Tables.SALE + "." +  OpenClothesContract.Sale.CUSTOMER,
        };
    }

    private interface SaleColumnsOrder {
        public static final int COL_SALE_ID = 0;
        public static final int COL_TOTAL = 1;
        public static final int COL_DATE = 2;
        public static final int COL_CUSTOMER = 3;


    }

    private static final int LOADER_SALE = 998;

    private static final String TAG = ListSalesFragment.class.getSimpleName();

    private static final int SPAN_COUNT = 1;

    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;

    protected LayoutManagerType mCurrentLayoutManagerType;

    private LinearLayoutManager mLinearLayoutManager;

    protected RecyclerView.LayoutManager mLayoutManager;

    private SaleHeaderAdapter adapter;

    private ArrayList<SaleModel> saleItems = Lists.newArrayList();

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockFragment.
     */
    public static ListSalesFragment newInstance(String param1, String param2) {
        ListSalesFragment fragment = new ListSalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListSalesFragment() {
        // Required empty public constructor

    }

    private static SaleModel getFromCursor(Cursor data){
        SaleModel sale = new SaleModel();
        sale.setId(data.getInt(SaleColumnsOrder.COL_SALE_ID));
        sale.setDate(data.getString(SaleColumnsOrder.COL_DATE));
        sale.setCustomer(data.getString(SaleColumnsOrder.COL_CUSTOMER));
        sale.setTotal(data.getInt(SaleColumnsOrder.COL_TOTAL));
        return sale;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_sale, container, false);

        view.setTag(TAG);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        ButterKnife.inject(this,view);

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_SALE, null, this);

        setTitle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = SaleColumns.COLUMNS[SaleColumnsOrder.COL_DATE] + " DESC";

        Uri salesUri = OpenClothesContract.Sale.CONTENT_URI;

        String selection =  null ;

        //Active
        String [] selectionArgs =null;// { "1" };


        return new CursorLoader( getActivity(),
                                 salesUri,
                                 SaleColumns.COLUMNS,
                                 selection,
                                 selectionArgs,
                                 sortOrder );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        saleItems.clear();

        if( data.getCount() == 0 )
            return;

        if (!data.moveToFirst())
            return;
        do
        {
            saleItems.add(getFromCursor(data));
        }while (data.moveToNext());

        adapter  = new SaleHeaderAdapter(getActivity(),saleItems,buildConfiguration());

        adapter.SetOnItemClickListener(adapterOnClickListener );

        adapter.setHasStableIds(true);

        if( null != mRecyclerView)
            mRecyclerView.setAdapter(adapter);

    }

    SaleHeaderAdapter.OnItemClickListener adapterOnClickListener = new SaleHeaderAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, long id) {

            Toast.makeText(getActivity(),"On click " + id ,Toast.LENGTH_SHORT ).show();

            startActivity( SaleActivity.createIntentShowSaleActivity((int) id, mContext) );
        }
    } ;

    private ConfigImageHelper buildConfiguration() {
        Pair<Integer,Integer>sizeImage;

        if( UiUtils.isTablet( getActivity() ) ) {

            sizeImage = new Pair<>(72, 72);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();
        }
        else
        {
            sizeImage = new Pair<>(48, 48);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        saleItems.clear();
    }


    private void showFragment(android.support.v4.app.DialogFragment dialogFragment, String TAG, int requestCode)
    {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        dialogFragment.setTargetFragment(this, requestCode);
        dialogFragment.show(fm, TAG);

    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @OnClick(R.id.btnNewSale)
    public void onClickButton(View view){

        Intent newSaleIntent = new Intent(mContext, NewSaleActivity.class);
        startActivity(newSaleIntent);
    }

    private void setTitle() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_sales_fragment));
    }

}
