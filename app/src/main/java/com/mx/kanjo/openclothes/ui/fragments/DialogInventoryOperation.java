package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.engine.InventoryManager;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.ui.ProductSpinnerAdapter;
import com.mx.kanjo.openclothes.util.Lists;
import com.mx.kanjo.openclothes.util.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * Created by JARP on 23/02/2015.
 */
public class DialogInventoryOperation extends DialogFragment {


    Spinner mSpinnerModel;
    Spinner mSpinnerOperationInventory;
    Spinner mSpinnerSize;
    TextView mTextboxQty;
    Context mContext;
    ProductSpinnerAdapter productItemAdapter;

    CatalogueManager mCatalogueManager;
    InventoryManager mInventoryManager;
    ConfigurationInventoryManager mConfigurationInventoryManager ;

    ArrayList<OutcomeType> outcomeTypes;

    //Defines wether this operation is Incoming or Outgoing from the inventory
    private static final String PARAM_TYPE_OPERATION = "TYPE_OPERATION";
    private static int operation = 0;

    public static DialogFragment createInstance(String param, int typeOperation)
    {
        DialogInventoryOperation  dialog = new DialogInventoryOperation();
        Bundle args = new Bundle();
        args.putInt(PARAM_TYPE_OPERATION, typeOperation);
        dialog.setArguments(args);
        return  dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( null != getArguments() )
        {
            operation = getArguments().getInt(PARAM_TYPE_OPERATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null; //= inflater.inflate()
        ButterKnife.inject(this,view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog( savedInstanceState );
        dialog.getWindow().requestFeature( Window.FEATURE_NO_TITLE );
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void init() {
        mContext = getActivity();
        mCatalogueManager = new CatalogueManager(mContext);
        mInventoryManager = new InventoryManager(mContext);
        mConfigurationInventoryManager = new ConfigurationInventoryManager(mContext);
        populateSpinModel();
        populateSpinOperationType();

    }

    private void populateSpinOperationType() {

        outcomeTypes = new ArrayList<>(mConfigurationInventoryManager.getOutcomeTypes());

        ArrayList<String> outcomeList = new ArrayList<>( outcomeTypes.size() );

        for ( OutcomeType item : outcomeTypes   )
        {
            outcomeList.add( item.getDescription() );
        }

        ArrayAdapter<String> outcomeAdapter = new ArrayAdapter<>(mContext, R.layout.view_size_spinner, outcomeList);

        mSpinnerOperationInventory.setAdapter(outcomeAdapter);

    }

    private void populateSpinModel() {

        //We will get only the item stocks that have at least one product
        Set<StockItem> setItemStock =  mInventoryManager.getStock();
        HashMap<Integer,StockItem> tempHashMap = Maps.newHashMap();
        ArrayList<LeanProductModel> listLeanProducts = Lists.newArrayList();
        for(StockItem item : setItemStock){
            if( null== tempHashMap.get(item.getIdProduct()))
            {
                tempHashMap.put(item.getIdProduct(),item);
                listLeanProducts.add(createLeanProduct(item));
            }
        }

        productItemAdapter = new ProductSpinnerAdapter(mContext,listLeanProducts);
        mSpinnerModel.setAdapter(productItemAdapter);

    }


    private void populateSpinSize()
    {

    }

    private static LeanProductModel createLeanProduct(StockItem item)
    {
        LeanProductModel leanProductModel = new LeanProductModel();
        leanProductModel.ID = item.getIdProduct();
        leanProductModel.ImagePath = item.getImagePath();
        leanProductModel.Model = item.getModel();
        leanProductModel.Price = item.getPrice();
        return leanProductModel;
    }
}
