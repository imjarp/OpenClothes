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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.view.WindowManager;

import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.ui.ProductSpinnerAdapter;
import com.mx.kanjo.openclothes.util.Lists;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DialogAddStockItem extends DialogFragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXTRA_ID_PRODUCT = "ID_PRODUCT";
    public static final String EXTRA_ID_SIZE = "ID_SIZE";
    public static final String EXTRA_QTY = "QUANTITY";

    int idProduct = 0;
    int idSize = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProductSpinnerAdapter modelAdapter ;


    @InjectView(R.id.spin_model) Spinner mSpinnerProduct;
    @InjectView(R.id.spin_size) Spinner mSpinnerSize;
    @InjectView(R.id.et_quantity) EditText mEditTextQuantity;

    private CatalogueManager mCatalogueManager;
    private ConfigurationInventoryManager mConfigInventoryManager;
    private ContentResolver mContentResolver;
    private ArrayList<SizeModel> listSize  = Lists.newArrayList();

    public static final String TAG ="com.mx.kanjo.openclothes.ui.fragments.DialogAddStockItem";

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
     * @return A new instance of fragment DialogAddStockItem.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogAddStockItem newInstance(String param1, String param2) {
        DialogAddStockItem fragment = new DialogAddStockItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogAddStockItem() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog( savedInstanceState );
        dialog.getWindow().requestFeature( Window.FEATURE_NO_TITLE );
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
        mSpinnerProduct.setAdapter(modelAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_add_stock_item, container, false);
        ButterKnife.inject(this,view);
        return view;


    }

    //TODO : Refactor this to search products not in the UI Thread
    public ArrayList<LeanProductModel> getProducts()
    {

        ArrayList<LeanProductModel> result =  Lists.newArrayList();

        String sortOrder = OpenClothesContract.Product.MODEL + " ASC ";

        Uri productsUri = OpenClothesContract.Product.CONTENT_URI;

        String selection =  OpenClothesContract.Product.IS_ACTIVE + " = ? " ;

        //Active
        String [] selectionArgs = { "1" };

        Cursor cursor = mContentResolver.query(productsUri,
                                               ProductColumns.COLUMNS,
                                               selection,
                                               selectionArgs,
                                               sortOrder);

        if( cursor.getCount() == 0 )
            return result;

        if (!cursor.moveToFirst())
            return result;
        do
        {
            result.add(getFromCursor(cursor));
        }while (cursor.moveToNext());

        return result;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String stop = "";

        stop = stop;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private static LeanProductModel getFromCursor(Cursor cursor)
    {
        LeanProductModel model = new LeanProductModel();
        model.ID = cursor.getInt(ProductColumnsOrder.COL_PRODUCT_ID);
        model.Model = cursor.getString(ProductColumnsOrder.COL_PRODUCT_MODEL);
        model.Price  = cursor.getInt( ProductColumnsOrder.COL_PRICE);
        model.ImagePath =  ! cursor.isNull(ProductColumnsOrder.COL_IMAGE_PATH) ?
                Uri.parse(cursor.getString(ProductColumnsOrder.COL_IMAGE_PATH)):
                null;



        return model;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset( this );
    }

    private void init() {
        Context context = getActivity();
        mCatalogueManager = new CatalogueManager(context);
        mConfigInventoryManager = new ConfigurationInventoryManager(context);
        mContentResolver = context.getContentResolver();
        modelAdapter = new ProductSpinnerAdapter(context,getProducts());

        populateSizeSpinner(context);

    }

    private  void populateSizeSpinner(Context context )
    {
        listSize = (ArrayList<SizeModel>) mConfigInventoryManager.getSizeCatalogue();

        ArrayList<String> simpleList = new ArrayList<>(listSize.size());

        for( SizeModel size : listSize )
        {
            simpleList.add(size.getSizeDescription());
        }

        ArrayAdapter<String> sizeAdapter
                = new ArrayAdapter<>(context, R.layout.view_size_spinner, simpleList);

        mSpinnerSize.setAdapter(sizeAdapter);

    }

    @OnClick({R.id.btn_add_stock_item,R.id.btn_discard_stock_item})
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

        if(!isQuantityCompliant(mEditTextQuantity.getText().toString())) {
            showMessage();
            return;
        }

        Intent i  = new Intent();

        int idProduct = (int) mSpinnerProduct.getSelectedItemId();
        int idSize = listSize.get(mSpinnerSize.getSelectedItemPosition()).getIdSize();
        int qty = Integer.parseInt(mEditTextQuantity.getText().toString());

        i.putExtra( EXTRA_ID_PRODUCT, idProduct );
        i.putExtra( EXTRA_ID_SIZE, idSize );
        i.putExtra( EXTRA_QTY, qty);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

        dismiss();

    }

    private void showMessage() {
        Toast.makeText(getActivity(),getString(R.string.message_quantity_validation),Toast.LENGTH_SHORT).show();
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


}
