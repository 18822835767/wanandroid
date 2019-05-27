package com.example.sorena.wanandroidapp.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 用于加载网络图片
 */
public class NetImageLoad
{
    private ImageView mImageView;
    private String mDownloadUrl;
    private Map<String,Bitmap> mCacheMap;


    static class MyHandler extends Handler {
        WeakReference<NetImageLoad> weekNetImageLoad;
        MyHandler(NetImageLoad netImageLoad) {
            super(Looper.getMainLooper());
            weekNetImageLoad = new WeakReference<>(netImageLoad);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                final NetImageLoad netImageLoad = weekNetImageLoad.get();
                if (netImageLoad != null) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    netImageLoad.loadImage(netImageLoad.mImageView, bitmap);
                }else {
                    LogUtil.d("日志:","netImageLoad为null");
                }
            }catch (Exception e){
                LogUtil.e("日志:handleMessage",e.getMessage());
            }
        }
    }



    private void loadImage(ImageView imageView, Bitmap bitmap){
        if(imageView.getTag()!=null && imageView.getTag().equals(mDownloadUrl)){
            imageView.setImageBitmap(bitmap);
            if (mCacheMap != null){
                mCacheMap.put(imageView.getTag().toString(),bitmap);
            }

        }
    }

    public void downloadImage(ImageView imageView, final String imgUrl, final Map<String,Bitmap> cacheMap){
        this.mImageView = imageView;
        this.mDownloadUrl = imgUrl;
        this.mCacheMap = cacheMap;
        ThreadPool.getInstance().addTask(()->{
            try {
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
                MyHandler myHandler = new MyHandler(NetImageLoad.this);
                myHandler.sendMessage(message);
            } catch (Exception e) {
                LogUtil.e("日志:NetImageLoad:",e.getMessage());
            }
        });
    }

}
