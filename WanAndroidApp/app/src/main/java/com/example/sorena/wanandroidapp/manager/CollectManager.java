package com.example.sorena.wanandroidapp.manager;

import android.widget.BaseAdapter;

import com.example.sorena.wanandroidapp.util.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sorena
 * @date 2019/5/13
 * 这个类目前用不上,因为没有登录功能
 * 只是打算用这个类来管理收藏的功能
 *
 */


public class CollectManager
{
    private static CollectManager collectManager;
    private static Set<Integer> collectSet;
    private static List<BaseAdapter> adapters;

    static {
        collectManager = new CollectManager();
        collectSet = new HashSet<>();
        adapters = new ArrayList<>();
    }

    public CollectManager getInstance(){
        return collectManager;
    }


    public void registerAdapter(BaseAdapter adapter){
        adapters.add(adapter);
    }


    public boolean isCollect(int id){
        return collectSet.contains(id);
    }

    public void addCollect(int id){
        collectSet.add(id);
        noticeDataChange();
    }

    public void removeCollect(int id){
        collectSet.remove(id);
        noticeDataChange();
    }


    private void noticeDataChange(){
        try {
            for (BaseAdapter adapter: adapters) {
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.e("日志:CollectManager",e.getMessage());
        }
    }



}
