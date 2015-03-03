package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.provider.OpenClothesDatabase;
import com.mx.kanjo.openclothes.ui.ProductSpinnerAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.Lists;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DialogAddNewSaleItem extends DialogFragment implements  LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXTRA_ID_STOCK = "ID_PRODUCT";
    public static final String EXTRA_ID_SIZE = "ID_SIZE";
    public static final String EXTRA_QTY = "QUANTITY";
    public static final String EXTRA_INCOME_TYPE = "ID_INCOME";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProductSpinnerAdapter adapter;

    @InjectView(R.id.spin_model) Spinner mSpinnerProduct;
    @InjectView(R.id.spin_size) Spinner mSpinnerSize;
    @InjectView(R.id.et_quantity) EditText mEditTextQuantity;
    @InjectView(R.id.text_total_line) TextView mTextViewTotal;

    private Context context;

    private CatalogueManager mCatalogueManager;
    private ConfigurationInventoryManager mConfigInventoryManager;
    private ContentResolver mContentResolver;
    private ArrayList<SizeModel> listSize  = Lists.newArrayList();
    private Dictionary<Integer,SizeModel> sizeModelDictionary;
    private ArrayList<StockItem> listStockItems = Lists.newArrayList();
    StockItem selectedStockItem  = null;
    public static final String TAG ="com.mx.kanjo.openclothes.ui.fragments.DialogAddStockItem";
    private static final int LOADER_STOCK = 999;
    private static final int LOADER_ALL_SIZE = 998;


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

    private interface SizeColums {

        public static String[] COLUMNS = {
            OpenClothesContract.Size._ID,
            OpenClothesContract.Size.SIZE
        };

    }

    private interface SizeColumsOrder{
        public static final int COL_SIZE_ID = 0;
        public static final int COL_SIZE_DESCRIPTION = 1;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = null;
        String selection =  null ;
        String [] selectionArgs =null;// { "1" };

        switch (id){
            case LOADER_STOCK :
            {

                Uri stockUri = OpenClothesContract.Stock.buildStockUriProductSize();

                return new CursorLoader( getActivity(),
                        stockUri,
                        StockColumns.COLUMNS,
                        selection,
                        selectionArgs,
                        sortOrder );

            }
            case LOADER_ALL_SIZE :
            {
                Uri sizeUri = OpenClothesContract.Size.CONTENT_URI;

                return new CursorLoader( getActivity(),
                        sizeUri,
                        SizeColums.COLUMNS,
                        selection,
                        selectionArgs,
                        sortOrder );

            }
            default : return  null;

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (  loader.getId()  ){

            case LOADER_STOCK :
            {
                int index = 0;

                ArrayList<LeanProductModel> listProduct = Lists.newArrayList();

                listStockItems.clear();

                StockItem tempStockItem;

                if( data.getCount() == 0 )
                    return;

                if (!data.moveToFirst())
                    return;
                do
                {

                    tempStockItem = getStockFromCursor(data);

                    listStockItems.add(tempStockItem);

                    addDistinctProduct(index, listProduct, tempStockItem);

                    index++;
                }while (data.moveToNext());


                adapter  = new ProductSpinnerAdapter(getActivity(), listProduct, build());

                mSpinnerProduct.setAdapter(adapter);

                mSpinnerProduct.setOnItemSelectedListener(productItemClickListener);

                return;

            }
            case LOADER_ALL_SIZE :
            {
                sizeModelDictionary = new Hashtable<>();

                if( data.getCount() == 0 )
                    return;

                if (!data.moveToFirst())
                    return;

                SizeModel tempSizeMode ;

                do
                {
                    tempSizeMode = getSizeFromCursor(data);

                    sizeModelDictionary.put(tempSizeMode.getIdSize(),tempSizeMode);

                }while (data.moveToNext());

                return;

            }


        }


    }

    private void addDistinctProduct(int index, ArrayList<LeanProductModel> listProduct, StockItem tempStockItem) {
        if(index==0) {

            listProduct.add(createLean(tempStockItem));

        }
        else{

            if(listProduct.get(listProduct.size()-1).ID != tempStockItem.getIdProduct() )
                listProduct.add(createLean(tempStockItem) );

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogAddStockItem.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogAddNewSaleItem newInstance(String param1, String param2) {
        DialogAddNewSaleItem fragment = new DialogAddNewSaleItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogAddNewSaleItem() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog( savedInstanceState );
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_add_sale_item, container, false);
        ButterKnife.inject(this,view);
        return view;


    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset( this );
    }

    protected AdapterView.OnItemSelectedListener productItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateSizeSpinner((int) adapter.getItemId(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    protected AdapterView.OnItemSelectedListener sizeItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String size = (String) parent.getAdapter().getItem(position);
            findStockItem(size);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    protected View.OnFocusChangeListener textQuantityFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if(hasFocus){

                if(!TextUtils.isEmpty(mEditTextQuantity.getText()) && TextUtils.isDigitsOnly(mEditTextQuantity.getText())){

                    int quantity = Integer.parseInt(mEditTextQuantity.getText().toString());

                    if(quantity>0){

                        if(null != selectedStockItem ){

                            int total = quantity * selectedStockItem.getPrice();

                            mTextViewTotal.setText(String.valueOf(total));
                        }

                    }
                }
            }

        }
    };



    public static LeanProductModel createLean(StockItem item){

        LeanProductModel lean = new LeanProductModel();
        lean.Price = item.getPrice();
        lean.Model = item.getModel();
        lean.ImagePath = item.getImagePath();
        lean.ID = item.getIdProduct();
        return lean;

    }

    private void init() {
        context = getActivity();
        mCatalogueManager = new CatalogueManager(context);
        mConfigInventoryManager = new ConfigurationInventoryManager(context);
        mContentResolver = context.getContentResolver();

        getLoaderManager().initLoader(LOADER_STOCK, null, this);
        getLoaderManager().initLoader(LOADER_ALL_SIZE, null, this);
        mEditTextQuantity.setOnFocusChangeListener(textQuantityFocusListener);
        mSpinnerSize.setOnItemSelectedListener(sizeItemClickListener);


    }

    private ConfigImageHelper build()
    {

        if(UiUtils.isTablet(context)){
            return new ConfigImageHelper.ConfigImageHelpBuilder(new Pair<>(60,60))
                    .withRoundImage(true)
                    .build();
        } else {
            return new ConfigImageHelper.ConfigImageHelpBuilder(new Pair<>(36,36))
                    .withRoundImage(true)
                    .build();
        }
    }

    private  void populateSizeSpinner(int idProduct)
    {

        //Fetch all stock that have that id Product
        ArrayList<String> simpleList = Lists.newArrayList();
        SizeModel size;

        for(StockItem stockItem : listStockItems){

            if(stockItem.getIdProduct() == idProduct){

                size =  sizeModelDictionary.get(stockItem.getSize().getIdSize());
                //TODO: refactor
                simpleList.add(size.getSizeDescription());

            }

        }

        ArrayAdapter<String> sizeAdapter
                = new ArrayAdapter<>(context, R.layout.view_item_spinner, simpleList);

        mSpinnerSize.setAdapter(sizeAdapter);


    }

    private void findStockItem(String item)
    {

        ArrayList<SizeModel> listSize = Collections.list(sizeModelDictionary.elements());

        SizeModel selectedSize = null;

        for(SizeModel s : listSize){

            if(s.getSizeDescription().equals(item)){

                selectedSize = s;
            }

        }

        selectedStockItem = findStockItem(selectedSize.getIdSize(), mSpinnerProduct.getSelectedItemId());

    }

    private StockItem findStockItem(int idSize, long idProduct){

        for(StockItem item : listStockItems){

            if(item.getIdProduct() == idProduct && item.getSize().getIdSize() == idSize){
                return item;
            }

        }
        return null;


    }

    @OnClick({R.id.btn_add_new_sale_item,R.id.btn_discard_stock_item})
    public void onClickEvent(Button button)
    {
        switch (button.getId())
        {
            case R.id.btn_add_stock_item :
                sendResult(Activity.RESULT_OK);
                break;

            case R.id.btn_discard_stock_item :
                sendResult(Activity.RESULT_CANCELED);
                break;
        }
    }

    private void sendResult(int resultCode) {

        if (null == getTargetFragment())
            return;

        String textQty = mEditTextQuantity.getText().toString();

        if ( !isQuantityCompliant(textQty) && resultCode == Activity.RESULT_OK ) {
            showMessage();
            return;
        }

        Intent i  = getIntentExtras();

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

        dismiss();

    }

    public Intent getIntentExtras()
    {
        Intent i = new Intent();

        int idProduct = (int) mSpinnerProduct.getSelectedItemId();
        int idSize = listSize.get(mSpinnerSize.getSelectedItemPosition()).getIdSize();
        int qty = Integer.parseInt(mEditTextQuantity.getText().toString());

        i.putExtra( EXTRA_ID_STOCK, idProduct );
        i.putExtra( EXTRA_ID_SIZE, idSize );
        i.putExtra( EXTRA_QTY, qty);

        return  i;

    }

    private void showMessage() {
        Toast.makeText(context, getString(R.string.message_quantity_validation),Toast.LENGTH_SHORT).show();
    }

    private static boolean isQuantityCompliant(String qtyText) {
        if(TextUtils.isEmpty(qtyText))
            return false;
        try
        {
            Integer.parseInt(qtyText);
            return true;
        }
        catch (Exception e) {
            return false;
        }
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

        return stockItem;
    }

    private static SizeModel getSizeFromCursor(Cursor data) {

        SizeModel sizeModel = new SizeModel();

        sizeModel.setIdSize(data.getInt(SizeColumsOrder.COL_SIZE_ID));
        sizeModel.setSizeDescription(data.getString(SizeColumsOrder.COL_SIZE_DESCRIPTION));

        return sizeModel;
    }



}
