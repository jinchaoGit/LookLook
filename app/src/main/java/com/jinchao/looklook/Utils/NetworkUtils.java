package com.jinchao.looklook.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ljc on 18-5-28.
 */

public class NetworkUtils {
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIsFirstStart(){
        return true;
    }
}
