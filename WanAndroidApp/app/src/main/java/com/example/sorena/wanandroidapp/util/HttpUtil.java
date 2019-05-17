package com.example.sorena.wanandroidapp.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil
{
    public static void sendHttpRequest(final String address,final HttpCallBackListener listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    //connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if( listener != null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    Log.d("日志:HttpUtilException",e.getMessage());
                    if(listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if( connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();

    }

    public interface HttpCallBackListener{

        void onFinish(String response);

        void onError(Exception e);

    }





    public static void sendPostRequest(final String address, final String[] keys, final String[] values, final HttpCallBackListener listener){

    new Thread(new Runnable() {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(address);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(JSONUtil.getPramsString(keys,values).getBytes());
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    response.append(line);
                }
                if( listener != null){
                    listener.onFinish(response.toString());
                }
            }catch (Exception e){
                Log.d("日志:HttpUtilException",e.getMessage());
                if(listener != null){
                    listener.onError(e);
                }
            }finally {
                if( connection != null){
                    connection.disconnect();
                }
            }
        }}).start();
    }

    public static void sendHttpGetRequestWithCookie(String address, String[] cookieKeys,
                                                    String[] cookiesValues, HttpCallBackListener listener){

        String cookie = JSONUtil.getCookieString(cookieKeys,cookiesValues);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
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
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    Log.d("日志:HttpUtilException",e.getMessage());
                    if (listener != null) {
                        listener.onError(e);
                    }
                }finally {
                    if( connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

    public static void sendHttpPostRequestWithCookie(String address, String[] cookieKeys,
                                                    String[] cookiesValues, String[] keys,String[] values,
                                                     HttpCallBackListener listener){

        String cookie = JSONUtil.getCookieString(cookieKeys,cookiesValues);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.addRequestProperty("Cookie",cookie);
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.write(JSONUtil.getPramsString(keys,values).getBytes());
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    Log.d("日志:HttpUtilException",e.getMessage());
                    if (listener != null) {
                        listener.onError(e);
                    }
                }finally {
                    if( connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }






    private static void test() throws Exception{

        String address = "https://www.wanandroid.com/lg/collect/list/0/json";
        String cookie = "loginUserName=sorena;loginUserPassword=123456;";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
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
                    LogUtil.d("日志:测试",response.toString());
                }catch (Exception e){
                    Log.d("日志:HttpUtilException",e.getMessage());
                }finally {
                    if( connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

}

//                String cookie = connection.getHeaderField("Set-Cookie");
//                LogUtil.d("日志:cookie:",cookie);
//                String[] datas = cookie.split(";");
//                for (String s : datas){
//                    LogUtil.d("日志:cookie(拆分):",s);
//                }

//                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                out.writeBytes(JSONUtil.getPramsString(keys,values));