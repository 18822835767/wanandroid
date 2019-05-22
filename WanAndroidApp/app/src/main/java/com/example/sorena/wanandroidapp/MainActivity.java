package com.example.sorena.wanandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.adapter.MainActivityViewPagerAdapter;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseHelper;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseOperator;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.manager.CollectManager;
import com.example.sorena.wanandroidapp.manager.OldCollectManager;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.PermissionUtils;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.view.AccountActivity;
import com.example.sorena.wanandroidapp.view.ShowCollectActivity;
import com.example.sorena.wanandroidapp.view.WebActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener
{

    private ImageView myBarImageViewMenu;
    private TextView myBarTextViewMessage;
    private DrawerLayout mDrawerLayoutRoot;
    private ViewPager mMainViewPagerRoot;
    private RadioGroup mMainRadioGroupBottomMenu;
    private ImageView mMainActivityImageViewAccount;
    private TextView mTextViewShowUserName;
    private LinearLayout mMainActivityLinearLayoutChtholly;
    private LinearLayout mMainActivityLinearLayoutCollect;
    private LinearLayout mMainActivityLinearLayoutExit;
    private String[] mPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtil.cancelActionBar(this);

        mPermission = new String[]
                {PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE,PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE};

        /*好像莫得什么用*/
        //申请权限
        Thread loadUIThread = new Thread(()->{
            Thread permissionThread = new Thread(()->{
                if (!PermissionUtils.permissionAllow(this,mPermission)){
                    PermissionUtils.requestPermission(this,1,mPermission);
                }
            });
            permissionThread.start();
            try {
                permissionThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(()->{
                initItem();
                setView();
                initDataBase();
            });
        });
        loadUIThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length == 2){
                    LogUtil.d("日志","权限申请成功");
                }
                else {
                    runOnUiThread(()->Toast.makeText(this,"拒绝权限可能导致无法正常使用",Toast.LENGTH_SHORT).show());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = SharedPreferencesHelper.getUserData();
        if (mTextViewShowUserName == null){
            return;
        }
        if (!user.dataIsNull()){
            mTextViewShowUserName.setText("欢迎     " + user.getUserName());
            mTextViewShowUserName.setVisibility(View.VISIBLE);
        }else {
            mTextViewShowUserName.setVisibility(View.GONE);
        }
    }

    //初始化activity中的控件
    private void initItem(){
        myBarImageViewMenu = findViewById(R.id.myBar_imageView_menu);
        myBarTextViewMessage = findViewById(R.id.myBar_textView_message);
        mDrawerLayoutRoot = findViewById(R.id.layout_drawerLayout_root);
        mMainViewPagerRoot = findViewById(R.id.main_viewPager_root);
        mMainRadioGroupBottomMenu = findViewById(R.id.main_radioGroup_bottomMenu);
        mTextViewShowUserName = findViewById(R.id.mainActivity_textView_showUserName);
        mMainActivityLinearLayoutChtholly = findViewById(R.id.mainActivity_LinearLayout_chtholly);
        mMainActivityLinearLayoutCollect = findViewById(R.id.mainActivity_LinearLayout_collect);
        mMainActivityLinearLayoutExit = findViewById(R.id.mainActivity_LinearLayout_exit);
        mMainRadioGroupBottomMenu.setOnCheckedChangeListener(this);
        myBarImageViewMenu.setOnClickListener(this);
        myBarTextViewMessage.setOnClickListener(this);
        mMainViewPagerRoot.addOnPageChangeListener(this);
        mMainActivityLinearLayoutChtholly.setOnClickListener(this);
        mMainActivityLinearLayoutCollect.setOnClickListener(this);
        mMainActivityLinearLayoutExit.setOnClickListener(this);
        mMainRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_home);
        mMainViewPagerRoot.setOffscreenPageLimit(3);
        mMainActivityImageViewAccount = findViewById(R.id.mainActivity_imageView_account);
        mMainActivityImageViewAccount.setOnClickListener(this);
    }

    void initDataBase(){
        SearchHistoryDataBaseHelper helper = new SearchHistoryDataBaseHelper(this,"History.db",null,1);
        helper.getWritableDatabase();

        SearchHistoryDataBaseOperator operator = SearchHistoryDataBaseOperator.getInstance();

    }

    //设置ViewPager和TabLayout相关的内容
    private void setView(){
        MainActivityViewPagerAdapter pagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        mMainViewPagerRoot.setAdapter(pagerAdapter);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.myBar_imageView_menu:
                mDrawerLayoutRoot.openDrawer(GravityCompat.START);
                break;
            case R.id.mainActivity_imageView_account:
                intent = new Intent(this,AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.mainActivity_LinearLayout_chtholly:
                intent = new Intent(this,WebActivity.class);
                intent.putExtra("url","https://www.chtholly.ac.cn/");
                startActivity(intent);
                break;
            case R.id.mainActivity_LinearLayout_exit:
                SharedPreferencesHelper.delData();
                CollectManager.getInstance().clearCollectSet();
                mTextViewShowUserName.setVisibility(View.GONE);
                break;
            case R.id.mainActivity_LinearLayout_collect:
                User user = SharedPreferencesHelper.getUserData();
                if (user.dataIsNull()){
                    Toast.makeText(this,R.string.request_login,Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this,ShowCollectActivity.class);
                startActivity(intent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
