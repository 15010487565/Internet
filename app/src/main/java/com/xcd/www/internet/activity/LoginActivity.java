package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.fragment.LoginCodeFragment;
import com.xcd.www.internet.fragment.LoginPasswordFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.activity.CheckPermissionsActivity;
import www.xcd.com.mylibrary.base.fragment.BaseFragment;

public class LoginActivity extends CheckPermissionsActivity implements CompoundButton.OnCheckedChangeListener{

    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    public static Class<?> fragmentArray[] = {
            LoginCodeFragment.class,
            LoginPasswordFragment.class,

    };
    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private TextView tvLoginRegister, tvLoginForgetPassword;
    private RadioButton rbLoginCode,rbLoginPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 初始化fragments
        initFragments();
        initView();
        clickFragmentBtn(0);

    }

    /**
     * 初始化fragments
     */
    protected void initFragments() {
        // 初始化fragments
        for (int i = 0; i < fragmentArray.length; i++) {
            BaseFragment fragment = null;
            try {
                fragment = (BaseFragment) fragmentArray[i].newInstance();
                fragment.setActivity(this);
            } catch (Exception e) {
            }
            fragmentList.add(fragment);
        }
        // 为布局添加fragment,开启事物
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tran = fm.beginTransaction();

        //R.id.relative为布局
        tran.add(R.id.frame_login, fragmentList.get(0), "logincode").show(fragmentList.get(0))
                .add(R.id.frame_login, fragmentList.get(1), "loginpassword").hide(fragmentList.get(1));

        tran.commit();
    }

    private void initView() {
        //登录方式
        rbLoginCode = findViewById(R.id.rb_LoginCode);
        rbLoginCode.setOnCheckedChangeListener(this);
        rbLoginPwd = findViewById(R.id.rb_LoginPwd);
        rbLoginPwd.setOnCheckedChangeListener(this);
        //注册
        tvLoginRegister = findViewById(R.id.tv_LoginRegister);
        tvLoginRegister.setOnClickListener(this);
        //忘记密码
        tvLoginForgetPassword = findViewById(R.id.tv_LoginForgetPassword);
        tvLoginForgetPassword.setOnClickListener(this);
    }

    private void clickFragmentBtn(int tabIndex) {
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == tabIndex) {
                fragmentTransaction.show(fragmentList.get(i));
            } else {
                fragmentTransaction.hide(fragmentList.get(i));
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.tv_LoginRegister://注冊
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.tv_LoginForgetPassword://忘記密碼
                startActivity(new Intent(this,UpdataPwdActivity.class));
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

    }

    @Override
    public void onCancelResult() {

    }

    @Override
    public void onErrorResult(int errorCode, IOException errorExcep) {

    }

    @Override
    public void onParseErrorResult(int errorCode) {

    }

    @Override
    public void onFinishResult() {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id){
            case R.id.rb_LoginCode://验证码登录
                clickFragmentBtn(1);
                break;
            case R.id.rb_LoginPwd://密码登录
                clickFragmentBtn(0);
                break;
        }
    }

}
