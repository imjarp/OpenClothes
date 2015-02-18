package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.mx.kanjo.openclothes.util.StorageUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductFragment extends Fragment {


    private OnFragmentProductListener mProductListener;

    private String pathFile, model, price, description, cost;
    private Uri uriImage;
    Boolean isActiveProduct;

    //TODO: Implement on screen change


    private static final String keyPathFile, keyModel, keyPrice, keyDescription, keyIsActiveProduct, keyCost;

    private boolean productCompliant;

    static {

        keyPathFile = "keyPathFile";
        keyModel = "keyModel";
        keyPrice = "keyPrice";
        keyDescription = "keyDescription";
        keyIsActiveProduct = "keyIsActiveProduct";
        keyCost = "keyCost";
    }

    @InjectView(R.id.edit_product_model)EditText editTextModel;

    @InjectView(R.id.edit_product_cost) EditText editTextCost;

    @InjectView(R.id.edit_product_price) EditText editTextPrice;

    @InjectView(R.id.edit_product_description) EditText editTextDescription;

    @InjectView(R.id.check_product_active) CheckBox checkBoxActiveProduct;

    @InjectView(R.id.img_new_product) ImageButton imageProduct;


    private static final int REQUEST_PICK_IMAGE = 1001;


    @OnClick(R.id.img_new_product)
    public void onClickImageProduct(View view)
    {
        startActivityForResult( Intent.createChooser(makeIntentPickImage(),
                                                    getString(R.string.message_pick_image)),
                                REQUEST_PICK_IMAGE);
    }

    private Intent makeIntentPickImage()
    {
        Intent intentPickImage = new Intent();
        intentPickImage.setAction(Intent.ACTION_PICK);
        intentPickImage.setType("image/*");
        return intentPickImage;

    }

    public ProductFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( Activity.RESULT_OK != resultCode )
            return;

        if( REQUEST_PICK_IMAGE != requestCode )
            return;

        if ( null != data && null != ( uriImage = data.getData() ) )
        {
            pathFile = StorageUtil.getPath(getActivity(), uriImage);

            //TODO: Validate image
            PictureUtils.setImageScaled(getActivity(), imageProduct, pathFile, PictureUtils.SizeImage.IMAGE_192x192);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProductListener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProductListener = (OnFragmentProductListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentProductListener");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product2, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_product, menu);

        MenuItem add =  menu.findItem(R.id.action_new_product);

        if(null != add.getActionView()) {
            (add.getActionView().findViewById(R.id.btn_menu_add_new_product)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isProductCompliant())
                        callParentActivity();
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void callParentActivity() {
        mProductListener.onAddProductClick(createProductModel());
    }


    public boolean isProductCompliant() {

        Notification resultValidation = new Notification();

        if(TextUtils.isEmpty(editTextCost.getText().toString()))
        {
            resultValidation.errors.add(getString(R.string.validation_cost_product));
        }

        if(TextUtils.isEmpty(editTextModel.getText().toString()))
        {
            resultValidation.errors.add(getString(R.string.validation_model_product));
        }

        if(TextUtils.isEmpty(editTextPrice.getText().toString()))
        {
            resultValidation.errors.add(getString(R.string.validation_price_product));
        }

        //TODO: Check if the model already exists
        
        if(resultValidation.hasError())
            showMessage(resultValidation);

        return !resultValidation.hasError();

    }

    private void showMessage(Notification resultValidation) {

        StringBuilder builderMessage = new StringBuilder();

        for (String error : resultValidation.errors)
            builderMessage.append(error + "\n");

        Toast.makeText(getActivity(), builderMessage.toString(), Toast.LENGTH_SHORT).show();

    }

    public interface OnFragmentProductListener {
        public void onAddProductClick(ProductModel productModel);
    }

    public  ProductModel createProductModel()
    {
        //model, price, description;
        description = editTextDescription.getText().toString();

        model = editTextModel.getText().toString();

        price = editTextPrice.getText().toString();

        cost = editTextCost.getText().toString();

        isActiveProduct = checkBoxActiveProduct.isChecked();

        ProductModel productModel = new ProductModel();
        productModel.setIdProduct(0);
        //productModel.setName();
        productModel.setDescription(description);
        productModel.setModel(model);
        productModel.setDateOperation(OpenClothesContract.getDbDateString(new Date()));
        productModel.setImagePath(uriImage);
        productModel.setActive(isActiveProduct);
        productModel.setPrice(Integer.parseInt(price));
        productModel.setCost( Integer.parseInt( cost ) );

        return productModel;

    }

    private class Notification
    {

        public Notification()
        {
            errors = new ArrayList<>();
        }

        public Boolean  hasError()
        {
            return ! errors.isEmpty();
        }

        public List<String> errors;

    }

}
