package com.mx.kanjo.openclothes.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.util.CircleTransform;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 2/18/15.
 */
public class ProductSpinnerAdapter implements SpinnerAdapter {

    private Context mContext;

    private ArrayList<LeanProductModel> list;

    LayoutInflater mInflater;

    Resources res;

    Picasso picasso;
    ConfigImageHelper mConfigImageHelper;
    CircleTransform mCircleTransform ;
    float widthPx,heightPx;

    private static LeanProductModel temp;

    public ProductSpinnerAdapter(Context context, ArrayList<LeanProductModel> list, ConfigImageHelper configImageHelper) {
        this.mContext = context;
        this.list = list;
        this.mInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = mContext.getResources();
        picasso = Picasso.with(context);
        mCircleTransform = new CircleTransform();
        this.mConfigImageHelper = configImageHelper;
        widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mConfigImageHelper.getSizeImage().first,
                context.getResources().getDisplayMetrics());
        heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mConfigImageHelper.getSizeImage().first,
                context.getResources().getDisplayMetrics());

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).ID;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public static class ViewHolderModel
    {

        @InjectView(R.id.image_model) ImageView mImageModel;

        @InjectView(R.id.text_model) TextView mTextModel;


        public ViewHolderModel(View view){
            ButterKnife.inject(this,view);
        }

    }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {
        View view  ;

        if(null == convertView) {

            view = mInflater.inflate(R.layout.view_model_spinner, parent, false);

            final ViewHolderModel viewHolder = new ViewHolderModel(view);

            view.setTag(viewHolder);

        }
        else
        {
            view = convertView;
        }

        ViewHolderModel holder = (ViewHolderModel) view.getTag();

        holder.mTextModel.setText(list.get(position).Model);

        temp = list.get(position);

        setImage(holder);

        return view;
    }

    private void setImage(ViewHolderModel holder) {
        if(temp.ImagePath == null ){

            setDefaultImage(holder,mConfigImageHelper.roundImage() ?
                    PictureUtils.getImageClotheDefaultRounded(mContext):
                    PictureUtils.getImageClotheDefault(mContext));
        }
        else{

            setImageWithPath(holder,
                    temp.ImagePath,
                    (int) widthPx,
                    (int) heightPx,
                    mConfigImageHelper.roundImage(),
                    mConfigImageHelper.roundImage() ?
                            PictureUtils.getImageClotheDefaultRounded(mContext) :
                            PictureUtils.getImageClotheDefault(mContext)
            );

        }
    }

    private void setImageWithPath(final ViewHolderModel holder, final Uri path, int widthPx, int heightPx, boolean isRounded, Drawable placeholder)
    {


        RequestCreator creator =  picasso.load(path)
                .placeholder(PictureUtils.getImageClotheDefault(mContext))
                .resize(widthPx, heightPx)
                .centerInside();


        if(isRounded)
            creator.transform(mCircleTransform);

        creator.into(holder.mImageModel);


    }

    private void setDefaultImage(final ViewHolderModel holder, final Drawable drawable){

        holder.mImageModel.setImageDrawable(drawable);
    }

}
