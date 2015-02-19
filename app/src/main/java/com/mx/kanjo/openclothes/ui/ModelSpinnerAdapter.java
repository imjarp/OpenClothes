package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
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

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 2/18/15.
 */
public class ModelSpinnerAdapter implements SpinnerAdapter {

    private Context mContext;

    private ArrayList<LeanProductModel> list;

    LayoutInflater mInflater;

    Resources res;

    public ModelSpinnerAdapter(Context context, ArrayList<LeanProductModel> list) {
        this.mContext = context;
        this.list = list;
        this.mInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.res = mContext.getResources();
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

    public static RoundedBitmapDrawable create(Resources res, String filePath)
    {
        return RoundedBitmapDrawableFactory.create(res  ,filePath);
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


        String filePath = list.get(position).ImagePath.toString();

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, filePath );

        roundedBitmapDrawable.setCornerRadius(5f);

        holder.mImageModel.setImageBitmap(roundedBitmapDrawable.getBitmap());

        holder.mTextModel.setText("Test");

        return view;
    }


}
