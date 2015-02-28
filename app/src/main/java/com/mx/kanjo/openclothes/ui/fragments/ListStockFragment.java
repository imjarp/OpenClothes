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
import com.mx.kanjo.openclothes.engine.InventoryManager;
import com.mx.kanjo.openclothes.model.IncomeModel;
import com.mx.kanjo.openclothes.model.IncomeType;
import com.mx.kanjo.openclothes.model.OutcomeModel;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.provider.OpenClothesDatabase;
import com.mx.kanjo.openclothes.ui.StockAdapter;
import com.mx.kanjo.openclothes.util.Lists;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ListStockFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{


    private interface StockColumns{
        public static String [] COLUMNS = {
                OpenClothesDatabase.Tables.STOCK + "." +  OpenClothesContract.Stock._ID,
                OpenClothesDatabase.Tables.PRODUCT + "." +  OpenClothesContract.Product._ID,
                OpenClothesContract.Product.MODEL,
                OpenClothesContract.Product.IMAGE_PATH,
                OpenClothesDatabase.Tables.SIZE + "." +  OpenClothesContract.Size._ID,
                OpenClothesDatabase.Tables.SIZE + "." +  OpenClothesContract.Size.SIZE,
                OpenClothesDatabase.Tables.STOCK + "." +  OpenClothesContract.Stock.QUANTITY,

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


    }

    private static final int LOADER_STOCK = 998;

    private static final int REQUEST_NEW_STOCK = 1;
    private static final int REQUEST_REMOVE_STOCK = 2;

    private static final String TAG = ListStockFragment.class.getSimpleName();

    private static final int SPAN_COUNT = 1;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;

    protected LayoutManagerType mCurrentLayoutManagerType;

    private LinearLayoutManager mLinearLayoutManager;

    protected RecyclerView.LayoutManager mLayoutManager;

    private StockAdapter adapter;

    private ArrayList<StockItem> stockItems = Lists.newArrayList();

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockFragment.
     */
    public static ListStockFragment newInstance(String param1, String param2) {
        ListStockFragment fragment = new ListStockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListStockFragment() {
        // Required empty public constructor

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
        int idSize = data.getInt(StockColumnsOrder.COL_SIZE_ID);
        String descriptionSize = data.getString(StockColumnsOrder.COL_SIZE);
        stockItem.setSize(new SizeModel(idSize,descriptionSize));
        stockItem.setQuantity(data.getInt(StockColumnsOrder.COL_QUANTITY));

        return stockItem;
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
        View view =  inflater.inflate(R.layout.fragment_stocklist, container, false);

        view.setTag(TAG);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER ;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        ButterKnife.inject(this,view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(Activity.RESULT_OK != resultCode)
            return;

        processResult(requestCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_STOCK, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_stock_list,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.outgoing_stock :
                showFragment( DialogInventoryOperation.createInstance("" , 0 ) , DialogInventoryOperation.TAG, REQUEST_REMOVE_STOCK );
                break   ;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String sortOrder = null;

        Uri stockUri = OpenClothesContract.Stock.buildStockUriProductSize();

        String selection =  null ;

        //Active
        String [] selectionArgs =null;// { "1" };


        return new CursorLoader( getActivity(),
                                 stockUri,
                                 StockColumns.COLUMNS,
                                 selection,
                                 selectionArgs,
                                 sortOrder );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        stockItems.clear();

        if( data.getCount() == 0 )
            return;

        if (!data.moveToFirst())
            return;
        do
        {
            stockItems.add(getFromCursor(data));
        }while (data.moveToNext());

        adapter = new StockAdapter(getActivity(),stockItems);
        if( null != mRecyclerView)
            mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        stockItems.clear();
    }

    @OnClick(R.id.btnCreateStock)
    public void onClickButtonListener(View view)
    {
        showFragment(DialogAddStockItem.newInstance("",""), DialogAddStockItem.TAG, REQUEST_NEW_STOCK);
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

    private void addToStock(Intent data) {
        InventoryManager manager = new InventoryManager( getActivity() );
        int size = data.getIntExtra( DialogAddStockItem.EXTRA_ID_SIZE, -1 ) ;
        int idProduct = data.getIntExtra( DialogAddStockItem.EXTRA_ID_PRODUCT, -1 ) ;
        int qty = data.getIntExtra( DialogAddStockItem.EXTRA_QTY, -1 );
        int idIncomeType = data.getIntExtra(DialogAddStockItem.EXTRA_INCOME_TYPE,-1);

        insertNewStockItem(idProduct, size, qty, manager);
        insertIncomeType( idProduct, size , qty, idIncomeType, manager);

    }

    private void insertNewStockItem(int idProduct, int idSize, int qty, InventoryManager manager) {

        if( idProduct < 0  || idSize < 0 || qty < 0 )
            return;

        manager.addItemToStock(createSpecificStock(idProduct,idSize,qty));
    }

    private void insertIncomeType(int idProduct, int idSize, int qty, int idIncomeType, InventoryManager manager){

        if( idProduct < 0  || idSize < 0 || qty < 0 || idIncomeType < 0 )
            return;

        manager.addIncome(createSpecificIncomeType(idProduct, idSize, qty , idIncomeType));
    }

    private  static StockItem createSpecificStock(int idProduct,int idSize, int qty){

        StockItem item = new StockItem();
        item.setQuantity(qty);
        SizeModel sizeModel = new SizeModel();
        sizeModel.setIdSize(idSize);
        item.setSize(sizeModel);
        item.setIdProduct(idProduct);
        return item;

    }

    private static IncomeModel createSpecificIncomeType(int idProduct, int idSize, int qty , int idIncomeType){
        IncomeModel incomeModel = new IncomeModel();
        IncomeType type = new IncomeType();
        SizeModel sizeModel = new SizeModel();

        type.setIdIncome(idIncomeType);
        sizeModel.setIdSize(idSize);

        incomeModel.setIncomeType(type);
        incomeModel.setSize(sizeModel);

        incomeModel.setIdProduct(idProduct);
        incomeModel.setQuantity(qty);
        incomeModel.setDateOperation( OpenClothesContract.getDbDateString( new Date() ) );

        return incomeModel;

    }

    private void removeFromStock(Intent data) {
        InventoryManager manager = new InventoryManager( getActivity() );
        int size = data.getIntExtra( DialogInventoryOperation.EXTRA_ID_SIZE, -1 ) ;
        int idProduct = data.getIntExtra( DialogInventoryOperation.EXTRA_ID_PRODUCT, -1 ) ;
        int qty = data.getIntExtra( DialogInventoryOperation.EXTRA_QTY, -1 );
        int idIncomeType = data.getIntExtra(DialogInventoryOperation.EXTRA_ID_OUTCOME,-1);


        removeStockItem(idProduct, size, qty, manager);
        insertOutcomeType(idProduct, size, qty, idIncomeType, manager);

    }

    private void removeStockItem(int idProduct, int idSize, int qty, InventoryManager manager) {

        if( idProduct < 0  || idSize < 0 || qty < 0 )
            return;

        manager.removeItemFromStock(createSpecificStock(idProduct,idSize,qty));
    }

    private void insertOutcomeType(int idProduct, int idSize, int qty, int idOutcomeType, InventoryManager manager){

        if( idProduct < 0  || idSize < 0 || qty < 0 || idOutcomeType < 0 )
            return;

        manager.addOutcome(createSpecificOutcome(idProduct, idSize, qty, idOutcomeType));
    }

    private static OutcomeModel createSpecificOutcome(int idProduct, int idSize, int qty , int idOutcomeType){
        OutcomeModel outcomeModel = new OutcomeModel();
        OutcomeType type = new OutcomeType();
        SizeModel sizeModel = new SizeModel();

        type.setIdOutcome(idOutcomeType);
        sizeModel.setIdSize(idSize);

        outcomeModel.setOutcomeType(type);
        outcomeModel.setSize(sizeModel);

        outcomeModel.setIdProduct(idProduct);
        outcomeModel.setQuantity(qty);
        outcomeModel.setDateOperation(OpenClothesContract.getDbDateString(new Date()));

        return outcomeModel;

    }

    private void processResult(int requestCode, Intent data) {

        this.onLoaderReset(null);

        switch (requestCode)
        {
            case REQUEST_NEW_STOCK :
                addToStock(data);
                break;
            case REQUEST_REMOVE_STOCK :
                removeFromStock(data);
                break;
        }

        getLoaderManager().restartLoader(LOADER_STOCK, null, this);

    }

}
