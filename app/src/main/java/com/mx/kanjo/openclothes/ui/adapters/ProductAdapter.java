package com.mx.kanjo.openclothes.ui.adapters;

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
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>
 {

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
    static OnItemClickListener mItemClickListener;

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
                        //.fit()
                        .centerInside()
                        .placeholder(PictureUtils.getImageClotheDefaultRounded(context))
                        .into(holder.imageViewModel);
            }
            else {
                picasso.load(tempModelProduct.ImagePath)
                        .placeholder(PictureUtils.getImageClotheDefault(context))
                        .resize((int)widthPx, (int)heightPx)
                        .into(holder.imageViewModel);
            }
        }


        SetOnItemClickListener(mItemClickListener);
        holder.textViewModel.setText(tempModelProduct.Model);
        holder.textViewPrice.setText("$ " + tempModelProduct.Price);
        holder.bindProduct(tempModelProduct);

    }


     @Override
    public long getItemId(int position) {
        return leanProductModelList.get(position).ID;
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public void updateProduct(int position ,LeanProductModel productModel){

        leanProductModelList.set(position,productModel);
        notifyItemChanged(position);
    }

     public void addProduct(LeanProductModel newProduct){

         leanProductModelList.add(newProduct);


         if(leanProductModelList.size()>1)
            notifyItemInserted(leanProductModelList.size()-1);
         notifyDataSetChanged();


     }

     public static interface OnItemClickListener {
         public void onItemClick(View view , int position, long id);
     }

     public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
         this.mItemClickListener = mItemClickListener;
     }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener
    {

        @InjectView(R.id.text_view_model)
        TextView textViewModel;

        @InjectView(R.id.image_view_product)
        ImageView imageViewModel;

        @InjectView(R.id.text_view_price)
        TextView textViewPrice;

        private LeanProductModel productModel;


        public ProductViewHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this,itemView);
            itemView.setOnLongClickListener(this);
        }

        public void bindProduct(LeanProductModel productModel){

            this.productModel = productModel;
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
