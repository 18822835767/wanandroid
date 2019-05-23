package com.example.sorena.wanandroidapp.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.sorena.wanandroidapp.view.HomeFragment;
import com.example.sorena.wanandroidapp.view.NavigationFragment;
import com.example.sorena.wanandroidapp.view.ProjectFragment;
import com.example.sorena.wanandroidapp.view.SystemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sorena
 * 用于给mainActivity的viewPager提供碎片
 *
 */
public class MainActivityViewPagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> mChildFragmentList;


    public MainActivityViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mChildFragmentList = new ArrayList<>();
        mChildFragmentList.add(new HomeFragment());
        mChildFragmentList.add(new SystemFragment());
        mChildFragmentList.add(new NavigationFragment());
        mChildFragmentList.add(new ProjectFragment());
    }


    @Override
    public Fragment getItem(int i) {
        return mChildFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mChildFragmentList.size();
    }


    //禁止销毁视图
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {}

}
