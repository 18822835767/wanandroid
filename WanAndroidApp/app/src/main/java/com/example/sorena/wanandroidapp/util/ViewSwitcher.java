package com.example.sorena.wanandroidapp.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于交换碎片的封装
 */


public class ViewSwitcher
{
    private Map<String,Fragment> fragmentMap;
    private int resourceId;
    private AppCompatActivity activity;


    public ViewSwitcher(AppCompatActivity activity, int resourceId){
        fragmentMap = new HashMap<>();
        this.resourceId = resourceId;
        this.activity = activity;
    }


    public void add(Fragment fragment, String tag){
        fragmentMap.put(tag,fragment);
    }

    public void remove(String key){
        fragmentMap.remove(key);
    }

    public Map<String, Fragment> getFragmentMap() {
        return fragmentMap;
    }

    public boolean replaceView(String tag)
    {
        Fragment fragment = fragmentMap.get(tag);
        if (fragment == null){
            return false;
        }
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resourceId , fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return true;
    }

    public boolean addFragment(Fragment fragment , String tag){

        if (fragment == null) return false;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(resourceId , fragment);
        fragmentTransaction.commitAllowingStateLoss();
        add(fragment,tag);
        return true;
    }

    public void replace(@NonNull Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resourceId , fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }



}
