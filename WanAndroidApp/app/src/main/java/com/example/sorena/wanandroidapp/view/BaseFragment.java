package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sorena.wanandroidapp.util.LogUtil;

public class BaseFragment extends Fragment
{
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onAttach执行" + "   绑定的活动:" + context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onCreate执行" + "   对象地址:" + this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onCreateView执行");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onViewCreated执行");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onStart执行");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onResume执行");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onPause执行");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onStop执行");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onDestroy执行");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.v("日志:BaseFragment:" + getClass().getSimpleName(),"onDetach执行");
    }
}
