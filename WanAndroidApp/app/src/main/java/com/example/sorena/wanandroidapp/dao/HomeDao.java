package com.example.sorena.wanandroidapp.dao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.sorena.wanandroidapp.bean.Article;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.FileUtil;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.MyApplication;
import com.example.sorena.wanandroidapp.util.NetWorkUtil;
import com.example.sorena.wanandroidapp.util.PermissionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用于首页的数据加载和数据缓存
 *
 */
public class HomeDao
{
    private static final String BaseDir = "/sorena/file/wanAndroidApp/";
    private static final String LoopPicBaseDir = "/sorena/file/wanAndroidApp/loopPic/";
    private static final String ToppingDataFileName = "toppingData.txt";
    private static final String NormalDataFileName = "normalData.txt";
    private static final String LoopDataFileName = "loopData.txt";
    private static final String LoopPicFrontString = "https://wanandroid.com/blogimgs/";
    private static final String LoopingDataAddress = "https://www.wanandroid.com/banner/json";
    private static boolean normalDataIsLoading = false;
    private static HomeDao homeDao;
    static {
        homeDao = new HomeDao();
    }
    private HomeDao(){}

    public static void getLoopingData(Activity activity,LoopingDataLoadingListener listener){

        if (normalDataIsLoading){
            return;
        }
        normalDataIsLoading = true;
        if (NetWorkUtil.isNetworkAvailable(activity)){
            loadLoopingDataByWeb(activity,listener);
        }else {
            loadLoopingDataByLocalFile(activity,listener);
        }

    }

