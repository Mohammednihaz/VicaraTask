package com.vicaraproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Networkutil {

    public static String getConnectivityStatusString(Context context) {
        //network state checking
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                return status;
            }else if (activeNetwork.getType() == ConnectivityManager.TYPE_BLUETOOTH)
            {
                status = "Mobile Bluetooth enabled";
                return status;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                return status;
            }

        } else {
            status = "No internet is available";
            return status;
        }

        return status;
    }
}
