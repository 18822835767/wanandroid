package com.example.sorena.wanandroidapp.backups;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

//用于第一版用viewPager和tabLayout实现的类微信布局
@Deprecated
public class MainActivityTabLayoutDataAdapter
{

    private List<String> texts;
    private List<Integer> normalImages;
    private List<Integer> selectImages;

    public MainActivityTabLayoutDataAdapter(){

        texts = new ArrayList<>();
        texts.add("首页");
        texts.add("体系");
        texts.add("导航");
        texts.add("项目");

        normalImages = new ArrayList<>();
        normalImages.add(R.drawable.ic_home_normal);
        normalImages.add(R.drawable.ic_system_normal);
        normalImages.add(R.drawable.ic_system_normal);
        normalImages.add(R.drawable.ic_project_normal);

        selectImages = new ArrayList<>();
        selectImages.add(R.drawable.ic_home_selected);
        selectImages.add(R.drawable.ic_system_selected);
        selectImages.add(R.drawable.ic_system_selected);
        selectImages.add(R.drawable.ic_project_selected);
    }



    public View getTabView(int position){
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.tab_item , null);
        TextView textView = view.findViewById(R.id.item_textView_text);
        ImageView imageView = view.findViewById(R.id.item_imageView_image);
        textView.setText(texts.get(position));
        textView.setTextColor(Color.parseColor("#dbdbdb"));
        imageView.setImageResource(normalImages.get(position));
        return view;
    }

    public View getNormalView(int position){

        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.tab_item , null);
        TextView textView = view.findViewById(R.id.item_textView_text);
        ImageView imageView = view.findViewById(R.id.item_imageView_image);
        textView.setText(texts.get(position));
        textView.setTextColor(Color.parseColor("#dbdbdb"));
        imageView.setImageResource(normalImages.get(position));
        return view;
    }

    public View getSelectView(int position){
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.tab_item , null);
        TextView textView = view.findViewById(R.id.item_textView_text);
        ImageView imageView = view.findViewById(R.id.item_imageView_image);
        textView.setText(texts.get(position));
        textView.setTextColor(Color.parseColor("#1195db"));
        imageView.setImageResource(selectImages.get(position));
        return view;
    }

    public void setNormal(View view, int position){
        TextView textView = view.findViewById(R.id.item_textView_text);
        ImageView imageView = view.findViewById(R.id.item_imageView_image);
        textView.setText(texts.get(position));
        textView.setTextColor(Color.parseColor("#dbdbdb"));
        imageView.setImageResource(normalImages.get(position));
    }

    public void setSelect(View view, int position){
        TextView textView = view.findViewById(R.id.item_textView_text);
        ImageView imageView = view.findViewById(R.id.item_imageView_image);
        textView.setText(texts.get(position));
        textView.setTextColor(Color.parseColor("#1195db"));
        imageView.setImageResource(selectImages.get(position));
    }

    public List<String> getTexts() {
        return texts;
    }

    public List<Integer> getNormalImages() {
        return normalImages;
    }

    public List<Integer> getSelectImages() {
        return selectImages;
    }
}
