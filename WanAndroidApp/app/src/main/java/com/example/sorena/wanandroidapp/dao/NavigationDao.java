package com.example.sorena.wanandroidapp.dao;

import android.app.Activity;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.NavArticleListAdapter;
import com.example.sorena.wanandroidapp.adapter.NavListItemAdapter;
import com.example.sorena.wanandroidapp.bean.NavFlowItem;
import com.example.sorena.wanandroidapp.bean.NavListItem;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.NetWorkUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NavigationDao
{
    private static final String NavAddress = "https://www.wanandroid.com/navi/json";
    public static void loadNavigationData(Activity activity,NavigationDataLoadingListener listener){
        if (NetWorkUtil.isNetworkAvailable(activity)){
            loadNavDataByWeb(listener);
        }else {
            if (listener != null){
                listener.onError(new Exception("届不到届不到"));
            }
        }
    }

    private static void loadNavDataByWeb(NavigationDataLoadingListener listener){
        HttpUtil.sendHttpRequest(NavAddress, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                List<NavListItem> navListItems = parseNavData(response);
                if (listener != null){
                    listener.onFinish(navListItems);
                }
            }
            @Override
            public void onError(Exception e) {
                LogUtil.e("日志:",e.getMessage());
            }
        });
    }



    private static List<NavListItem> parseNavData(String response){

        List<NavListItem> navListItems = new LinkedList<>();
        String data = JSONUtil.getValue("data",response,new String[]{});
        Map<String,List<String>> dataMap = JSONUtil.getMapInArray(data,new String[]{"articles","name"});
        List<String> articles = dataMap.get("articles");
        if (articles == null){
            LogUtil.d("日志:articles","null");
            return navListItems;
        }
        List<String> names = dataMap.get("name");
        if (names == null) {return navListItems;}

        for (int i = 0; i < names.size(); i++){
            Map<String,List<String>> flowItemDataMap =JSONUtil.getMapInArray(articles.get(i),new String[]{"link","title"});
            List<String> links = flowItemDataMap.get("link");
            List<String> titles = flowItemDataMap.get("title");
            if (links == null || titles == null){
                LogUtil.d("日志:links/titles","null");
                return navListItems;
            }
            List<NavFlowItem> flowItems = new LinkedList<>();
            int size = Math.min(links.size(),titles.size());
            for (int j = 0; j < size; j++) {
                flowItems.add(new NavFlowItem(titles.get(j),links.get(j)));
            }
            navListItems.add(new NavListItem(names.get(i),false,flowItems));

        }
        return navListItems;
    }



    public interface NavigationDataLoadingListener {
        void onFinish(List<NavListItem> navFlowItemList);
        void onError(Exception e);
    }



}
