package com.example.sorena.wanandroidapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sorena.wanandroidapp.bean.ProjectChapter;
import com.example.sorena.wanandroidapp.view.ProjectViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * projectFragment的viewpager的碎片适配器
 */
public class ProjectViewPagerAdapter extends FragmentPagerAdapter
{
    private List<ProjectViewPagerFragment> mFragmentList;
    private List<ProjectChapter> mProjectChapters;
    public ProjectViewPagerAdapter(FragmentManager fm, List<ProjectChapter> chapters) {
        super(fm);

        mProjectChapters = chapters;
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            mFragmentList.add(ProjectViewPagerFragment.getInstance(chapters.get(i)));
        }

    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mProjectChapters.get(position).getName();
    }
}
