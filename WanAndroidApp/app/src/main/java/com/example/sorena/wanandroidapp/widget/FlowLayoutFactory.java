package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.bean.FlowItem;

import java.util.List;
import java.util.Random;

public class FlowLayoutFactory
{
    /**
     * 一个加载FlowLayout的Factory
     */

    /**
     *
     * @param context 上下文
     * @param stringList 每个textView的文字
     * @param tags 每个txtView要存储的tag
     * @param listener 监听器
     * @return
     */
    public static FlowLayout loadTextViewLayout(final Context context, List<String> stringList, @Nullable List<Object> tags,@Nullable View.OnClickListener listener){

        LayoutInflater inflater = LayoutInflater.from(context);
        FlowLayout flowLayout = new FlowLayout(context);
        for (int i = 0; i < stringList.size(); i++)
        {
            final TextView tv = (TextView) inflater.inflate(R.layout.system_flowlayout_tv,flowLayout, false);
            tv.setText(stringList.get(i));
            if (tags != null && tags.size() > i){
                tv.setTag(tags.get(i));
            }
            Random random = new Random();
            tv.setTextColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
            if (listener != null){
                tv.setOnClickListener(listener);
            }else {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,tv.getText(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            flowLayout.addView(tv);
        }
        return flowLayout;
    }


    /**
     *
     * @param flowLayout 要设置的flowLayout
     * @param context 上下文
     * @param stringList 每个textView的文字
     * @param tags 每个textView要存储的tag
     * @param listener 监听器
     * @param randomTextColor 是否随机生成颜色
     * @param resourceId 加载的资源id,只能是textView
     */
    public static void setFlowLayout(FlowLayout flowLayout, final Context context, int resourceId, List<String> stringList, @Nullable Object[] tags, @Nullable View.OnClickListener listener , boolean randomTextColor){

        flowLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < stringList.size(); i++)
        {
            final TextView tv = (TextView) inflater.inflate(resourceId ,flowLayout, false);
            tv.setText(stringList.get(i));
            if (tags != null && tags.length > i){
                tv.setTag(tags[i]);
            }
            if (randomTextColor) {
                Random random = new Random();
                tv.setTextColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
            }
            if (listener != null){
                tv.setOnClickListener(listener);
            }else {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,tv.getText(),Toast.LENGTH_SHORT).show();
                    }
                    });

            }
            flowLayout.addView(tv);
        }
    }




    /**
     *
     * @param character 要设置systemItem中的标题栏的文字
     * @param context 上下文
     * @param stringList 每个textView的文字
     * @param tags 每个txtView要存储的tag
     * @param listener 监听器
     */


    @Deprecated
    public static View getSystemItem(final Context context, List<String> stringList,String character, @Nullable List<FlowItem> tags,@Nullable View.OnClickListener listener){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.system_item,null);
        TextView textView = view.findViewById(R.id.systemItem_textView_character);
        textView.setText(character);
        FlowLayout flowLayout = view.findViewById(R.id.systemItem_flowLayout_item);
        //setFlowLayout(flowLayout,context,stringList,null,null);
        return view;
    }


    /**
     *
     * @param flowLayout 流式布局
     * @param context 上下文
     * @param resourceId 资源id
     * @param stringList 文字列表
     * @param tags 存储在每个item里的tag
     * @param listener 监听器
     * @param randomTextColor 是否设置随机颜色
     */

    public static void setFlowLayout(FlowLayout flowLayout, final Context context, int resourceId, List<String> stringList, @Nullable List<Object> tags, @Nullable View.OnClickListener listener , boolean randomTextColor){

        flowLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < stringList.size(); i++)
        {
            final TextView tv = (TextView) inflater.inflate(resourceId ,flowLayout, false);
            tv.setText(stringList.get(i));
            if (tags != null && tags.size() > i){
                tv.setTag(tags.get(i));
            }
            if (randomTextColor) {
                Random random = new Random();
                tv.setTextColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
            }
            if (listener != null){
                tv.setOnClickListener(listener);
            }else {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,tv.getText(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
            flowLayout.addView(tv);
        }
    }



}
