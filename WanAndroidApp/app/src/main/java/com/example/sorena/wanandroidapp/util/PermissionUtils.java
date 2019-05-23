package com.example.sorena.wanandroidapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.LinkedList;
import java.util.List;

/**
 * 权限申请的工具类
 */
public class PermissionUtils
{
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    /**
     * 申请权限
     * @param activity 活动
     * @param requestCode 请求码
     * @param permissions 要申请的权限集合
     */
    public static void requestPermission(final Activity activity, final  int requestCode,String[] permissions){

        List<String> permissionsRequest = new LinkedList<>();
        for (int i = 0; i < permissions.length; i++) {
            if ( ContextCompat.checkSelfPermission(activity , permissions[i]) != PackageManager.PERMISSION_GRANTED){
                permissionsRequest.add(permissions[i]);
            }
        }
        if (permissionsRequest.size() != 0){
            try {
                String[] strings = new String[permissionsRequest.size()];
                for (int i = 0; i < permissionsRequest.size() ; i++) {
                    strings[i] = permissionsRequest.get(i);
                }
                ActivityCompat.requestPermissions(activity,strings,requestCode);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.e("日志:PermissionUtils",e.getMessage());
            }
        }
    }

    /**
     * 判断权限有没有全部申请成功
     */
    public static boolean permissionAllow(Context context, String[] permissions){
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context,permissions[i]) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }



}
