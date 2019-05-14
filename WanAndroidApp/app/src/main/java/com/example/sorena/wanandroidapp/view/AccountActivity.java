package com.example.sorena.wanandroidapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.ViewSwitcher;
import com.example.sorena.wanandroidapp.util.ViewUtil;

public class AccountActivity extends AppCompatActivity implements LoginFragment.SwitchToRegisterAble, RegisterFragment.SwitchToLoginAble{

    private ViewSwitcher switcher;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ViewUtil.cancelActionBar(this);

        textView = findViewById(R.id.mySystemBar_textView_message);
        textView.setText("登录");
        switcher = new ViewSwitcher(this,R.id.accountActivity_frameLayout_forReplace);
        switcher.replace(new RegisterFragment());
    }


    @Override
    public void switchToRegister() {
        switcher.replace(new RegisterFragment());
        textView.setText("注册");
    }

    @Override
    public void switchToLogin() {
        switcher.replace(new LoginFragment());
        textView.setText("登录");
    }
}
