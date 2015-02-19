package com.mx.kanjo.openclothes.ui.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;

import butterknife.InjectView;


public class DialogAddStockItem extends DialogFragment implements AdapterViewCompat.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @InjectView(R.id.spin_model) Spinner mSpinnerModel;
    //@InjectView(R.id.spin_size) Spinner mSpinnerSize;
    @InjectView(R.id.et_quantity) EditText mEditTextQuantity;

    private CatalogueManager mCatalogueManager;
    private ConfigurationInventoryManager mConfigInventoryManager;


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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_add_stock_item, container, false);

        return view;


    }




    @Override
    public void onItemSelected(AdapterViewCompat<?> adapterViewCompat, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterViewCompat<?> adapterViewCompat) {

    }
}
