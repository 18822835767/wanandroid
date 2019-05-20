package com.example.sorena.wanandroidapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.view.SystemItemFragment;

import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class SystemViewPagerAdapter extends FragmentPagerAdapter
{
    private Chapter mChapter;
    private List<SystemItemFragment> fragments;
    public SystemViewPagerAdapter(FragmentManager fm, Chapter chapter) {
        super(fm);
        this.mChapter = chapter;
        fragments = new LinkedList<>();
        List<FlowItem> flowItems  = chapter.getFlowItems();
        for (FlowItem flowItem: flowItems) {
            fragments.add(SystemItemFragment.getInstance(flowItem));
        }
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mChapter.getFlowItems().get(position).getName();
    }
}
