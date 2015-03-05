package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.util.CircleTransform;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 1/30/15.
 */
public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.SaleItemHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 4;

    private Context context;
    private int lastAnimatedPosition = -1;
    private ArrayList<StockItem> mSaleItems;
    private static StockItem tempSaleItem = null;
    Picasso picasso;
    ConfigImageHelper mConfigImageHelper;
    CircleTransform mCircleTransform ;
    float widthPx,heightPx;

    public SaleItemAdapter(Context context) {
        this.context = context;
    }

    public SaleItemAdapter(Context context, ArrayList<StockItem> saleItems, ConfigImageHelper configImageHelper){
        this.context = context;
        mSaleItems = saleItems;
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
    public SaleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.line_item_sale, parent, false);
        return new SaleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleItemHolder viewHolder, int position) {


        SaleItemHolder holder = (SaleItemHolder) viewHolder;

        tempSaleItem = mSaleItems.get(position);

        if( null == tempSaleItem.getImagePath()){

            if(mConfigImageHelper.roundImage())
                holder.imageViewModel.setImageDrawable(PictureUtils.getImageClotheDefaultRounded(context));
            else
            holder.imageViewModel.setImageDrawable(PictureUtils.getImageClotheDefault(context));
        }
        else {

            //TODO: Check if is rounded

            if(mConfigImageHelper.roundImage())
            {

                picasso.load(tempSaleItem.getImagePath())
                        .resize((int)widthPx, (int)heightPx)
                        .transform(mCircleTransform)
                        //.fit()
                        .centerInside()
                        .placeholder(PictureUtils.getImageClotheDefaultRounded(context))
                        .into(holder.imageViewModel);
            }
            else {
                picasso.load(tempSaleItem.getImagePath())
                        .placeholder(PictureUtils.getImageClotheDefault(context))
                        .resize((int)widthPx, (int)heightPx)
                        .into(holder.imageViewModel);
            }
        }

        holder.textViewModel.setText("Model : " + tempSaleItem.getModel());
        holder.textViewSize.setText( tempSaleItem.getSize().getSizeDescription() );
        holder.textViewQuantity.setText( String.valueOf( tempSaleItem.getQuantity() ) );
        holder.textViewPrice.setText("$ " + tempSaleItem.getPrice());
        holder.textViewTotal.setText( "$ " +  String.valueOf( tempSaleItem.getQuantity() * tempSaleItem.getPrice()) );

    }

    @Override
    public long getItemId(int position) {
        return mSaleItems.get(position).getStockItemId();
    }

    @Override
    public int getItemCount() {
        return mSaleItems.size();
    }

    public static class SaleItemHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.image_view_product) ImageView imageViewModel;
        @InjectView(R.id.text_view_model) TextView textViewModel;
        @InjectView(R.id.text_view_size) TextView textViewSize;
        @InjectView(R.id.text_view_quantity) TextView textViewQuantity;
        @InjectView(R.id.text_view_price) TextView textViewPrice;
        @InjectView(R.id.text_view_total) TextView textViewTotal;


        public SaleItemHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this,itemView);
        }

    }

    public void addSaleItem(StockItem saleItem){
        mSaleItems.add(saleItem);
        notifyItemInserted(mSaleItems.size());
    }




}
