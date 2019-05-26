package com.example.sorena.wanandroidapp.dao;

import android.app.Activity;
import android.view.View;

import com.example.sorena.wanandroidapp.adapter.ProjectViewPagerAdapter;
import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.bean.ProjectChapter;
import com.example.sorena.wanandroidapp.util.ApiConstants;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;

public class ProjectDao
{

    private final static String ProjectDataAddress = ApiConstants.ProjectTreeAddress;

    public static void loadProjectData(Activity activity,ProjectDataLoadListener loadListener){

        if (loadListener == null){
            return;
        }
        if (NetWorkUtil.isNetworkAvailable(activity)){
            loadDataByWeb(loadListener);
        }else {
            loadListener.onError(new Exception("莫得网络"));
        }


    }

    private static void loadDataByWeb(ProjectDataLoadListener loadListener) {

        HttpUtil.sendHttpRequest(ProjectDataAddress, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                List<ProjectChapter> chapters = parseData(response);
                loadListener.onFinish(chapters);
            }

            @Override
            public void onError(Exception e) {
                loadListener.onError(e);
            }
        });

    }


    private static List<ProjectChapter> parseData(String response){
        String data = JSONUtil.getValue("data",response,new String[]{});
        Map<String,List<String>> dataMap = getMapInArray(data,new String[]{"id","name"});
        List<String> ids = dataMap.get("id");
        List<String> names = dataMap.get("name");
        List<ProjectChapter> chapters = new ArrayList<>();
        if (ids == null || names == null){
            return chapters;
        }
        for (int i = 0; i < ids.size(); i++) {
            chapters.add(new ProjectChapter(names.get(i),Integer.parseInt(ids.get(i))));
        }
        return chapters;
    }





    public interface ProjectDataLoadListener{
        void onFinish(List<ProjectChapter> chapters);
        void onError(Exception e);
    }



}
