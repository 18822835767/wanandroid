package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.db.SharedPreferencesHelper;
import com.example.sorena.wanandroidapp.util.HttpUtil;
import com.example.sorena.wanandroidapp.util.JSONUtil;
import com.example.sorena.wanandroidapp.util.JudgeUtil;
import com.example.sorena.wanandroidapp.util.LogUtil;

/**
 * 登录碎片
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener
{

    private EditText mEditTextUserNameInput;
    private EditText mEditTextUserPasswordInput;
    private Button mButtonRegister;
    private TextView mTextViewGoRegister;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        try {
            switcher = (SwitchToRegisterAble)context;
        }catch (ClassCastException e){
            LogUtil.e("日志:错误","传入了未实现SwitchToRegisterAble接口的类");
        }
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        mEditTextUserNameInput = getActivity().findViewById(R.id.loginFragment_editText_userNameInput);
        mEditTextUserPasswordInput = getActivity().findViewById(R.id.loginFragment_editText_userPasswordInput);
        mButtonRegister = getActivity().findViewById(R.id.loginFragment_button_login);
        mTextViewGoRegister = getActivity().findViewById(R.id.loginFragment_textView_goRegister);
        mTextViewGoRegister.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
    }

    private SwitchToRegisterAble switcher;
    interface SwitchToRegisterAble{
        void switchToRegister();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.loginFragment_textView_goRegister:
                if (switcher != null){
                    switcher.switchToRegister();
                }
                break;
            case R.id.loginFragment_button_login:
                if (JudgeUtil.editTextContainsEmpty(mEditTextUserNameInput, mEditTextUserPasswordInput)){
                    return;
                }
                String name = mEditTextUserNameInput.getText().toString();
                String password = mEditTextUserPasswordInput.getText().toString();
                HttpUtil.sendPostRequest("https://www.wanandroid.com/user/login", new String[]{"username", "password"},
                        new String[]{name,password},
                        new HttpUtil.HttpCallBackListener() {
                            @Override
                            public void onFinish(String response) {
                                String data = JSONUtil.getValue("errorMsg",response,new String[]{});
                                if (data == null){
                                    return;
                                }else if (data.equals("")){
                                    SharedPreferencesHelper.rememberUser(name,password);
                                    getActivity().runOnUiThread(()->{
                                        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    });
                                }else {
                                    getActivity().runOnUiThread(()->Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show());
                                }

                            }

                            @Override
                            public void onError(Exception e) {}
                        });
                break;
            default:
                break;
        }
    }
}
