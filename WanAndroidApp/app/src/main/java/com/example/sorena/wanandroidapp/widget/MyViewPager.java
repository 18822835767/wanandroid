package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.sorena.wanandroidapp.util.LogUtil;

public class MyViewPager extends ViewPager
{
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //上一次MotionEvent的时间
    private long mPrevTime;
    //是否在手抬起前有滑动
    private boolean mIsDraw;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        long thisTime = System.currentTimeMillis();
        if (listener != null){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    listener.onPagerTouch(true);
                    mPrevTime = thisTime;
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mIsDraw){
                        //点击事件小于1s时判断为打开网页操作
                        if (thisTime - mPrevTime <= 1000){
                            if (openWeb != null){
                                openWeb.openWeb();
                            }
                        }else {
                            LogUtil.d("日志:","什么都没发生");
                        }
                    }
                    mPrevTime = thisTime;
                    mIsDraw = false;
                case MotionEvent.ACTION_CANCEL:
                    listener.onPagerTouch(false);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mIsDraw = true;
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    public void setViewPagerTouchLister(OnViewPagerTouchListener onViewPagerTouchListener){
        this.listener = onViewPagerTouchListener;
    }


    private OnViewPagerTouchListener listener = null;

    public interface OnViewPagerTouchListener{
        void onPagerTouch(boolean isTouch);
    }

    private OpenWeb openWeb;
    public interface OpenWeb {
        void openWeb();
    }

    public void setOpenWeb(OpenWeb openWeb) {
        this.openWeb = openWeb;
    }
}
