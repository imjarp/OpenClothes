package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.util.StorageUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 2/18/15.
 */
public class ModelSpinnerAdapter implements SpinnerAdapter {

    private Context mContext;

    private ArrayList<LeanProductModel> list;

    private HashMap<Integer,Drawable> images;

    LayoutInflater mInflater;

    Resources res;

    private static LeanProductModel temp;

    public ModelSpinnerAdapter(Context context, ArrayList<LeanProductModel> list) {
        this.mContext = context;
        this.list = list;
        this.mInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = mContext.getResources();
        images = new HashMap<>();
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
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
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
        View view   = null;

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

        Drawable bmImage;

        temp = list.get(position);

        if( null != images.get(temp.ID)) {
            bmImage = images.get(temp.ID);
        }
        else {
            String filePath = StorageUtil.getPath(mContext, temp.ImagePath);
            bmImage = getRoundedBitmap(filePath);
            images.put(temp.ID,bmImage);
        }

        holder.mImageModel.setImageDrawable(bmImage);


        return view;
    }

    public Drawable getRoundedBitmap(String filePath)
    {

        Bitmap src = BitmapFactory.decodeFile(filePath);

        //TODO : Set a default image
        /*if( null == src )
        {

        }*/
        Bitmap dst;
        if (src.getWidth() >= src.getHeight()){
            dst = Bitmap.createBitmap(src, src.getWidth()/2 - src.getHeight()/2, 0, src.getHeight(), src.getHeight()
            );
        }else{
            dst = Bitmap.createBitmap(src, 0, src.getHeight()/2 - src.getWidth()/2, src.getWidth(), src.getWidth()
            );
        }
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, dst);
        roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
        roundedBitmapDrawable.setAntiAlias(true);
        return roundedBitmapDrawable;

    }


}
