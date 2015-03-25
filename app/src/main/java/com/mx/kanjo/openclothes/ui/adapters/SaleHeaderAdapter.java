package com.mx.kanjo.openclothes.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.SaleModel;
import com.mx.kanjo.openclothes.util.CircleTransform;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 1/30/15.
 */
public class SaleHeaderAdapter extends RecyclerView.Adapter<SaleHeaderAdapter.SaleHeaderViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 4;

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private List<SaleModel> saleList;
    private static SaleModel tempSale = null;
    Picasso picasso;
    ConfigImageHelper mConfigImageHelper;
    CircleTransform mCircleTransform ;
    float widthPx,heightPx;
    static OnItemClickListener mItemClickListener;

    public SaleHeaderAdapter(Context context) {
        mContext = context;
    }



    public SaleHeaderAdapter(Context context, ArrayList<SaleModel> stockItems, ConfigImageHelper configImageHelper){
        mContext = context;
        saleList = stockItems;
        picasso = Picasso.with(context);
        this.mConfigImageHelper = configImageHelper;
        mCircleTransform = new CircleTransform();
        widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mConfigImageHelper.getSizeImage().first,
                context.getResources().getDisplayMetrics());
        heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mConfigImageHelper.getSizeImage().first,
                context.getResources().getDisplayMetrics());
    }

    @Override
    public SaleHeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.line_general_sale, parent, false);

        return new SaleHeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleHeaderViewHolder viewHolder, int position) {

        SaleHeaderViewHolder holder = (SaleHeaderViewHolder) viewHolder;

        tempSale = saleList.get(position);

        //TODO : Change this for a divider

        SetOnItemClickListener(mItemClickListener);

        holder.textViewCustomer.setText(tempSale.getCustomer());

        holder.textViewDate.setText(tempSale.getDate());

        holder.textViewTotal.setText(" $"+ String.valueOf(tempSale.getTotal()));

        holder.bind(tempSale);

    }


    @Override
    public long getItemId(int position) {
        return saleList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }

    public static interface OnItemClickListener {
        public void onItemClick(View view , int position, long id);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public static class SaleHeaderViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener
    {


        @InjectView(R.id.tv_date_sale)
        TextView textViewDate;

        @InjectView(R.id.tv_customer)
        TextView textViewCustomer;

        @InjectView(R.id.text_view_total)
        TextView textViewTotal;

        private SaleModel mSaleModel;


        public SaleHeaderViewHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this,itemView);
            itemView.setOnLongClickListener(this);
        }

        public void bind(SaleModel saleModel){

            mSaleModel = saleModel;

        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition(),getItemId());
                return true;
            }
            return false;
        }
    }
}
