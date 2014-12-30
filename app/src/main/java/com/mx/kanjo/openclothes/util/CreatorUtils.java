package com.mx.kanjo.openclothes.util;

import android.net.Uri;


/**
 * Created by JARP on 12/10/14.
 */
public class CreatorUtils {

    public static String UriToString(Uri uri)
    {
        if(uri == null)
            return "";

        return uri.toString();
    }

    public Uri fromStringToUri(String uri)
    {
        return Uri.parse(uri);
    }
}