    private static void loadLoopingDataByWeb(Activity activity, LoopingDataLoadingListener listener){
        HttpUtil.sendHttpRequest(LoopingDataAddress, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                saveLoopingDataFile(activity,response);
                String data = JSONUtil.getValue("data",response,new String[]{});
                String[] keys = {"desc","imagePath","title","url"};
                Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,keys);
                List<String> urls = stringListMap.get("imagePath");
                List<String> messages = stringListMap.get("title");
                List<String> webUrls = stringListMap.get("url");
                listener.onFinish(urls,messages,webUrls);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                listener.onError(e);
            }
        });
    }

    private static void loadLoopingDataByLocalFile(Activity activity, LoopingDataLoadingListener listener){

        String response = FileUtil.getStringInFile(BaseDir,LoopDataFileName);
        if (response.equals("")){
            return;
        }
        try {
            String data = JSONUtil.getValue("data",response,new String[]{});
            String[] keys = {"desc","imagePath","title","url"};
            Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,keys);
            List<String> urls = stringListMap.get("imagePath");
            List<String> messages = stringListMap.get("title");
            List<String> webUrls = stringListMap.get("url");
            listener.onFinish(urls,messages,webUrls);
        }catch (Exception e){
            listener.onError(e);
        }
    }

    public static void getToppingData(Activity activity,ToppingDataLoadingListener loadingListener){

        if (NetWorkUtil.isNetworkAvailable(activity)){
            loadToppingDataByWeb(activity,loadingListener);
        }else {
            LogUtil.d("日志:HomeDao:getToppingData","木有网络");
            loadToppingDataByLocalFile(loadingListener);
        }
    }

    private static void loadToppingDataByLocalFile(ToppingDataLoadingListener loadingListener){
        String data = FileUtil.getStringInFile(BaseDir,ToppingDataFileName);
        if (data.equals("")){
            return;
        }
        List<Article> articles = parseToppingArticleData(data);
        if (loadingListener != null){
            loadingListener.onFinish(articles);
        }
    }

    private static void loadToppingDataByWeb(Activity activity,ToppingDataLoadingListener loadingListener){
        new Thread(()->{
            User user = SharedPreferencesHelper.getUserData();
            HttpUtil.sendHttpGetRequestWithCookie("https://www.wanandroid.com/article/top/json",
                    new String[]{"loginUserName","loginUserPassword"},
                    new String[]{user.getUserName(),user.getUserPassword()},
                    new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onFinish(String response) {
                            saveToppingDataFile(activity,response);
                            if (loadingListener != null){
                                List<Article> articles = HomeDao.parseToppingArticleData(response);
                                loadingListener.onFinish(articles);
                            }else {
                                LogUtil.d("日志:HomeDao","未传入listener");
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
        }).start();
    }



    public static void loadNormalData(Activity activity,int loadPage,NormalDataLoadingListener listener){

        if (NetWorkUtil.isNetworkAvailable(activity)){
            loadNormalDataByWeb(activity,loadPage,listener);
        }else {
            LogUtil.d("日志:HomeDao:loadNormalData","莫得网络");
            if (loadPage == 1){
                loadNormalDataByLocalFile(listener);
            }
        }
    }

    private static void loadNormalDataByLocalFile(NormalDataLoadingListener listener){
        String data = FileUtil.getStringInFile(BaseDir,NormalDataFileName);
        if (data.equals("")){
            return;
        }
        List<Article> articles = parseNormalArticleData(data);
        if (listener != null){
            listener.onFinish(articles);
        }
    }

    private static void loadNormalDataByWeb(Activity activity,int loadPage,NormalDataLoadingListener listener){
        new Thread(()->{
            User user = SharedPreferencesHelper.getUserData();
            HttpUtil.sendHttpGetRequestWithCookie("https://www.wanandroid.com/article/list/"
                            + loadPage + "/json",
                    new String[]{"loginUserName","loginUserPassword"},
                    new String[]{user.getUserName(),user.getUserPassword()},
                    new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onFinish(String response) {
                            List<Article> articles = parseNormalArticleData(response);
                            if (loadPage == 1){
                                saveNormalDataFile(activity,response);
                            }
                            if (listener != null){
                                listener.onFinish(articles);
                            }
                            normalDataIsLoading = false;
                        }
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            if (listener != null){
                                listener.onError(e);
                            }
                            normalDataIsLoading = false;
                        }
                    });
        }).start();
    }



    public interface ToppingDataLoadingListener{
        void onFinish(List<Article> articles);
        void onError(Exception e);
    }


    public interface NormalDataLoadingListener{
        void onFinish(List<Article> articles);
        void onError(Exception e);
    }

    public interface LoopingDataLoadingListener{
        void onFinish(List<String> urls,List<String> messages,List<String> webUrls);
        void onError(Exception e);
    }

    private static void saveToppingDataFile(Activity activity, String jsonString){
        if (PermissionUtils.permissionAllow(activity,new String[]{PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE})){
            FileUtil.saveFileToSDCardCustomDir(jsonString.getBytes(), BaseDir,ToppingDataFileName);
        }
    }

    private static void saveNormalDataFile(Activity activity,String jsonString){
        if (PermissionUtils.permissionAllow(activity,new String[]{PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE})){
            FileUtil.saveFileToSDCardCustomDir(jsonString.getBytes(),BaseDir,NormalDataFileName);
        }
    }

    private static void saveLoopingDataFile(Activity activity,String jsonString){
        if (PermissionUtils.permissionAllow(activity,new String[]{PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE})){
            FileUtil.saveFileToSDCardCustomDir(jsonString.getBytes(),BaseDir,LoopDataFileName);
        }
    }

    private static List<Article> parseNormalArticleData(String jsonString){
        String data = JSONUtil.getValue("datas",jsonString,new String[]{"data"});
        Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,new String[]{"author","link","title","niceDate","chapterName","id","collect"});
        List<String> authors = stringListMap.get("author");
        List<String> links = stringListMap.get("link");
        List<String> titles = stringListMap.get("title");
        List<String> niceDates = stringListMap.get("niceDate");
        List<String> chapterNames = stringListMap.get("chapterName");
        List<String> ids = stringListMap.get("id");
        List<String> collects = stringListMap.get("collect");
        ArrayList<Article> articles = new ArrayList<>();
        if (authors == null) return null;
        for (int i = 0; i < authors.size() ; i++) {
            articles.add(new Article(authors.get(i),links.get(i),titles.get(i),niceDates.get(i),chapterNames.get(i),Integer.parseInt(ids.get(i)),Boolean.parseBoolean(collects.get(i))));
        }
        return articles;
    }

    private static List<Article> parseToppingArticleData(String jsonString){
        String data = JSONUtil.getValue("data",jsonString,new String[]{});
        Map<String,List<String>> stringListMap = JSONUtil.getMapInArray(data,new String[]{"author","link","title","niceDate","chapterName","id","collect"});
        List<String> authors = stringListMap.get("author");
        List<String> links = stringListMap.get("link");
        List<String> titles = stringListMap.get("title");
        List<String> niceDates = stringListMap.get("niceDate");
        List<String> chapterNames = stringListMap.get("chapterName");
        List<String> ids = stringListMap.get("id");
        List<String> collects = stringListMap.get("collect");
        List<Article> articles = new LinkedList<>();
        if (authors == null) return articles;
        for (int i = 0; i < authors.size() ; i++) {
            articles.add(new Article(authors.get(i),links.get(i),titles.get(i),niceDates.get(i),chapterNames.get(i),Integer.parseInt(ids.get(i)),Boolean.parseBoolean(collects.get(i))));
        }
        return articles;
    }

    public static void saveBitmap(Context context, Bitmap bitmap, String url){
        if (PermissionUtils.permissionAllow(context,new String[]{PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE})){
            byte[] bytes = FileUtil.getBitmapByteArray(bitmap);
            url = url.substring(url.lastIndexOf("/")+1);
            LogUtil.d("日志:保存bitmap:",url + "   size:" + bytes.length);
            FileUtil.saveFileToSDCardCustomDir(bytes,LoopPicBaseDir,url);
        }
    }

    /**
     *
     * @param url:url链接
     * @return 如果这张图片被缓存,返回bitmap,否则,返回null
     */
    public static Bitmap tryGetBitmap(String url){
        if (PermissionUtils.permissionAllow(MyApplication.getContext(),new String[]{PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE})){
            url = url.substring(url.lastIndexOf("/")+1);
            return FileUtil.loadBitmapFromSDCard(FileUtil.getSDCardBaseDir() + LoopPicBaseDir + url);
        }
        return null;
    }



}
