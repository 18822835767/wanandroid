package com.example.sorena.wanandroidapp.util;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewUtil
{

   public static void cancelActionBar(AppCompatActivity activity){
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
    }

    public static void fixSystemNoticeItem(AppCompatActivity activity){

       if (Build.VERSION.SDK_INT >= 21){

           View decorView = activity.getWindow().getDecorView();
           decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
           activity.getWindow().setStatusBarColor(Color.TRANSPARENT);


       }
    }




}
