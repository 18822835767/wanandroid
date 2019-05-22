package com.example.sorena.wanandroidapp.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.MyApplication;
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
    private List<Fragment> childFragmentList;


    public MainActivityViewPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragmentList = new ArrayList<>();
        childFragmentList.add(new HomeFragment());
        childFragmentList.add(new SystemFragment());
        childFragmentList.add(new NavigationFragment());
        childFragmentList.add(new ProjectFragment());
    }


    @Override
    public Fragment getItem(int i) {
        return childFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return childFragmentList.size();
    }


    //禁止销毁视图
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {}

}
