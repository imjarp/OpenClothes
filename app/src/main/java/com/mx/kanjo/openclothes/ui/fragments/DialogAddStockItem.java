package com.mx.kanjo.openclothes.ui.fragments;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.ui.ModelSpinnerAdapter;
import com.mx.kanjo.openclothes.util.Lists;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DialogAddStockItem extends DialogFragment implements AdapterViewCompat.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ModelSpinnerAdapter modelAdapter ;


    @InjectView(R.id.spin_model) Spinner mSpinnerModel;
    //@InjectView(R.id.spin_size) Spinner mSpinnerSize;
    @InjectView(R.id.et_quantity) EditText mEditTextQuantity;

    private CatalogueManager mCatalogueManager;
    private ConfigurationInventoryManager mConfigInventoryManager;
    private ContentResolver mContentResolver;

    public static final String TAG ="com.mx.kanjo.openclothes.ui.fragments.DialogAddStockItem";


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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();
        mCatalogueManager = new CatalogueManager(context);
        mConfigInventoryManager = new ConfigurationInventoryManager(context);
        mContentResolver = context.getContentResolver();
        modelAdapter = new ModelSpinnerAdapter(context,getProducts());

    }

    @Override
    public void onResume() {
        super.onResume();
        mSpinnerModel.setAdapter(modelAdapter);
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
    public void onItemSelected(AdapterViewCompat<?> adapterViewCompat, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterViewCompat<?> adapterViewCompat) {

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
}
