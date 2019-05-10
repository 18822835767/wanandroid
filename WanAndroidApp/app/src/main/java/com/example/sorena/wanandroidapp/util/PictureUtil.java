package com.example.sorena.wanandroidapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PictureUtil
{
    public static Bitmap getBitmaps(final String path){

        Bitmap bitmap = null;
        try {
            //把传过来的路径转成URL
            URL url = new URL(path);
            //获取连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //使用GET方法访问网络
            connection.setRequestMethod("GET");
            //超时时间为3秒
            connection.setConnectTimeout(3000);
            //获取输入流
            InputStream inputStream = connection.getInputStream();
            //使用工厂把网络的输入流生产Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);

        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
