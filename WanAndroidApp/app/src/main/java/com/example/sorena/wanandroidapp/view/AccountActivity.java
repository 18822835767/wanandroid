package com.example.sorena.wanandroidapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.ViewSwitcher;
import com.example.sorena.wanandroidapp.util.ViewUtil;

/**
 * 账户管理的界面,会填充登录碎片或注册碎片
 */
public class AccountActivity extends AppCompatActivity
        implements LoginFragment.SwitchToRegisterAble, RegisterFragment.SwitchToLoginAble{

    private ViewSwitcher mSwitcher;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ViewUtil.cancelActionBar(this);

        mTextView = findViewById(R.id.mySystemBar_textView_message);
        mTextView.setText("登录");
        mSwitcher = new ViewSwitcher(this,R.id.accountActivity_frameLayout_forReplace);
        mSwitcher.replace(new LoginFragment());
    }


    @Override
    public void switchToRegister() {
        mSwitcher.replace(new RegisterFragment());
        mTextView.setText("注册");
    }

    @Override
    public void switchToLogin() {
        mSwitcher.replace(new LoginFragment());
        mTextView.setText("登录");
    }
}
