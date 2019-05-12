package com.example.sorena.wanandroidapp;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.adapter.MainActivityTabLayoutDataAdapter;
import com.example.sorena.wanandroidapp.adapter.MainActivityViewPagerAdapter;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.MyApplication;
import com.example.sorena.wanandroidapp.util.ViewUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener
{

    private ImageView myBarImageViewMenu;
    private TextView myBarTextViewMessage;
    private DrawerLayout mDrawerLayoutRoot;
    private ViewPager mMainViewPagerRoot;
    private RadioGroup mMainRadioGroupBottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtil.cancelActionBar(this);
        initItem();
        setView();
    }

    //初始化activity中的控件
    private void initItem(){
        myBarImageViewMenu = findViewById(R.id.myBar_imageView_menu);
        myBarTextViewMessage = findViewById(R.id.myBar_textView_message);
        mDrawerLayoutRoot = findViewById(R.id.layout_drawerLayout_root);
        mMainViewPagerRoot = findViewById(R.id.main_viewPager_root);
        mMainRadioGroupBottomMenu = findViewById(R.id.main_radioGroup_bottomMenu);
        mMainRadioGroupBottomMenu.setOnCheckedChangeListener(this);
        myBarImageViewMenu.setOnClickListener(this);
        myBarTextViewMessage.setOnClickListener(this);
        mMainViewPagerRoot.addOnPageChangeListener(this);
        mMainRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_home);
        mMainViewPagerRoot.setOffscreenPageLimit(3);
    }

    //设置ViewPager和TabLayout相关的内容
    private void setView(){

        MainActivityViewPagerAdapter pagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        mMainViewPagerRoot.setAdapter(pagerAdapter);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.myBar_imageView_menu:
                mDrawerLayoutRoot.openDrawer(GravityCompat.START);
                break;

            default:
                break;
        }
    }

    //RadioGroup.OnCheckedChangeListener要重写的一个方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){

            case R.id.bottomMenu_radioButton_home:
                mMainViewPagerRoot.setCurrentItem(0);
                break;
            case R.id.bottomMenu_radioButton_system:
                mMainViewPagerRoot.setCurrentItem(1);
                break;
            case R.id.bottomMenu_radioButton_navigation:
                mMainViewPagerRoot.setCurrentItem(2);
                break;
            case R.id.bottomMenu_radioButton_project:
                mMainViewPagerRoot.setCurrentItem(3);
                break;

        }
    }



    // ViewPager.OnPageChangeListener要重写的三个方法
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i){
            case 0:
                mMainRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_home);
                myBarTextViewMessage.setText("首页");
                break;
            case 1:
                mMainRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_system);
                myBarTextViewMessage.setText("体系");
                break;
            case 2:
                mMainRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_navigation);
                myBarTextViewMessage.setText("导航");
                break;
            case 3:
                mMainRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_project);
                myBarTextViewMessage.setText("项目");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {}

}
