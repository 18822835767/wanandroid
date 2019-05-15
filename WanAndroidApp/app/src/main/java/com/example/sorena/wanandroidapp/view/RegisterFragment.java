package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.JudgeUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;

public class RegisterFragment extends Fragment implements View.OnClickListener
{
    private EditText EditTextUserNameInput;
    private EditText mEditTextUserPasswordInput;
    private EditText mEditTextUserPasswordConfine;
    private Button mButtonRegister;
    private TextView mTextViewGoToLogin;
    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            switcher = (SwitchToLoginAble)context;
        }catch (ClassCastException e){
            LogUtil.e("日志:警告","传入了未实现SwitchToLoginAble的类");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        EditTextUserNameInput = getActivity().findViewById(R.id.registerFragment_editText_userNameInput);
        mEditTextUserPasswordInput = getActivity().findViewById(R.id.registerFragment_editText_userPasswordInput);
        mEditTextUserPasswordConfine = getActivity().findViewById(R.id.registerFragment_editText_userPasswordConfine);
        mButtonRegister = getActivity().findViewById(R.id.registerFragment_button_register);
        mTextViewGoToLogin = getActivity().findViewById(R.id.registerFragment_textView_goToLogin);
        mTextViewGoToLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
    }


    private SwitchToLoginAble switcher;
    interface SwitchToLoginAble{
        void switchToLogin();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.registerFragment_textView_goToLogin:
                if (switcher != null){
                    switcher.switchToLogin();
                }
                break;

            case R.id.registerFragment_button_register:
                if (JudgeUtil.editTextContainsEmpty(EditTextUserNameInput,
                        mEditTextUserPasswordConfine, mEditTextUserPasswordInput)){
                    return;
                }else if (!mEditTextUserPasswordInput.getText().toString().equals(mEditTextUserPasswordConfine.getText().toString())){
                    getActivity().runOnUiThread(()->Toast.makeText(getContext(),"两次输入的密码不同",Toast.LENGTH_SHORT).show());
                }else {
                    sendRegisterMessage();
                }
                break;
            default:
                break;
        }
    }

    void parseData(String response){

        String data = JSONUtil.getValue("data",response,new String[]{});
        if (data != null){
            LogUtil.d("日志:data",data);
        }else {
            LogUtil.d("日志","data为null");
            return;
        }
        String id = JSONUtil.getValue("id",data,new String[]{});
        String name = JSONUtil.getValue("username",data,new String[]{});
        if (id == null){
            String errMeg = JSONUtil.getValue("errorMsg",response,new String[]{});
            LogUtil.d("日志:错误信息",errMeg);
            Toast.makeText(getContext(),"错误:" + errMeg , Toast.LENGTH_SHORT).show();
        }else{
            LogUtil.d("日志","id:" + id + "    name:" + name);
            Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
        }

    }



    void sendRegisterMessage(){
        HttpUtil.sendPostRequest("https://www.wanandroid.com/user/register",
                new String[]{"username", "password", "repassword"},
                new String[]{EditTextUserNameInput.getText().toString(),
                        mEditTextUserPasswordInput.getText().toString(),
                        mEditTextUserPasswordConfine.getText().toString()},
                new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                LogUtil.d("日志:","注册返回数据:" + response);
                getActivity().runOnUiThread(()->{
                    parseData(response);
                });
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }
}
