package com.mx.kanjo.openclothes.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.engine.CatalogueManager;
import com.mx.kanjo.openclothes.engine.creators.ProductCreator;
import com.mx.kanjo.openclothes.model.ProductModel;
import com.mx.kanjo.openclothes.provider.OpenClothesContract;
import com.mx.kanjo.openclothes.util.StorageUtil;
import com.mx.kanjo.openclothes.util.UiUtils;
import com.squareup.picasso.Picasso;

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

    public final static String KEY_ID_PRODUCT = "idProduct";
    private static final int REQUEST_PICK_IMAGE = 1001;

    private OnFragmentProductListener mProductListener;

    private String pathFile, model, price, description, cost;
    //Keep the value if its is used for update
    private String originalModel;
    private Uri uriImage;
    Boolean isActiveProduct;
    Boolean updateProduct = false;

    private CatalogueManager mCatalogueManager;
    private Context mContext;
    private int idProduct;

    @InjectView(R.id.edit_product_model)EditText editTextModel;

    @InjectView(R.id.edit_product_cost) EditText editTextCost;

    @InjectView(R.id.edit_product_price) EditText editTextPrice;

    @InjectView(R.id.edit_product_description) EditText editTextDescription;

    @InjectView(R.id.check_product_active) CheckBox checkBoxActiveProduct;

    @InjectView(R.id.img_new_product)ImageButton imageProduct;

    @InjectView(R.id.progress_model) ProgressBar progressModel;

    Button mBtnMenuAction;

    Picasso picasso ;

    float dimen ;


    private interface  KeyState{

        String KEY_PATH_FILE = "keyPathFile";
        String KEY_MODEL = "keyModel";
        String KEY_PRICE = "keyPrice";
        String KEY_DESCRIPTION = "keyDescription";
        String KEY_IS_ACTIVE_PRODUCT = "keyIsActiveProduct";
        String KEY_COST = "keyCost";
    }

    public interface OnFragmentProductListener{
        public void onAddProductClick(ProductModel productModel);
        public void onUpdateProduct(ProductModel productModel);
    }

    public ProductFragment(){
    }

    public static ProductFragment createUpdateFragment(int idProduct){

        ProductFragment fragment = new ProductFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_ID_PRODUCT, idProduct);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);

        mCatalogueManager = new CatalogueManager(activity);

        mContext = activity;
        try {
            mProductListener = (OnFragmentProductListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentProductListener");
        }

        picasso  = Picasso.with(mContext);

        int imageViewDimension = UiUtils.isTablet(mContext) ?  192 : 144 ;

        dimen  = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, imageViewDimension, getResources().getDisplayMetrics() );

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.fragment_product2, container, false);

        ButterKnife.inject(this, rootView);
        if( null != savedInstanceState)
            restoreSaveInstance(savedInstanceState);
        else{
            if (getArguments()!=null)
            {
                idProduct  = getArguments().getInt(KEY_ID_PRODUCT,-1);
                if (idProduct >0){
                    fetchProduct(rootView, idProduct);
                }
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        inflater.inflate(R.menu.menu_product, menu);

        MenuItem add =  menu.findItem(R.id.action_new_product);


        if(null != add.getActionView()) {

            mBtnMenuAction = (Button) add.getActionView().findViewById(R.id.btn_menu_add_new_product);

            if( idProduct > 0 && mBtnMenuAction != null ) {
                mBtnMenuAction.setText(getString(R.string.update_action));
            }
            mBtnMenuAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateProductModel();
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        saveKeysToInstance(outState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if( Activity.RESULT_OK != resultCode )
            return;

        if( REQUEST_PICK_IMAGE != requestCode )
            return;

        if ( null != data && null != ( uriImage = data.getData() ) )
        {
            setImage(uriImage);
        }

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProductListener = null;
    }

    @OnClick(R.id.img_new_product)
    public void onClickImageProduct(View view){
        startActivityForResult( Intent.createChooser(makeIntentPickImage(),
                        getString(R.string.message_pick_image)),
                REQUEST_PICK_IMAGE);
    }

    private static Intent makeIntentPickImage(){
        Intent intentPickImage = new Intent();
        intentPickImage.setAction(Intent.ACTION_PICK);
        intentPickImage.setType("image/*");
        return intentPickImage;

    }

    private void onAddProductEvent(){
        mProductListener.onAddProductClick(createProductModel());
    }

    private void onUpdateProductEvent(){
        mProductListener.onUpdateProduct(createProductModel());
    }

    public void validateProductModel(){

        Notification resultValidation = new Notification();

        if(TextUtils.isEmpty(editTextCost.getText().toString()))
        {
            resultValidation.errors.add(getString(R.string.validation_cost_product));
            editTextCost.setError(getString(R.string.validation_cost_product));
        }

        if(TextUtils.isEmpty(editTextPrice.getText().toString()))
        {
           resultValidation.errors.add(getString(R.string.validation_price_product));

            editTextPrice.setError(getString(R.string.validation_price_product));
        }

        if(TextUtils.isEmpty(editTextModel.getText().toString()))
        {
            resultValidation.errors.add(getString(R.string.validation_model_product));
            editTextModel.setError(getString(R.string.validation_model_product));
        }

        if(resultValidation.hasError())
            return;

        String currentModel = editTextModel.getText().toString();

        if( !updateProduct ||( updateProduct && !originalModel.equals( currentModel ) ) ) {
            executeValidationModelTask(currentModel);
            return;
        }

        onUpdateProductEvent();
        return;

    }

    private void executeValidationModelTask(String model) {

        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                toogleProgressModel();
            }

            @Override
            protected Boolean doInBackground(String... params) {

                String model = params[0];

                //Validate if the model exist
                if (null != mCatalogueManager.findProductByModel(model)) {
                    return true;
                }

                return false;
            }



            @Override
            protected void onPostExecute(Boolean aBoolean) {
                toogleProgressModel();
                setResultExistModelTask(aBoolean);
            }
        }.execute(new String[]{model});
    }

    private void saveKeysToInstance(Bundle outState){

        outState.putString(KeyState.KEY_COST, editTextCost.getText().toString());
        outState.putString(KeyState.KEY_DESCRIPTION, editTextDescription.getText().toString() );
        outState.putBoolean(KeyState.KEY_IS_ACTIVE_PRODUCT, checkBoxActiveProduct.isChecked());
        outState.putString( KeyState.KEY_MODEL , editTextModel.getText().toString() );
        outState.putParcelable( KeyState.KEY_PATH_FILE, uriImage );
        outState.putString( KeyState.KEY_PRICE , editTextPrice.getText().toString() );

    }

    private void restoreSaveInstance(Bundle savedInstanceState){

        String cost , description, model , price;
        boolean isActive ;
        cost =  savedInstanceState.getString(KeyState.KEY_COST);
        description =  savedInstanceState.getString(KeyState.KEY_DESCRIPTION);
        model =  savedInstanceState.getString(KeyState.KEY_MODEL);
        price =  savedInstanceState.getString(KeyState.KEY_PRICE);
        uriImage = savedInstanceState.getParcelable(KeyState.KEY_PATH_FILE);
        isActive = savedInstanceState.getBoolean(KeyState.KEY_IS_ACTIVE_PRODUCT);

        if(!TextUtils.isEmpty(cost)){ editTextCost.setText(cost); }
        if(!TextUtils.isEmpty(description)){ editTextDescription.setText(description); }
        if(!TextUtils.isEmpty(model)){ editTextModel.setText(model); }
        if(!TextUtils.isEmpty(price)){ editTextPrice.setText(price); }
        if(null != uriImage ){setImage(uriImage);}
        checkBoxActiveProduct.setChecked(isActive);

    }

    private void setImage(Uri uri){

        pathFile = StorageUtil.getPath(mContext, uri);

        picasso.load(uri)
                .resize((int)dimen,(int) dimen)
                .centerInside()
                .into(imageProduct);


    }

    private void fetchProduct(final View rootView, int idProduct){

        AsyncTask fetchProductTask = new AsyncTask<Integer, Void, ProductModel>() {
            @Override
            protected ProductModel doInBackground(Integer ... params) {

                Uri uriProduct = OpenClothesContract.Product.buildProductUri(params[0]);

                Cursor cursor =  mContext.getContentResolver().query(uriProduct,null,null,null,null);

                if(!cursor.moveToFirst())
                    return null;
                if(cursor.getCount() == 0)
                    return null;

                ProductModel model =  ProductCreator.getProductModelFromCursor(cursor);

                originalModel = model.getModel();

                uriImage = model.getImagePath();

                updateProduct = true;

                cursor.close();

                return model;
            }



            @Override
            protected void onPreExecute() {
                rootView.findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.view_scroll_product).setVisibility(View.GONE);


            }

            @Override
            protected void onPostExecute(ProductModel productModel) {
                super.onPostExecute(productModel);
                rootView.findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                rootView.findViewById(R.id.view_scroll_product).setVisibility(View.VISIBLE);
                if( null != productModel){

                    if (productModel.getCost()>0){ editTextCost.setText(String.valueOf(productModel.getCost())); }
                    if(!TextUtils.isEmpty(productModel.getDescription())){ editTextDescription.setText(productModel.getDescription()); }
                    if(!TextUtils.isEmpty(productModel.getModel())){ editTextModel.setText(productModel.getModel()); }
                    if(productModel.getPrice()>0){ editTextPrice.setText(String.valueOf(productModel.getPrice())); }
                    if(null != productModel.getImagePath() ){setImage(productModel.getImagePath());}

                }
            }
        };
        fetchProductTask.execute(new Integer[]{idProduct});


    }

    private void setResultExistModelTask(boolean existModel){

        if( !existModel && !updateProduct ){
            onAddProductEvent();
        }
        else if ( !existModel && updateProduct ){
            onUpdateProductEvent();
        }
        else{
            editTextModel.setError(getString(R.string.validation_model_duplicated));
        }

    }

    private void toogleProgressModel(){

        if(mBtnMenuAction !=null ){
            mBtnMenuAction.setEnabled( ! mBtnMenuAction.isEnabled() );
        }

        int visibility = progressModel.getVisibility();

        if(visibility == View.VISIBLE){
            progressModel.setVisibility(View.GONE);
        }
        else
        {
            progressModel.setVisibility(View.VISIBLE);
        }



    }

    public  ProductModel createProductModel(){
        description = editTextDescription.getText().toString();

        model = editTextModel.getText().toString();

        price = editTextPrice.getText().toString();

        cost = editTextCost.getText().toString();

        isActiveProduct = checkBoxActiveProduct.isChecked();

        ProductModel productModel = new ProductModel();
        productModel.setIdProduct( idProduct > 0 && updateProduct  ? idProduct : 0 );
        productModel.setDescription(description);
        productModel.setModel(model);
        productModel.setDateOperation(OpenClothesContract.getDbDateString(new Date()));
        productModel.setImagePath(uriImage);
        productModel.setActive(isActiveProduct);
        productModel.setPrice(Integer.parseInt(price));
        productModel.setCost( Integer.parseInt( cost ) );


        return productModel;

    }

    private class Notification{

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
