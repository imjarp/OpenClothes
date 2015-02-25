package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.util.Maps;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.mx.kanjo.openclothes.util.StorageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 1/30/15.
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ProductViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 4;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private List<StockItem> stockList;
    private static StockItem tempStock = null;
    private HashMap<Integer,Drawable> images = Maps.newHashMap();

    public StockAdapter(Context context) {
        this.context = context;
    }



    public StockAdapter(Context context, ArrayList<StockItem> stockItems){
        this.context = context;
        stockList = stockItems;
        itemsCount = stockItems.size();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.stock_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int position) {

        ProductViewHolder holder = (ProductViewHolder) viewHolder;

        tempStock = stockList.get(position);

        if( null != tempStock.getImagePath()) {
            //holder.imageViewModel.setImageURI(tempStock.getImagePath());
            String filePath = StorageUtil.getPath(context, tempStock.getImagePath());
            holder.imageViewModel.setImageDrawable(PictureUtils.getRoundedBitmap(filePath, context));

        }
        else {
            holder.imageViewModel.setImageDrawable(PictureUtils.getImageClotheDefaultRounded(context));;
        }

        holder.textViewSize.setText( tempStock.getSize().getSizeDescription() );

        holder.textViewModel.setText( tempStock.getModel() );

        holder.textViewQuantity.setText( String.valueOf(tempStock.getQuantity()) );

    }

    @Override
    public long getItemId(int position) {
        return stockList.get(position).getStockItemId();
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.text_model)
        TextView textViewModel;


        @InjectView(R.id.image_model)
        ImageView imageViewModel;

        @InjectView(R.id.text_size)
        TextView textViewSize;

        @InjectView(R.id.text_qty)
        TextView textViewQuantity;


        public ProductViewHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this,itemView);
        }

    }
}
