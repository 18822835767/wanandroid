package com.example.sorena.wanandroidapp.manager;

import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.JudgeUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.MyApplication;

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

    public static CollectManager getInstance(){
        return collectManager;
    }


    public void registerAdapter(BaseAdapter adapter){
        adapters.add(adapter);
    }

    public void unResisterAdapter(BaseAdapter adapter){
        adapters.remove(adapter);
    }

    public void addCollect(int id, ImageView imageView, Activity activity){
        sendCollectData(id,imageView,activity);
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

    private void sendCollectData(int id, ImageView imageView, Activity activity){

        String address = "https://www.wanandroid.com/lg/collect/" + id + "/json";
        User user = SharedPreferencesHelper.getUserData();
        if (user.dataIsNull()){
            return;
        }
        HttpUtil.sendHttpPostRequestWithCookie(address, new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()}, null, null, new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        String errorMessage = JSONUtil.getValue("errorMsg",response,new String[]{});
                        if (errorMessage == null) {
                            LogUtil.d("日志:转化错误", errorMessage);
                        }
                        activity.runOnUiThread(()->{
                            if (errorMessage.equals("")){
                                collectSet.add(id);
                                if (imageView != null){
                                    imageView.setImageResource(R.drawable.ic_collect_selected);
                                    imageView.setTag(R.drawable.ic_collect_selected);
                                }
                            }
                            else {
                                Toast.makeText(activity,"因为不可描述的原因,收藏失败啦",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onError(Exception e) {}
                });
    }






}
