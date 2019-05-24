package com.example.sorena.wanandroidapp.util;

import android.app.Activity;
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
import java.util.Map;

/**
 * 用于加载网络图片
 */
public class NetImageLoad
{
    private ImageView mImageView;
    private String mDownloadUrl;
    private Map<String,Bitmap> mCacheMap;
    private Activity mActivity;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            Bitmap bitmap = (Bitmap) msg.obj;
//            loadImage(mImageView, bitmap);
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
                    netImageLoad.mActivity.runOnUiThread(()->{
                        netImageLoad.loadImage(netImageLoad.mImageView, bitmap);
                    });
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
                mActivity.runOnUiThread(()->mCacheMap.put(imageView.getTag().toString(),bitmap));
            }

        }
    }

    public void downloadImage(Activity activity,ImageView imageView, final String imgUrl, final Map<String,Bitmap> cacheMap){
        this.mImageView = imageView;
        this.mDownloadUrl = imgUrl;
        this.mCacheMap = cacheMap;
        this.mActivity = activity;
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
//                        loadImage(mImageView, bitmap);
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
