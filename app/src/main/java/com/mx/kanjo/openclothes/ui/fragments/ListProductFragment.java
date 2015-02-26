package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.ui.ProductActivity;
import com.mx.kanjo.openclothes.ui.ProductAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ListProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProductFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {



    private static final String TAG = "ListProductFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_NEW_PRODUCT = 1;
    private static final int LOADER_PRODUCT = 999;

    private String mParam1;
    private String mParam2;



    private ArrayList<LeanProductModel> productList = new ArrayList<>();

    private RecyclerView mRecyclerViewProducts;

    private LinearLayoutManager mLinearLayoutManager;

    private interface ProductColumns{
        public static String [] COLUMNS = {
                OpenClothesContract.Product._ID,
                OpenClothesContract.Product.MODEL,
                OpenClothesContract.Product.IMAGE_PATH,
                OpenClothesContract.Product.PRICE

        };
    }

    private interface ProductColumnsOrder{
        public static final int COL_PRODUCT_ID = 0;
        public static final int COL_PRODUCT_MODEL = 1;
        public static final int COL_IMAGE_PATH = 2;
        public static final int COL_PRICE = 3;

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListProductFragment newInstance(String param1, String param2) {
        ListProductFragment fragment = new ListProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static LeanProductModel getFromCursor(Cursor cursor)
    {
        LeanProductModel model = new LeanProductModel();
        model.ID = cursor.getInt(ProductColumnsOrder.COL_PRODUCT_ID);
        model.Model = cursor.getString(ProductColumnsOrder.COL_PRODUCT_MODEL);
        model.Price  = cursor.getInt( ProductColumnsOrder.COL_PRICE);
        model.ImagePath =  cursor.isNull(ProductColumnsOrder.COL_IMAGE_PATH) ?
                           null :
                           TextUtils.isEmpty(cursor.getString(ProductColumnsOrder.COL_IMAGE_PATH)) ?
                           null :
                           Uri.parse(cursor.getString(ProductColumnsOrder.COL_IMAGE_PATH));



        return model;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = OpenClothesContract.Product.MODEL + " ASC ";

        Uri productsUri = OpenClothesContract.Product.CONTENT_URI;

        String selection =  OpenClothesContract.Product.IS_ACTIVE + " = ? " ;

        //Active
        String [] selectionArgs = { "1" };

        return new CursorLoader( getActivity(),
                                 productsUri,
                                 ProductColumns.COLUMNS,
                                 selection,
                                 selectionArgs,
                                 sortOrder );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        productList.clear();

        if( data.getCount() == 0 )
            return;

        if (!data.moveToFirst())
            return;
        do
        {
            productList.add(getFromCursor(data));
        }while (data.moveToNext());

        ProductAdapter adapter = new ProductAdapter(getActivity(), productList);

        if( null != mRecyclerViewProducts)
            mRecyclerViewProducts.setAdapter(adapter);


    }

    //TODO : check
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        productList.clear();

        ProductAdapter adapter = new ProductAdapter(getActivity(), productList);

        if( null != mRecyclerViewProducts) {
            mRecyclerViewProducts.setAdapter(adapter);
            mRecyclerViewProducts.getAdapter().notifyDataSetChanged();
        }


        //adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(Activity.RESULT_OK != resultCode)
            return;

        if( requestCode == REQUEST_NEW_PRODUCT )
        {
            this.onLoaderReset(null);
            getLoaderManager().restartLoader(LOADER_PRODUCT, null, this);

        }
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
        View view =  inflater.inflate(R.layout.fragment_productlist, container, false);

        view.setTag(TAG);

        mRecyclerViewProducts = (RecyclerView) view.findViewById(R.id.recycle_view_list_product);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER ;
            //setContentView(R.layout.main_activity_tablet);
        } else {
            //setContentView(R.layout.main_activity);
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;
        }

        //mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER ;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        ButterKnife.inject(this,view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_PRODUCT,null,this);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        ButterKnife.reset(this);

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_products,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView.LayoutManager mLayoutManager;

    private static final int SPAN_COUNT = 5;

    public ListProductFragment() {
        // Required empty public constructor
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerViewProducts.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerViewProducts.getLayoutManager())
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

        mRecyclerViewProducts.setLayoutManager(mLayoutManager);
        mRecyclerViewProducts.scrollToPosition(scrollPosition);
    }

    @OnClick(R.id.btnCreateProduct)
    public void onClickCreateProduct(View view)
    {

        Intent intentNewProduct = new Intent(getActivity(), ProductActivity.class);

        startActivityForResult(intentNewProduct, REQUEST_NEW_PRODUCT);

    }


}
