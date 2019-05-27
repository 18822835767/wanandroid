package com.example.sorena.wanandroidapp.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.sorena.wanandroidapp.view.WebActivity;

public class WebActivityUtil
{

    public static void startWebActivity(@NonNull Context context, String url){
        Intent intent = new Intent(context,WebActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }


}
