package com.example.sorena.wanandroidapp.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.SystemViewPagerAdapter;
import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.widget.SystemBarLayout;

/**
 *
 */
public class SystemActivity extends AppCompatActivity {

    private SystemBarLayout mBarLayoutBar;
    private TabLayout mTabLayoutShowItemName;
    private ViewPager mViewPagerShowItem;
    private SystemViewPagerAdapter mSystemViewPagerAdapter;
    private TextView mySystemBarTextViewMessage;
    private Chapter mChapter;
    private int mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        ViewUtil.cancelActionBar(this);

        initData();
        initView();
    }

    private void initData() {

        Intent intent = getIntent();
        mChapter = (Chapter)intent.getSerializableExtra("chapterData");
        mPosition = Integer.parseInt(intent.getStringExtra("mPosition"));
    }

    private void initView() {
        mBarLayoutBar = findViewById(R.id.systemActivity_BarLayout_bar);
        mTabLayoutShowItemName = findViewById(R.id.systemActivity_tabLayout_showItemName);
        mViewPagerShowItem = findViewById(R.id.systemActivity_viewPager_showItem);
        mySystemBarTextViewMessage = findViewById(R.id.mySystemBar_textView_message);
        mTabLayoutShowItemName.setupWithViewPager(mViewPagerShowItem);
        mSystemViewPagerAdapter = new SystemViewPagerAdapter(getSupportFragmentManager(),mChapter);
        mViewPagerShowItem.setAdapter(mSystemViewPagerAdapter);
        mViewPagerShowItem.setCurrentItem(mPosition,false);
        mySystemBarTextViewMessage.setText(mChapter.getChapterName());
    }

}
