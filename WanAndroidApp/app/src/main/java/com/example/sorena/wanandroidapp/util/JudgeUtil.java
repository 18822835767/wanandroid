package com.example.sorena.wanandroidapp.util;

import android.widget.EditText;
import android.widget.TextView;

public class JudgeUtil
{
    /**
     *
     * @param objects:要判断的object数组
     * @return 是否包含null
     */
    public static boolean dataContainsNull(Object... objects){

        for (Object o : objects){
            if (o == null){
                return true;
            }
        }
        return false;
    }

    //判断editText是否有没有输入文字的
    public static boolean editTextContainsEmpty(EditText... textViews){
        for (EditText textView : textViews){
            if (textView.getText().toString().equals("")){
                return true;
            }
        }
        return false;
    }










}
