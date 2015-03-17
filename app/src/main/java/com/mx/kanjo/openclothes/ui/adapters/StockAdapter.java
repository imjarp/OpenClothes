package com.mx.kanjo.openclothes.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.kanjo.openclothes.R;
import com.mx.kanjo.openclothes.model.StockItem;
import com.mx.kanjo.openclothes.util.CircleTransform;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 1/30/15.
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ProductViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 4;

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private List<StockItem> stockList;
    private static StockItem tempStock = null;
    Picasso picasso;
    ConfigImageHelper mConfigImageHelper;
    CircleTransform mCircleTransform ;
    float widthPx,heightPx;

    public StockAdapter(Context context) {
        mContext = context;
    }



    public StockAdapter(Context context, ArrayList<StockItem> stockItems,ConfigImageHelper configImageHelper){
        mContext = context;
        stockList = stockItems;
        itemsCount = stockItems.size();
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
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.stock_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int position) {

        ProductViewHolder holder = (ProductViewHolder) viewHolder;

        //TODO : Change this for a divider
        if(position%2==0)
            holder.parentRowView.setBackgroundColor(mContext.getResources().getColor(R.color.odd_row_view));

        tempStock = stockList.get(position);

        setImage(holder);

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

    private void setImage(ProductViewHolder holder) {
        if(tempStock.getImagePath() == null ){

            setDefaultImage(holder,mConfigImageHelper.roundImage() ?
                    PictureUtils.getImageClotheDefaultRounded(mContext):
                    PictureUtils.getImageClotheDefault(mContext));
        }
        else{

            setImageWithPath(holder,
                    tempStock.getImagePath(),
                    (int) widthPx,
                    (int) heightPx,
                    mConfigImageHelper.roundImage(),
                    mConfigImageHelper.roundImage() ?
                            PictureUtils.getImageClotheDefaultRounded(mContext) :
                            PictureUtils.getImageClotheDefault(mContext)
            );

        }
    }

    private void setDefaultImage(final ProductViewHolder holder, final Drawable drawable){

        holder.imageViewModel.setImageDrawable(drawable);
    }


    private void setImageWithPath(final ProductViewHolder holder, final Uri path, int widthPx, int heightPx, boolean isRounded, Drawable placeholder)
    {


        RequestCreator creator =  picasso.load(path)
                .placeholder(PictureUtils.getImageClotheDefault(mContext))
                .resize(widthPx, heightPx)
                .centerInside();


        if(isRounded)
            creator.transform(mCircleTransform);

        creator.into(holder.imageViewModel);


    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.stock_item_view)
        LinearLayout parentRowView;

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
