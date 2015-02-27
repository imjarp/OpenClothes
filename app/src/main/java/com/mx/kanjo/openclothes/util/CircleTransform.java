package com.mx.kanjo.openclothes.util;

/**
 * Created by JARP on 2/26/15.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {


    private static final int DEFAULT_PAINT_FLAGS =
            Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;

    public Resources res;

    Bitmap mBitmap;

    private Paint mPaint = new Paint(DEFAULT_PAINT_FLAGS);

    BitmapShader mBitmapShader;

    int mTargetDensity;
    // These are scaled to match the target density.
    final RectF mDstRectF = new RectF();
    final RectF mDstRect = new RectF();
    float mCornerRadius;

    public void setCornerRadius(float cornerRadius) {

        mCornerRadius = cornerRadius;
    }

    @Override
    public Bitmap transform(final Bitmap source) {




        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }



    @Override
    public String key() {
        return "circle";
    }
}