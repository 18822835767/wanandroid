package com.example.sorena.wanandroidapp.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(),"onCreate执行" + "   对象地址:" + this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(), "onStart执行");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(), "onResume执行");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(), "onPause执行");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(), "onStop执行");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(), "onDestroy执行" +  "   对象地址:" + this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("日志:BaseActivity:" + getClass().getSimpleName(), "onRestart执行");
    }
}
