package com.example.sorena.wanandroidapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.view.SystemItemFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * 当点击体系流布局的某个项时,跳转到的新活动的viewPager用的适配器
 *
 */
public class SystemViewPagerAdapter extends FragmentPagerAdapter
{
    private Chapter mChapter;
    private List<SystemItemFragment> mFragments;
    public SystemViewPagerAdapter(FragmentManager fm, Chapter chapter) {
        super(fm);
        this.mChapter = chapter;
        mFragments = new LinkedList<>();
        List<FlowItem> flowItems  = chapter.getFlowItems();
        for (FlowItem flowItem: flowItems) {
            mFragments.add(SystemItemFragment.getInstance(flowItem));
        }
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mChapter.getFlowItems().get(position).getName();
    }

}
