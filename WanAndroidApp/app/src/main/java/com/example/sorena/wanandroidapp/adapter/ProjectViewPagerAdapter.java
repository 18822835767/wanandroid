package com.example.sorena.wanandroidapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sorena.wanandroidapp.bean.ProjectChapter;
import com.example.sorena.wanandroidapp.view.ProjectViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectViewPagerAdapter extends FragmentPagerAdapter
{
    private List<ProjectViewPagerFragment> fragmentList;
    private List<ProjectChapter> projectChapters;
    public ProjectViewPagerAdapter(FragmentManager fm, List<ProjectChapter> chapters) {
        super(fm);

        projectChapters = chapters;
        fragmentList = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            fragmentList.add(ProjectViewPagerFragment.getInstance(chapters.get(i)));
        }

    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return projectChapters.get(position).getName();
    }
}
