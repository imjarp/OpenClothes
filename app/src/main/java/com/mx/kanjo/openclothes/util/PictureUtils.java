package com.mx.kanjo.openclothes.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageButton;

/**
 * Created by JARP on 2/15/15.
 */
public class PictureUtils {

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

    public static void setImageScaled(Context context, ImageButton imageButton, String photoPath, SizeImage sizeImage)
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

        imageButton.setImageDrawable(drawable);

    }

    public static Drawable getRoundedBitmap(String filePath, Resources res)
    {

        Bitmap src = BitmapFactory.decodeFile(filePath);

        //TODO : Set a default image
        /*if( null == src )
        {

        }*/
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
}