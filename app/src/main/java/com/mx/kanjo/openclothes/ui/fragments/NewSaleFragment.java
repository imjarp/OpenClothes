package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.util.Pair;
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
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.provider.OpenClothesDatabase;
import com.mx.kanjo.openclothes.ui.SaleItemAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.Lists;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Use the {@link NewSaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewSaleFragment extends Fragment {


    private static final int REQUEST_SALE_ITEM = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = ListSalesFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;

    private RecyclerView mRecyclerView;

    protected LayoutManagerType mCurrentLayoutManagerType;

    private LinearLayoutManager mLinearLayoutManager;

    protected RecyclerView.LayoutManager mLayoutManager;

    //TODO : Refactor for textswicther
    @InjectView(R.id.text_total_sale) TextView mTextViewTotalSale;

    SaleItemAdapter mSaleItemAdapter ;

    ArrayList<StockItem> mSalesItems = Lists.newArrayList();

    int totalSale = 0;

    int SPAN_COUNT = 0;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private interface StockColumns{
        public static String [] COLUMNS = {
                OpenClothesDatabase.Tables.STOCK + "." +  OpenClothesContract.Stock._ID,
                OpenClothesDatabase.Tables.PRODUCT + "." +  OpenClothesContract.Product._ID,
                OpenClothesContract.Product.MODEL,
                OpenClothesContract.Product.IMAGE_PATH,
                OpenClothesDatabase.Tables.SIZE + "." +  OpenClothesContract.Size._ID,
                OpenClothesDatabase.Tables.SIZE + "." +  OpenClothesContract.Size.SIZE,
                OpenClothesDatabase.Tables.STOCK + "." +  OpenClothesContract.Stock.QUANTITY,
                OpenClothesDatabase.Tables.PRODUCT + "." + OpenClothesContract.Product.PRICE

        };
    }

    private interface StockColumnsOrder{
        public static final int COL_STOCK_ID = 0;
        public static final int COL_PRODUCT_ID = 1;
        public static final int COL_PRODUCT_MODEL = 2;
        public static final int COL_IMAGE_PATH = 3;
        public static final int COL_SIZE_ID = 4;
        public static final int COL_SIZE = 5;
        public static final int COL_QUANTITY = 6;
        public static final int COL_PRICE = 7;


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewSaleFragment newInstance(String param1, String param2) {
        NewSaleFragment fragment = new NewSaleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewSaleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( Activity.RESULT_OK != resultCode)
            return;

        parseResult(requestCode,data);
    }

    private void parseResult(int requestCode, Intent data) {


        if(requestCode == REQUEST_SALE_ITEM){

            addSaleItemToForm(data);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);

        view.setTag(TAG);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        ButterKnife.inject(this, view);

        mContext = getActivity();


        mSaleItemAdapter = new SaleItemAdapter(mContext,mSalesItems,buildConfiguration());

        mRecyclerView.setAdapter(mSaleItemAdapter);

        ButterKnife.inject(this,view);

        return view;


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
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
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_new_sale_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_new_item:
                showFragment(DialogAddNewSaleItem.newInstance("",""), DialogAddStockItem.TAG, REQUEST_SALE_ITEM);
                break;

            case R.id.save_sale :
                    saveSale();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSale() {

        NavUtils.navigateUpFromSameTask(getActivity());

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
            sizeImage = new Pair<>(24, 24);

            return new ConfigImageHelper.ConfigImageHelpBuilder(sizeImage)
                    .withRoundImage(true)
                    .build();

        }
    }


    /*@OnClick(R.id.btnAddNewSaleItem)
    public void onClickListener(View view)
    {
        if( R.id.btnAddNewSaleItem == view.getId())
        {
            showFragment(DialogAddNewSaleItem.newInstance("",""), DialogAddStockItem.TAG, REQUEST_SALE_ITEM);
        }
    }*/

    private void addSaleItemToForm(Intent data) {

        int idStock = data.getIntExtra( DialogAddNewSaleItem.EXTRA_ID_STOCK, -1 );
        int idProduct = data.getIntExtra( DialogAddNewSaleItem.EXTRA_ID_PRODUCT, -1 );
        int idSize = data.getIntExtra( DialogAddNewSaleItem.EXTRA_ID_SIZE, -1 );
        int quantity = data.getIntExtra( DialogAddNewSaleItem.EXTRA_QTY, -1 );

        try {
            StockItem item = createStockItem(idStock, idSize, quantity, idProduct);
            mSaleItemAdapter.addSaleItem(item);
            //mSalesItems.add(item);
            totalSale += (item.getQuantity() * item.getPrice());
            updateTotal();

        } catch (Exception e) {

        }

    }

    public StockItem createStockItem(int idStock, int sizeId, int quantity,int idProduct) throws Exception {

        Uri stockItemUri = OpenClothesContract.Stock.buildStockUriProductSize();

        String selection = StockColumns.COLUMNS[StockColumnsOrder.COL_STOCK_ID] + " = ?";

        String[] selectionArgs = new String[]{ String.valueOf(idStock) };

        ContentResolver resolver = mContext.getContentResolver();

        Cursor cursor = resolver.query(stockItemUri,
                                       StockColumns.COLUMNS,
                                       selection,
                                       selectionArgs,
                                       null);


        if(!cursor.moveToFirst() || 0 == cursor.getCount()){
            throw  new Exception("Error can't find Stock item");
        }

        StockItem saleItem = getStockFromCursor(cursor);

        saleItem.setQuantity(quantity);

        //TODO : Check if the values are constant??

        cursor.close();

        return saleItem;
    }

    private static StockItem getStockFromCursor(Cursor data) {
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
        int idSize = data.getInt(StockColumnsOrder.COL_SIZE_ID);
        String descriptionSize = data.getString(StockColumnsOrder.COL_SIZE);
        stockItem.setSize(new SizeModel(idSize,descriptionSize));
        stockItem.setQuantity(data.getInt(StockColumnsOrder.COL_QUANTITY));
        stockItem.setPrice(data.getInt(StockColumnsOrder.COL_PRICE));

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

    private void showFragment(android.support.v4.app.DialogFragment dialogFragment, String TAG, int requestCode) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        dialogFragment.setTargetFragment(this, requestCode);
        dialogFragment.show(fm, TAG);
    }

    private void updateTotal(){

        mTextViewTotalSale.setText("$ " + String.valueOf(totalSale));

    }
}
