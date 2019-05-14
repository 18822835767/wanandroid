package com.example.sorena.wanandroidapp.util;

import android.widget.EditText;
import android.widget.TextView;

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

    public static boolean editTextContainsEmpty(EditText... textViews){
        for (EditText textView : textViews){
            if (textView.getText().toString().equals("")){
                return true;
            }
        }
        return false;
    }










}
