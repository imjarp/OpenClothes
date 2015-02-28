package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.ConfigurationInventoryManager;
import com.mx.kanjo.openclothes.engine.InventoryManager;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.model.OutcomeType;
import com.mx.kanjo.openclothes.model.SizeModel;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.ui.ProductSpinnerAdapter;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.Lists;
import com.mx.kanjo.openclothes.util.Maps;
import com.mx.kanjo.openclothes.util.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by JARP on 23/02/2015.
 */
public class DialogInventoryOperation extends DialogFragment {

    public static final String TAG = "DialogInventoryOperation";

    public static final String EXTRA_ID_PRODUCT = "ID_PRODUCT";
    public static final String EXTRA_ID_SIZE = "ID_SIZE";
    public static final String EXTRA_QTY = "QTY";
    public static final String EXTRA_ID_OUTCOME = "ID_OUTCOME";

    @InjectView(R.id.spin_model) Spinner mSpinnerModel;
    @InjectView(R.id.spin_type) Spinner mSpinnerOperationInventory;
    @InjectView(R.id.spin_size) Spinner mSpinnerSize;
    @InjectView(R.id.et_quantity) EditText mEditboxQty;

    Context mContext;
    ProductSpinnerAdapter productItemAdapter;

    CatalogueManager mCatalogueManager;
    InventoryManager mInventoryManager;
    ConfigurationInventoryManager mConfigurationInventoryManager ;

    ArrayList<OutcomeType> outcomeTypes;
    Set<StockItem> setItemStock;
    ArrayList<LeanProductModel> listLeanProducts = Lists.newArrayList();
    ArrayList<SizeModel> sizeModelArrayList = Lists.newArrayList();

    //Defines whether this operation is Incoming or Outgoing from the inventory
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
        View view = inflater.inflate(R.layout.fragment_dialog_inventory_operation,container,false);
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

    @OnClick({R.id.btn_add_outcome,R.id.btn_discard_outcome})
    public void onClickEvent(View view){

        switch ( view.getId() ) {
            case R.id.btn_add_outcome :
                sendResult(Activity.RESULT_OK);
                break;
            case R.id.btn_discard_outcome :
                sendResult(Activity.RESULT_CANCELED);
                break;
            default : return;
        }

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

        ArrayAdapter<String> outcomeAdapter = new ArrayAdapter<>(mContext, R.layout.view_item_spinner, outcomeList);

        mSpinnerOperationInventory.setAdapter(outcomeAdapter);

    }

    private void populateSpinModel() {

        //We will get only the item stocks that have at least one product
        setItemStock =  mInventoryManager.getStock();
        HashMap<Integer,StockItem> tempHashMap = Maps.newHashMap();

        for(StockItem item : setItemStock){
            if( null== tempHashMap.get(item.getIdProduct()))
            {
                tempHashMap.put(item.getIdProduct(),item);
                listLeanProducts.add(createLeanProduct(item));
            }
        }

        productItemAdapter = new ProductSpinnerAdapter(mContext,listLeanProducts,build());

        mSpinnerModel.setAdapter(productItemAdapter);

        mSpinnerModel.setOnItemSelectedListener(productItemClickListener);



    }

    private ConfigImageHelper build()
    {

        if(UiUtils.isTablet(mContext)){
            return new ConfigImageHelper.ConfigImageHelpBuilder(new Pair<>(60,60))
                    .withRoundImage(true)
                    .build();
        } else {
            return new ConfigImageHelper.ConfigImageHelpBuilder(new Pair<>(24,24))
                    .withRoundImage(true)
                    .build();
        }
    }

    protected AdapterView.OnItemSelectedListener productItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            populateSpinSize(listLeanProducts.get(position).ID);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void populateSpinSize(int idProduct){

        StockItem temp;
        sizeModelArrayList.clear();
        Iterator<StockItem> stockItemIterator =  setItemStock.iterator();
        ArrayList<String> arraySizeDescriptions = Lists.newArrayList();

        while (stockItemIterator.hasNext())
        {
            temp =stockItemIterator.next();

            if( idProduct == temp.getIdProduct())
            {
                sizeModelArrayList.add( temp.getSize() );
                arraySizeDescriptions.add( temp.getSize().getSizeDescription() );

            }

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.view_item_spinner, arraySizeDescriptions);


        mSpinnerSize.setAdapter(adapter);




    }

    private static LeanProductModel createLeanProduct(StockItem item){
        LeanProductModel leanProductModel = new LeanProductModel();
        leanProductModel.ID = item.getIdProduct();
        leanProductModel.ImagePath = item.getImagePath();
        leanProductModel.Model = item.getModel();
        leanProductModel.Price = item.getPrice();
        return leanProductModel;
    }

    private void sendResult(int resultCode) {

        if (null == getTargetFragment())
            return;

        String textQty = mEditboxQty.getText().toString();

        if ( !isQuantityCompliant(textQty) && resultCode == Activity.RESULT_OK ) {
            showMessage();
            return;
        }

        Intent i = getIntentExtras();

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

        dismiss();

    }

    public Intent getIntentExtras() {
        Intent intentExtras = new Intent();

        //mSpinnerModel
        int idProduct = (int) mSpinnerModel.getSelectedItemId();
        int idSize = sizeModelArrayList.get(mSpinnerSize.getSelectedItemPosition()).getIdSize();
        int qty = Integer.parseInt(mEditboxQty.getText().toString());
        int idOutcomeType =  outcomeTypes.get(mSpinnerOperationInventory.getSelectedItemPosition()).getIdOutcome();

        intentExtras.putExtra( EXTRA_ID_PRODUCT, idProduct );
        intentExtras.putExtra( EXTRA_ID_SIZE, idSize );
        intentExtras.putExtra( EXTRA_QTY, qty);
        intentExtras.putExtra( EXTRA_ID_OUTCOME, idOutcomeType);

        return intentExtras;
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

    private void showMessage() {
        Toast.makeText(getActivity(), getString(R.string.message_quantity_validation), Toast.LENGTH_SHORT).show();
    }
}
