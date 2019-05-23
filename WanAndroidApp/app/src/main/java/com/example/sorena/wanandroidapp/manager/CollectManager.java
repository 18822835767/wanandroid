package com.example.sorena.wanandroidapp.manager;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.bean.ProjectListItem;
import com.example.sorena.wanandroidapp.bean.SearchResult;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 新的收藏管理类
 */
public class CollectManager
{
    private static CollectManager manger;
    private static Set<Integer> collectSet;

    static {
        manger = new CollectManager();
        collectSet = new HashSet<>();
    }

    public static CollectManager getInstance(){
        return manger;
    }

    /**
     * 增加收藏的方法，所有增加收藏的操作都由这个方法执行
     * @param id
     * @param imageView
     * @param activity
     */
    public void addCollect(int id, ImageView imageView, Activity activity){
        sendCollectData(id,imageView,activity);
    }

    /**
     *
     * @param id:增加的收藏id
     * @param imageView:要修改的imageView
     * @param activity:调用这个方法的活动
     */
    private void sendCollectData(int id, ImageView imageView, Activity activity){

        String address = "https://www.wanandroid.com/lg/collect/" + id + "/json";
        User user = SharedPreferencesHelper.getUserData();
        if (user.dataIsNull()){
            activity.runOnUiThread(()->{
                Toast.makeText(activity,R.string.request_login,Toast.LENGTH_SHORT).show();
            });
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
                                addToCollectSet(id);
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
                    public void onError(Exception e) {
                        activity.runOnUiThread(()->Toast.makeText(activity,"可能是电波对不上,收藏失败啦",Toast.LENGTH_SHORT).show());
                    }
                });
    }


    /**
     * 所有取消收藏的操作都由这个方法操作
     * @param id:取消收藏的id
     * @param imageView:要修改的imageView
     * @param activity:调用这个方法的活动
     */
    public void cancelCollect(int id,ImageView imageView,Activity activity){
        sendCancelCollectionData(id,imageView,activity);
    }

    private void sendCancelCollectionData(int id, ImageView imageView, Activity activity){

        String address = "https://www.wanandroid.com/lg/uncollect_originId/" + id +"/json";
        User user = SharedPreferencesHelper.getUserData();
        if (user.dataIsNull()){
            activity.runOnUiThread(()->{
                Toast.makeText(activity,R.string.request_login,Toast.LENGTH_SHORT).show();
            });
            return;
        }
        HttpUtil.sendHttpPostRequestWithCookie(address, new String[]{"loginUserName", "loginUserPassword"},
                new String[]{user.getUserName(), user.getUserPassword()}, null, null,
                new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        String errorMessage = JSONUtil.getValue("errorMsg",response,new String[]{});
                        if (errorMessage == null) {
                            LogUtil.d("日志:转化错误:errorMessage:","为null");
                            return;
                        }
                        activity.runOnUiThread(()->{
                            if (errorMessage.equals("")){
                                removeFromCollectSet(id);
                                if (imageView != null){
                                    imageView.setImageResource(R.drawable.ic_collect_normal);
                                    imageView.setTag(R.drawable.ic_collect_normal);
                                }
                            }
                            else {

                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        activity.runOnUiThread(()->Toast.makeText(activity,"哦豁,取消收藏失败啦",Toast.LENGTH_SHORT).show());
                    }
                });

    }







    public void addToCollectSet(int id){
        collectSet.add(id);
    }

    private void removeFromCollectSet(int id){
        collectSet.remove(id);
    }

    public boolean isCollect(int id){
        return collectSet.contains(id);
    }

    public void clearCollectSet(){
        if (collectSet != null){
            collectSet.clear();
        }
    }

    public void addToCollectSet(List<Article> articleList){
        if (articleList != null){
            for (Article a: articleList) {
                if (a.isCollect()){
                    addToCollectSet(a.getId());
                }
            }
        }
    }

    public void addToCollectSetByResult(List<SearchResult> results){
        if (results != null){
            for (SearchResult result : results) {
                if (result.isCollect()){
                    addToCollectSet(result.getId());
                }
            }
        }
    }


    public void addToCollectSetByProjectItem(List<ProjectListItem> projectListItems){
        if (projectListItems != null){
            for (ProjectListItem listItem : projectListItems){
                if (listItem.isCollect()){
                    addToCollectSet(listItem.getId());
                }
            }
        }
    }

}
