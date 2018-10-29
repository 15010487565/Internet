package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xcd.www.internet.R;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class SettingSafetyActivity extends SimpleTopbarActivity {

    private LinearLayout llSettingUpdataPay, llSettingUpdataLogin;
    @Override
    protected Object getTopbarTitle() {
        return R.string.setting_safety;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_safety);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //支付密码
        llSettingUpdataPay = findViewById(R.id.ll_SettingUpdataPay);
        llSettingUpdataPay.setOnClickListener(this);
        //登陆密码
        llSettingUpdataLogin = findViewById(R.id.ll_SettingUpdataLogin);
        llSettingUpdataLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()){
            case R.id.ll_SettingUpdataPay:
                intent = new Intent(this, SettingUpdataPayCodeActivity.class);
                intent.putExtra("updataType",1);//修改支付密码
                startActivity(intent);
                break;
            case R.id.ll_SettingUpdataLogin:
                intent = new Intent(this, SettingUpdataPayCodeActivity.class);
                intent.putExtra("updataType",2);//修改登录密码
                startActivity(intent);
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

}
