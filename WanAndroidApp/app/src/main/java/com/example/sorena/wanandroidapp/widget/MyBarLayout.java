package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.view.SearchActivity;

public class MyBarLayout extends LinearLayout
{
    public MyBarLayout(Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_bar_layout,this);


        ImageView imageView = this.findViewById(R.id.myBar_imageView_search);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SearchActivity.class);
                context.startActivity(intent);
            }
        });

    }



}
