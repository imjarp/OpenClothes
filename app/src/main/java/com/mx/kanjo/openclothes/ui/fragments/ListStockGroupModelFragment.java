package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.provider.OpenClothesDatabase;
import com.mx.kanjo.openclothes.ui.adapters.GroupModelStockAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ListStockGroupModelFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
                                                                                                          SearchView.OnQueryTextListener {





    private interface StockColumns{
        public static String [] COLUMNS = {
                OpenClothesDatabase.Tables.STOCK + "." +  OpenClothesContract.Stock._ID,
                OpenClothesDatabase.Tables.PRODUCT + "." +  OpenClothesContract.Product._ID,
                OpenClothesContract.Product.MODEL,
                OpenClothesContract.Product.IMAGE_PATH,
                "SUM (" +  OpenClothesDatabase.Tables.STOCK + "." +  OpenClothesContract.Stock.QUANTITY +" ) ",

        };
    }

    private interface StockColumnsOrder{
        public static final int COL_STOCK_ID = 0;
        public static final int COL_PRODUCT_ID = 1;
        public static final int COL_PRODUCT_MODEL = 2;
        public static final int COL_IMAGE_PATH = 3;
        public static final int COL_QUANTITY = 4;


    }

    private static final int LOADER_STOCK = 998;

    int SPAN_COUNT = 4;

    String TAG  = "StockGroupFragment";

    private RecyclerView mRecyclerView;

    protected LayoutManagerType mCurrentLayoutManagerType;

    private LinearLayoutManager mLinearLayoutManager;

    protected RecyclerView.LayoutManager mLayoutManager;

    private Context mContext;

    private GroupModelStockAdapter adapter;

    private SearchView searchView;

    private String mQuery ;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListStockGroupModelFragment newInstance(String param1, String param2) {
        ListStockGroupModelFragment fragment = new ListStockGroupModelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListStockGroupModelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_STOCK, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_group, container, false);

        view.setTag(TAG);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_stock_fragment_find,menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if(null != searchView) {
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri uri = OpenClothesContract.Stock.buildStockUriGroupProduct();

        String selection =  OpenClothesContract.Stock.QUANTITY  + " >   ? ";

        String selectionArgs[]= new String[]{"0"};

        if( !TextUtils.isEmpty( mQuery )){

            //Quantity > 0  if model is provided
            selection =  OpenClothesContract.Stock.QUANTITY  + " >   0 ";
            selection += " AND "
                        + StockColumns.COLUMNS[ StockColumnsOrder.COL_PRODUCT_MODEL ]
                        + " LIKE  '%"+mQuery+"%'";

                            /*cursor = dbReadableDatabase.query(OpenClothesDatabase.Tables.PRODUCT,projection,
                                                                                     OpenClothesContract.Product.MODEL + " LIKE '%" + OpenClothesContract.Product.getModelFromUri(uri) + "%'",
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     sortOrder);*/


            selectionArgs = null;

        }

        String sortOrder = StockColumns.COLUMNS[ StockColumnsOrder.COL_PRODUCT_MODEL ] + " ASC ";

        return new CursorLoader( getActivity(),
                uri,
                StockColumns.COLUMNS,
                selection,
                selectionArgs,
                sortOrder );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        ArrayList<StockItem> stockItems = new ArrayList<>();

        int i = 0 ;

        try {

            if (!cursor.moveToFirst())
            {
                adapter = new GroupModelStockAdapter(getActivity(),stockItems,buildConfiguration());
                return  ;
            }
            do
            {
                stockItems.add( getFromCursor(cursor));
            } while ( cursor.moveToNext() );

        } finally {

        }

        adapter = new GroupModelStockAdapter(getActivity(),stockItems,buildConfiguration());

        if( null != mRecyclerView)
            mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        mQuery = s;

        getLoaderManager().restartLoader(LOADER_STOCK,null,this);

        return true;
    }

    private static StockItem getFromCursor(Cursor data) {
        StockItem stockItem = new StockItem();

        stockItem.setStockItemId(data.getInt(StockColumnsOrder.COL_STOCK_ID));

        stockItem.setIdProduct(data.getInt(StockColumnsOrder.COL_PRODUCT_ID));

        stockItem.setModel(data.getString(StockColumnsOrder.COL_PRODUCT_MODEL));

        Uri imagePath =  data.isNull(StockColumnsOrder.COL_IMAGE_PATH) ?
                null:
                TextUtils.isEmpty(data.getString(StockColumnsOrder.COL_IMAGE_PATH))?
                        null :
                        Uri.parse(data.getString(StockColumnsOrder.COL_IMAGE_PATH));
        stockItem.setImagePath(imagePath);

        stockItem.setQuantity(data.getInt(StockColumnsOrder.COL_QUANTITY));

        return stockItem;
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

    private ConfigImageHelper buildConfiguration() {
        Pair<Integer,Integer> sizeImage;

        if( UiUtils.isTablet(getActivity()) ) {

            sizeImage = new Pair<>(72, 72);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();
        }
        else
        {
            sizeImage = new Pair<>(72, 72);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();

        }
    }

}
