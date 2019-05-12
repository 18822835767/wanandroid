package com.example.sorena.wanandroidapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            LogUtil.d("日志:connection",connection + "   链接:" + path);
            //使用GET方法访问网络
            connection.setRequestMethod("GET");
            //超时时间为3秒
            connection.setConnectTimeout(3000);
            //获取输入流
            InputStream inputStream = connection.getInputStream();
            LogUtil.d("日志:inputStream",inputStream + "   链接:" + path);
            //使用工厂把网络的输入流生产Bitmap
            bitmap = BitmapFactory.decodeStream(inputStream);
            LogUtil.d("日志:图片大小(try)",bitmap + "   链接:" + path);
            //inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.e("日志:PictureUtil:exception",e.getMessage());
        }
        LogUtil.d("日志:图片大小",bitmap + "   链接:" + path);
        return bitmap;
    }



    private static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
