package com.example.sorena.wanandroidapp.util;

public class JudgeUtil
{
    public static boolean dataContainsNull(Object... objects){

        for (Object o : objects){
            if (o == null){
                return true;
            }
        }
        return false;
    }











}
