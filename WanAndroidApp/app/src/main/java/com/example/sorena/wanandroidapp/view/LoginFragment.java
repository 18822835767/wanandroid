package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.BaseFragment;
import com.example.sorena.wanandroidapp.util.LogUtil;

public class LoginFragment extends BaseFragment implements View.OnClickListener
{

    private EditText loginFragmentEditTextUserNameInput;
    private EditText loginFragmentEditTextUserPasswordInput;
    private Button loginFragmentButtonRegister;
    private TextView loginFragmentTextViewGoRegister;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        loginFragmentEditTextUserNameInput = getActivity().findViewById(R.id.loginFragment_editText_userNameInput);
        loginFragmentEditTextUserPasswordInput = getActivity().findViewById(R.id.loginFragment_editText_userPasswordInput);
        loginFragmentButtonRegister = getActivity().findViewById(R.id.loginFragment_button_register);
        loginFragmentTextViewGoRegister = getActivity().findViewById(R.id.loginFragment_textView_goRegister);
        loginFragmentTextViewGoRegister.setOnClickListener(this);
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

            default:
                break;



        }
    }
}
