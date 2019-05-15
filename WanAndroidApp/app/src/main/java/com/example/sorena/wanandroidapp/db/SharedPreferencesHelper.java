package com.example.sorena.wanandroidapp.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.util.MyApplication;

public class SharedPreferencesHelper
{

    public static void remenberUser(String userName , String userPassword)
    {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString("loginUserName",userName);
        editor.putString("loginUserPassword",userPassword);
        editor.apply();
    }

    public static User getUserData(){

        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        String userName = preferences.getString("loginUserName","");
        String userPassword = preferences.getString("loginUserPassword","");
        return new User(userName,userPassword);
    }


    public static void delData(){
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.remove("loginUserName");
        editor.remove("loginUserPassword");
        editor.apply();
    }

}
