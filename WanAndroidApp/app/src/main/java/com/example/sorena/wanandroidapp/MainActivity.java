package com.example.sorena.wanandroidapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.adapter.MainActivityViewPagerAdapter;
import com.example.sorena.wanandroidapp.bean.User;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseHelper;
import com.example.sorena.wanandroidapp.db.SearchHistoryDataBaseOperator;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.manager.CollectManager;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.PermissionUtils;
import com.example.sorena.wanandroidapp.util.ViewUtil;
import com.example.sorena.wanandroidapp.view.AccountActivity;
import com.example.sorena.wanandroidapp.view.BaseActivity;
import com.example.sorena.wanandroidapp.view.ShowCollectActivity;
import com.example.sorena.wanandroidapp.view.WebActivity;

public class MainActivity extends BaseActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener
{

    private ImageView myBarImageViewMenu;
    private TextView myBarTextViewMessage;
    private DrawerLayout mDrawerLayoutRoot;
    private ViewPager mViewPagerRoot;
    private RadioGroup mRadioGroupBottomMenu;
    private ImageView mImageViewAccount;
    private TextView mShowUserName;
    private LinearLayout mLinearLayoutChtholly;
    private LinearLayout mLinearLayoutCollect;
    private LinearLayout mLinearLayoutExit;
    private String[] mPermission;

    private RadioButton mHomeButtom;
    private RadioButton mSystemButton;
    private RadioButton mNavigationButton;
    private RadioButton mProjectButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtil.cancelActionBar(this);
        initView();

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


    void initView(){
        mHomeButtom = findViewById(R.id.bottomMenu_radioButton_home);
        mSystemButton = findViewById(R.id.bottomMenu_radioButton_system);
        mNavigationButton = findViewById(R.id.bottomMenu_radioButton_navigation);
        mProjectButton = findViewById(R.id.bottomMenu_radioButton_project);

        Drawable hd =   getResources().getDrawable(R.drawable.selector_main_view_tab_home);
        hd.setBounds(0,0,50,50);
        mHomeButtom.setCompoundDrawables(null,hd,null,null);

        Drawable sd =   getResources().getDrawable(R.drawable.selector_main_view_tab_system);
        sd.setBounds(0,0,70,70);
        mSystemButton.setCompoundDrawables(null,sd,null,null);

        Drawable nd =   getResources().getDrawable(R.drawable.selector_main_view_tab_navigation);
        nd.setBounds(0,0,70,70);
        mNavigationButton.setCompoundDrawables(null,nd,null,null);

        Drawable pd =   getResources().getDrawable(R.drawable.selector_main_view_tab_project);
        pd.setBounds(0,0,70,70);
        mProjectButton.setCompoundDrawables(null,pd,null,null);


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
        LogUtil.d("日志:BaseActivity:MainActivity",user.toString());
        if (mShowUserName == null){
            LogUtil.d("日志:BaseActivity:MainActivity:","TextViewShowUserName" + "为空");
            mShowUserName = findViewById(R.id.mainActivity_textView_showUserName);
        }
        if (!user.dataIsNull()){
            mShowUserName.setText("欢迎     " + user.getUserName());
            mShowUserName.setVisibility(View.VISIBLE);
        }else {
            mShowUserName.setVisibility(View.GONE);
        }
    }

    //初始化activity中的控件
    private void initItem(){
        myBarImageViewMenu = findViewById(R.id.myBar_imageView_menu);
        myBarTextViewMessage = findViewById(R.id.myBar_textView_message);
        mDrawerLayoutRoot = findViewById(R.id.layout_drawerLayout_root);
        mViewPagerRoot = findViewById(R.id.main_viewPager_root);
        mRadioGroupBottomMenu = findViewById(R.id.main_radioGroup_bottomMenu);
        mShowUserName = findViewById(R.id.mainActivity_textView_showUserName);
        mLinearLayoutChtholly = findViewById(R.id.mainActivity_LinearLayout_chtholly);
        mLinearLayoutCollect = findViewById(R.id.mainActivity_LinearLayout_collect);
        mLinearLayoutExit = findViewById(R.id.mainActivity_LinearLayout_exit);
        mRadioGroupBottomMenu.setOnCheckedChangeListener(this);
        myBarImageViewMenu.setOnClickListener(this);
        myBarTextViewMessage.setOnClickListener(this);
        mViewPagerRoot.addOnPageChangeListener(this);
        mLinearLayoutChtholly.setOnClickListener(this);
        mLinearLayoutCollect.setOnClickListener(this);
        mLinearLayoutExit.setOnClickListener(this);
        mRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_home);
        mViewPagerRoot.setOffscreenPageLimit(3);
        mImageViewAccount = findViewById(R.id.mainActivity_imageView_account);
        mImageViewAccount.setOnClickListener(this);
    }

    void initDataBase(){
        SearchHistoryDataBaseHelper helper = new SearchHistoryDataBaseHelper(this,"History.db",null,1);
        helper.getWritableDatabase();

        SearchHistoryDataBaseOperator operator = SearchHistoryDataBaseOperator.getInstance();

    }

    //设置ViewPager和TabLayout相关的内容
    private void setView(){
        MainActivityViewPagerAdapter pagerAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        mViewPagerRoot.setAdapter(pagerAdapter);
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
                mShowUserName.setVisibility(View.GONE);
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
                mViewPagerRoot.setCurrentItem(0);
                break;
            case R.id.bottomMenu_radioButton_system:
                mViewPagerRoot.setCurrentItem(1);
                break;
            case R.id.bottomMenu_radioButton_navigation:
                mViewPagerRoot.setCurrentItem(2);
                break;
            case R.id.bottomMenu_radioButton_project:
                mViewPagerRoot.setCurrentItem(3);
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
                mRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_home);
                myBarTextViewMessage.setText("首页");
                break;
            case 1:
                mRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_system);
                myBarTextViewMessage.setText("体系");
                break;
            case 2:
                mRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_navigation);
                myBarTextViewMessage.setText("导航");
                break;
            case 3:
                mRadioGroupBottomMenu.check(R.id.bottomMenu_radioButton_project);
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
