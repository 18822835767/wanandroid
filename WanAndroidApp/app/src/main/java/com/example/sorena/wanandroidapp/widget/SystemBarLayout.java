package com.example.sorena.wanandroidapp.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sorena.wanandroidapp.R;

public class SystemBarLayout extends RelativeLayout
{
    public SystemBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_system_bar,this);

        ImageView imageView = findViewById(R.id.mySystemBar_imageView_back);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
                Log.d("日志:","点击了返回键");
            }
        });
    }

}
