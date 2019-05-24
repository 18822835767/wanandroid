package com.example.sorena.wanandroidapp.util;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 用于加载网络图片
 */
public abstract class NetImageLoad
{
    private ImageView imageView;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            Bitmap bitmap = (Bitmap) msg.obj;
//            loadImage(imageView, bitmap);
//        }
//    };


    static class MyHandler extends Handler {
        WeakReference<NetImageLoad> weekNetImageLoad;
        MyHandler(NetImageLoad netImageLoad) {
            weekNetImageLoad = new WeakReference<>(netImageLoad);
            LogUtil.v("日志:WeakReference:",weekNetImageLoad.toString());
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                final NetImageLoad netImageLoad = weekNetImageLoad.get();
                if (netImageLoad != null) {
                    LogUtil.v("日志:","加载执行");
                    Bitmap bitmap = (Bitmap) msg.obj;
                    netImageLoad.loadImage(netImageLoad.imageView, bitmap);
                }else {
                    LogUtil.d("日志:","netImageLoad为null");
                }
            }catch (Exception e){
                LogUtil.e("日志:handleMessage",e.getMessage());
            }
        }
    }







    public abstract void loadImage(ImageView imageView,Bitmap bitmap);

    public void downloadImage(ImageView imageView,final String imgUrl){
        this.imageView  = imageView;
        new Thread(){
            public void run() {
                try {
                    Looper.prepare();
                    URL url = new URL(imgUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoInput(true);
                    connection.setUseCaches(false); //设置不使用缓存
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap == null){
                        LogUtil.v("日志:",imgUrl + "加载失败");
                    }else {
                        LogUtil.v("日志:",imgUrl + "加载成功");
                    }
                    Message message = new Message();
                    message.obj = bitmap;
//                    handler.sendMessage(message);
                    MyHandler myHandler = new MyHandler(NetImageLoad.this);
//                    myHandler.post(()->{
//                        loadImage(imageView, bitmap);
//                    });
                    //The application may be doing too much work on its main thread
                    myHandler.handleMessage(message);
                } catch (Exception e) {
                    Log.e("日志:NetImageLoad:",e.getMessage());
                }
            }
        }.start();
    }

}
