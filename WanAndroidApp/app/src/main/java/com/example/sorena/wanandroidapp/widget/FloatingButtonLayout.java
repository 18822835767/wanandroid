package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.sorena.wanandroidapp.R;

public class FloatingButtonLayout extends RelativeLayout
{

    private FloatingActionButton myFloatingButton;

    public FloatingButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_button_floating,this);
        myFloatingButton = findViewById(R.id.my_floatingButton);
        myFloatingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick();
                }
            }
        });
    }

    private floatingButtonListener listener;
    public interface floatingButtonListener{
        void onClick();
    }

    public void setListener(floatingButtonListener listener) {
        this.listener = listener;
    }

    public void setToTopListView(ListView listView){
        if (listView != null){
            listener = ()->{
                if (listView.getAdapter() != null){
                    listView.smoothScrollToPosition(0);
                }
            };
        }
    }
}
