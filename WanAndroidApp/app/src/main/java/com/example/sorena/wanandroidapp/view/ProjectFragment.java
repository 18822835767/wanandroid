package com.example.sorena.wanandroidapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.ProjectViewPagerAdapter;
import com.example.sorena.wanandroidapp.bean.ProjectChapter;
import com.example.sorena.wanandroidapp.util.ApiConstants;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;

/**
 * 项目碎片
 */
public class ProjectFragment extends BaseFragment {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ProjectViewPagerAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadData();
    }

    private void initView(){

        if (getView() == null){
            return;
        }
        mViewPager = getView().findViewById(R.id.view_pager);
        mTabLayout = getView().findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mViewPager.setOffscreenPageLimit(3);

    }
    private void loadData(){

        HttpUtil.sendHttpRequest(ApiConstants.ProjectTreeAddress, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                String data = JSONUtil.getValue("data",response,new String[]{});
                Map<String,List<String>> dataMap = getMapInArray(data,new String[]{"id","name"});
                List<String> ids = dataMap.get("id");
                List<String> names = dataMap.get("name");
                List<ProjectChapter> chapters = new ArrayList<>();
                if (ids == null || names == null){
                    return;
                }
                for (int i = 0; i < ids.size(); i++) {
                    chapters.add(new ProjectChapter(names.get(i),Integer.parseInt(ids.get(i))));
                }
                if (getActivity() == null){
                    return;
                }
                mAdapter = new ProjectViewPagerAdapter(getActivity().getSupportFragmentManager(),chapters);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setAdapter(mAdapter);
                        mTabLayout.setVisibility(View.VISIBLE);
                        mViewPager.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });




    }


}
