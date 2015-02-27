package com.mx.kanjo.openclothes.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mx.kanjo.openclothes.R;

/**
 * Created by JARP on 2/15/15.
 */
public class PictureUtils {

    public static Drawable imageDefault;
    public static Bitmap bitmapDefaultRounded;

    public enum SizeImage {
        IMAGE_192x192,
    }

    private static int getSize(SizeImage sizeImage){

        switch (sizeImage)
        {
            case IMAGE_192x192 :
                return 192;

        }
        return 128;
    }

    public static void setImageScaled(Context context, ImageView imageWidget, String photoPath, SizeImage sizeImage)
    {

        int destHeight = getSize(sizeImage);
        int destWidth = getSize(sizeImage);

        // read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round((float)srcHeight / (float)destHeight);
            } else {
                inSampleSize = Math.round((float)srcWidth / (float)destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        BitmapDrawable drawable =  new BitmapDrawable(context.getResources(), bitmap);

        imageWidget.setImageDrawable(drawable);

    }

    public static Drawable getRoundedBitmap(String filePath,  Context context)
    {
        Bitmap src = BitmapFactory.decodeFile(filePath);
        if(src == null)
            return getImageClotheDefaultRounded(context);


        return getRoundedDrawable(src, context.getResources());
    }

    public static Drawable getImageClotheDefaultRounded(Context context)
    {

        if( null == bitmapDefaultRounded)
            bitmapDefaultRounded = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_default_clother_primary);

        return getRoundedDrawable(bitmapDefaultRounded ,  context.getResources());

    }

    private static Drawable getRoundedDrawable(Bitmap src, Resources res)
    {
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


    public static Drawable getImageClotheDefault(Context context) {

        if( null ==imageDefault )
        {
            imageDefault = context.getResources().getDrawable(R.mipmap.ic_default_clother_primary);
        }

        return imageDefault;

    }






}
