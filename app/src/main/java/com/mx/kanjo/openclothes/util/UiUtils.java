package com.mx.kanjo.openclothes.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by JARP on 2/26/15.
 */
public class UiUtils {

    public static boolean isTablet(Context context)
    {
        Configuration config = context.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            return true;
        }
        return  false;
    }
}
