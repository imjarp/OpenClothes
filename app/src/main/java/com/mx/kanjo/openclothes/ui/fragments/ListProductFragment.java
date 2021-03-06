package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.mx.kanjo.openclothes.ui.adapters.ProductAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ListProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProductFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener{



    private static final String TAG = "ListProductFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_NEW_PRODUCT = 1;
    private static final int REQUEST_UPDATE_PRODUCT = 2;
    private static final int LOADER_PRODUCT = 999;

    int positionUpdated;

    private String mParam1;
    private String mParam2;

    ConfigImageHelper configImageHelper;

    private ArrayList<LeanProductModel> productList = new ArrayList<>();

    private RecyclerView mRecyclerViewProducts;

    private LinearLayoutManager mLinearLayoutManager;

    ProductAdapter adapter;

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView.LayoutManager mLayoutManager;

    private static final int SPAN_COUNT = 5;

    private SearchView searchView;

    String mQuery = "";

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

    @Optional @InjectView(R.id.card_header_view)CardView cardViewHeader;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductFragment.
     */
    public static ListProductFragment newInstance() {
        ListProductFragment fragment = new ListProductFragment();
        Bundle args = new Bundle();
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

        if( !TextUtils.isEmpty( mQuery )){


            selection =  OpenClothesContract.Product.IS_ACTIVE   + " =   1 ";
            selection += " AND "
                    + ProductColumns.COLUMNS[ ProductColumnsOrder.COL_PRODUCT_MODEL ]
                    + " LIKE  '%"+mQuery+"%'";

            selectionArgs = null;

        }

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

        if( data.getCount() == 0 ) {
            adapter = new ProductAdapter(getActivity(), productList,configImageHelper);
            return;
        }

        if (!data.moveToFirst()) {
            adapter = new ProductAdapter(getActivity(), productList,configImageHelper);
            return;
        }
        do
        {
            productList.add(getFromCursor(data));
        }while (data.moveToNext());

        adapter = new ProductAdapter(getActivity(), productList,configImageHelper);
        adapter.SetOnItemClickListener(adapterClickListener);
        adapter.setHasStableIds(true);

        if( null != mRecyclerViewProducts)
            mRecyclerViewProducts.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        productList.clear();

        mRecyclerViewProducts.swapAdapter(null,false);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mQuery  = s ;
        getLoaderManager().restartLoader(LOADER_PRODUCT,null,this);
        return true;
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

        mRecyclerViewProducts.setOnScrollListener(onScrollListener);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        Pair<Integer,Integer> sizeImage;

        if(UiUtils.isTablet(getActivity())){
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER ;
            sizeImage = new Pair<>(168,168);
            configImageHelper = new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .build();
        } else {

            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;
            sizeImage = new Pair<>(72,72);
            configImageHelper = new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();
        }


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
    public void onResume() {
        super.onResume();
        if(productList == null){
            getLoaderManager().restartLoader(LOADER_PRODUCT,null,this);
        }

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

        inflater.inflate(R.menu.menu_products,menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if(null != searchView) {
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

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
    public void onClickCreateProduct(View view) {

        Intent intentNewProduct = new Intent(getActivity(), ProductActivity.class);

        startActivityForResult(intentNewProduct, REQUEST_NEW_PRODUCT);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(Activity.RESULT_OK != resultCode)
            return;

        if( requestCode == REQUEST_NEW_PRODUCT )
        {
            //addItemAdapter(data);
            this.onLoaderReset(null);
            getLoaderManager().restartLoader(LOADER_PRODUCT,null,this);
        }
        if ( requestCode == REQUEST_UPDATE_PRODUCT ){
            updateAdapter(data);
        }
    }

    private void addItemAdapter(Intent data){
        Uri uri = data.getData();
        if( uri ==null ){return;}

        LeanProductModel lean = getLeanProduct(uri);

        if( null == lean ){return;}

        adapter.addProduct(lean);

    }

    private void updateAdapter(Intent data) {
        Uri uri = data.getData();
        if( uri ==null ){return;}

        LeanProductModel lean = getLeanProduct(uri);

        if( null == lean ){return;}

        if( positionUpdated > -1 ) {
            adapter.updateProduct( positionUpdated, lean );
            positionUpdated = -1;
        }

    }

    private LeanProductModel getLeanProduct(Uri uri){

        Cursor cursor =  getActivity().getContentResolver().query(uri,
                ProductColumns.COLUMNS,
                null,
                null,
                null);

        //Error
        if(!cursor.moveToFirst())
            return null;
        if(cursor.getCount()<=0)
            return null;

        LeanProductModel model = getFromCursor( cursor );

        cursor.close();

        return model;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            boolean isIdle = newState == RecyclerView.SCROLL_STATE_IDLE;
            if(!UiUtils.isTablet(getActivity())) {

                boolean isOnTop =((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() ==0;

                cardViewHeader.setUseCompatPadding(true);

                if(isIdle && isOnTop){
                    cardViewHeader.setCardElevation(0);
                }
                else if(cardViewHeader.getCardElevation()==0 && !isIdle){
                    cardViewHeader.setCardElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources().getDisplayMetrics()));
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {}
    };

    ProductAdapter.OnItemClickListener adapterClickListener = new ProductAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, long id) {
            positionUpdated = position;
            Intent intent = ProductActivity.createIntentForUpdate( getActivity(), (int) id);
            startActivityForResult(intent, REQUEST_UPDATE_PRODUCT);
        }
    };

}
