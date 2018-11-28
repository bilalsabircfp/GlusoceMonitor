package com.bilal.dzone.medical_glucose.JsonParsing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Malik Umair on 3/29/2016.
 */
public class Check_internet_connection {

    Context mContext;


    public Check_internet_connection(Context mContext){
        this.mContext = mContext;
    }





    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
