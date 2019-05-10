package com.example.sorena.wanandroidapp.util;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application
{
    /**
     * 使用这个类时要在Manifests中设置android:name
     */

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }


}
