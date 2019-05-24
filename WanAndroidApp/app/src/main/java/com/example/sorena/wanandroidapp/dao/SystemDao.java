package com.example.sorena.wanandroidapp.dao;

import android.app.Activity;

import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JudgeUtil;
import com.example.sorena.wanandroidapp.util.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

/**
 * 体系的数据来源类
 */
public class SystemDao
{
    private static final String SystemDataAddress = "https://www.wanandroid.com/tree/json";

    public static void getSystemData(Activity activity,SystemDataLoadingListener loadingListener){
        if (NetWorkUtil.isNetworkAvailable(activity)){
            loadSystemDataByWeb(loadingListener);
        }else {
            if (loadingListener != null){
                loadingListener.onError(new Exception("是届不到的连接呢"));
            }
        }
    }


    private static void loadSystemDataByWeb(SystemDataLoadingListener loadingListener){
        HttpUtil.sendHttpRequest(SystemDataAddress, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                List<Chapter> chapters = parseData(response);
                if (loadingListener != null){
                    loadingListener.onFinish(chapters);
                }
            }

            @Override
            public void onError(Exception e) {
                loadingListener.onError(e);
            }
        });
    }

    private static List<Chapter> parseData(String response){
        String data = getValue("data",response,new String[]{});
        Map<String,List<String>> childMap = getMapInArray(data,new String[]{"name","children"});
        List<String> childrenFirst =  childMap.get("children");
        List<String> name = childMap.get("name");
        Map<String,List<String>> dataMap;
        List<Chapter> chapters = new ArrayList<>();
        if (JudgeUtil.dataContainsNull(name,childrenFirst)){
            return chapters;
        }
        for (int i = 0; i < name.size(); i++) {
            dataMap = getMapInArray(childrenFirst.get(i),new String[]{"id","name"});
            List<String> idList = dataMap.get("id");
            List<String> nameList = dataMap.get("name");
            Chapter chapter = new Chapter();
            chapter.setChapterName(name.get(i));
            List<FlowItem> itemList = new ArrayList<>();
            for (int j = 0; j < idList.size(); j++){
                FlowItem flowItem = new FlowItem();
                flowItem.setId(Integer.parseInt(idList.get(j)));
                flowItem.setName(nameList.get(j));
                flowItem.setParentsName(name.get(i));
                itemList.add(flowItem);
            }
            chapter.setFlowItems(itemList);
            chapters.add(chapter);
        }
        return chapters;
    }




    public interface SystemDataLoadingListener{
        void onFinish(List<Chapter> chapters);
        void onError(Exception e);
    }

}
