package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.sorena.wanandroidapp.R;

public class RefreshLayout extends RelativeLayout
{

    private Button refreshLayoutButtonRefresh;

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.refresh_layout,this);
        refreshLayoutButtonRefresh = (Button) findViewById(R.id.refreshLayout_button_refresh);
        refreshLayoutButtonRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refreshAble != null){
                    refreshAble.refresh();
                }
            }
        });
        this.setVisibility(GONE);
    }


    private refreshAble refreshAble;
    public interface refreshAble{
        void refresh();
    }
    public void setRefreshAble(RefreshLayout.refreshAble refreshAble) {
        this.refreshAble = refreshAble;
    }
}
