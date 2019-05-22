package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.sorena.wanandroidapp.R;

public class MyBarLayout extends LinearLayout
{
    public MyBarLayout(Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_bar_layout,this);
    }



}
