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
import com.mx.kanjo.openclothes.model.LeanProductModel;
import com.mx.kanjo.openclothes.util.CircleTransform;
import com.mx.kanjo.openclothes.util.ConfigImageHelper;
import com.mx.kanjo.openclothes.util.PictureUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JARP on 1/30/15.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 4;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private List<LeanProductModel> leanProductModelList;
    private static LeanProductModel tempModelProduct = null;
    Picasso picasso;
    ConfigImageHelper mConfigImageHelper;
    CircleTransform mCircleTransform ;
    float widthPx,heightPx;

    public ProductAdapter(Context context) {
        this.context = context;
    }



    public ProductAdapter(Context context, ArrayList<LeanProductModel> products, ConfigImageHelper configImageHelper){
        this.context = context;
        leanProductModelList = products;
        itemsCount = products.size();
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

        final View view = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int position) {

        ProductViewHolder holder = (ProductViewHolder) viewHolder;

        tempModelProduct = leanProductModelList.get(position);

        if( null ==tempModelProduct.ImagePath){

            if(mConfigImageHelper.roundImage())
                holder.imageViewModel.setImageDrawable(PictureUtils.getImageClotheDefaultRounded(context));
            else
            holder.imageViewModel.setImageDrawable(PictureUtils.getImageClotheDefault(context));
        }
        else {

            //TODO: Check if is rounded

            if(mConfigImageHelper.roundImage())
            {

                picasso.load(tempModelProduct.ImagePath)
                        .resize((int)widthPx, (int)heightPx)
                        .transform(mCircleTransform)
                        .into(holder.imageViewModel);
            }
            else {
                picasso.load(tempModelProduct.ImagePath)
                        .resize((int)widthPx, (int)heightPx)
                        .into(holder.imageViewModel);
            }
        }

        holder.textViewModel.setText("Model : " + tempModelProduct.Model);
        holder.textViewPrice.setText("Price : $" + tempModelProduct.Price);

    }

    @Override
    public long getItemId(int position) {
        return leanProductModelList.get(position).ID;
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.text_view_model)
        TextView textViewModel;


        @InjectView(R.id.image_view_product)
        ImageView imageViewModel;

        @InjectView(R.id.text_view_price)
        TextView textViewPrice;


        public ProductViewHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this,itemView);
        }

    }
}
