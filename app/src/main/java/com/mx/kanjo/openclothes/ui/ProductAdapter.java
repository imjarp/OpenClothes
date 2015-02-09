package com.mx.kanjo.openclothes.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 1/30/15.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 2;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int position) {

        ProductViewHolder holder = (ProductViewHolder) viewHolder;

        holder.imageViewModel.setImageResource(R.drawable.cb_002);
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.text_view_model)
        TextView textViewModel;

        @InjectView(R.id.text_view_cost)
        TextView textViewCost;

        @InjectView(R.id.text_view_price)
        TextView textViewPrice;

        @InjectView(R.id.image_view_product)
        ImageView imageViewModel;


        public ProductViewHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
