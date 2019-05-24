package com.example.sorena.wanandroidapp.backups;

import android.app.Activity;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *
 * @author sorena
 * @date 2019/5/16
 * 开始就加载所有收藏数据的方法太浪费资源
 * 接下来使用新的加载方法
 *
 *
 */

@Deprecated
public class OldCollectManager
{
    private static OldCollectManager oldCollectManager;
    private static Set<Integer> collectSet;
    private static List<BaseAdapter> adapters;
    private static int maxPage = 0;
    private static int loadingFinishNum = 0;

    static {
        oldCollectManager = new OldCollectManager();
        collectSet = new HashSet<>();
        adapters = new ArrayList<>();
    }

    private OldCollectManager(){}
    public static OldCollectManager getInstance(){
        return oldCollectManager;
    }



    public void registerAdapter(BaseAdapter adapter){
        adapters.add(adapter);
    }

    public void unResisterAdapter(BaseAdapter adapter){
        adapters.remove(adapter);
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

    public void removeCollect(int id){
        collectSet.remove(id);

    }


    private void noticeDataChange(){
        try {
            for (BaseAdapter adapter: adapters) {
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.e("日志:OldCollectManager",e.getMessage());
        }
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


    /**
     * 用于初始化collectSet,加载所有的收藏数据到collectSet中
     */
    public static void loadCollectData(){
        collectSet.clear();
        new Thread(()->{
            User user = SharedPreferencesHelper.getUserData();
            if (user.dataIsNull()){
                return;
            }
            try {
                Thread thread = new Thread(()->{
                    firstLoading(user);
                });
                thread.setPriority(8);
                thread.start();
                thread.join();
                for (int i = 0; i <= maxPage; i++) {
                    loadData(i,user);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            LogUtil.d("日志:收藏数据为",collectSet.toString());
        }).start();
    }

    public static void loadData(int page,User user){

        if (page > maxPage) {
            return;
        }
        String address  = "https://www.wanandroid.com/lg/collect/list/" + page + "/json";
        Thread thread = new Thread(()->{
            HttpUtil.sendHttpGetRequestWithCookie(address, new String[]{"loginUserName", "loginUserPassword"}, new String[]{user.getUserName(), user.getUserPassword()}, new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(String response) {
                    LogUtil.d("日志:response",response);
                    collectSet.addAll(parseData(response));
                    LogUtil.d("日志:收藏数据为",collectSet.toString());
                }

                @Override
                public void onError(Exception e) {
                }
            });
        });
        thread.setPriority(8);
        thread.start();
        try {
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static List<Integer> parseData(String jsonString){
        String data = JSONUtil.getValue("data",jsonString,new String[]{});
        String datas = JSONUtil.getValue("datas",data,new String[]{});
        Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(datas,new String[]{"originId"});
        List<String> ids = stringListMap.get("originId");
        if (ids != null){
            LogUtil.d("日志:originId",ids.toString());
        }else {
            LogUtil.d("日志:","orginId为null");
        }
        List<Integer> integerList = new LinkedList<>();
        if (ids != null){
            for (int i = 0; i < ids.size(); i++) {
                integerList.add(Integer.parseInt(ids.get(i)));
            }
        }
        return integerList;
    }


    //用于获得maxPage数
    private static void firstLoading(User user){

        String[] cookieKeys = new String[]{"loginUserName","loginUserPassword"};
        String[] cookiesValues = new String[]{user.getUserName(),user.getUserPassword()};
        String cookie = JSONUtil.getCookieString(cookieKeys,cookiesValues);
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://www.wanandroid.com/lg/collect/list/0/json");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.addRequestProperty("Cookie",cookie);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            String data = JSONUtil.getValue("data",response.toString(),new String[]{});
            String maxPageString = JSONUtil.getValue("pageCount",data,new String[]{});
            if (maxPageString != null){
                maxPage = Integer.parseInt(maxPageString) - 1;
            }
        }catch (Exception e){
            Log.d("日志:HttpUtilException",e.getMessage());
        }finally {
            if( connection != null){
                connection.disconnect();
            }
        }
    }
}
