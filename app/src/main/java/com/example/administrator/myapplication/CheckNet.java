package com.example.administrator.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNet {
    public final static int NET_NONE = 0;
    public final static int NET_WIFI = 1;
    public final static int NET_MOBILE = 2;

    // 使用ConnectManager类的getSystemService方法，获取网络连接状态值
    public static int getNetStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return NET_NONE;
        }
        int type = activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NET_MOBILE;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NET_WIFI;
        }
        return NET_MOBILE;
    }
}
