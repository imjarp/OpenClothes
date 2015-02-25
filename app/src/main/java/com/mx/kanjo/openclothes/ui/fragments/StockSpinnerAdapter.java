package com.mx.kanjo.openclothes.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.mx.kanjo.openclothes.util.StorageUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 23/02/2015.
 */
public class StockSpinnerAdapter implements SpinnerAdapter {

    private Context mContext;

    private ArrayList<StockItem> list;

    private HashMap<Integer,Drawable> images;

    LayoutInflater mInflater;

    Resources res;

    private static StockItem temp;

    public StockSpinnerAdapter(Context context, ArrayList<StockItem> list) {
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
        return list.get(position).getIdProduct();
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

        @InjectView(R.id.image_model)
        ImageView mImageModel;

        @InjectView(R.id.text_model)
        TextView mTextModel;


        public ViewHolderModel(View view){
            ButterKnife.inject(this, view);
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

        holder.mTextModel.setText(list.get(position).getModel());

        Drawable bmImage;

        temp = list.get(position);

        if( null != images.get( temp.getStockItemId() ) ) {
            bmImage = images.get( temp.getStockItemId() );
        }
        else {

            if(null != temp.getImagePath()) {
                String filePath = StorageUtil.getPath(mContext, temp.getImagePath());
                bmImage = PictureUtils.getRoundedBitmap(filePath,mContext);
                images.put(temp.getStockItemId(), bmImage);
            }
            else
            {
                bmImage = PictureUtils.getImageClotheDefaultRounded(mContext);
            }
        }

        holder.mImageModel.setImageDrawable(bmImage);


        return view;
    }

}
