package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class MeAboutActivity extends SimpleTopbarActivity {

    private TextView tvVersionNumber;
    @Override
    protected Object getTopbarTitle() {
        return R.string.me_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_about);
        BaseApplication instance = BaseApplication.getInstance();
        try {
            String versionName = instance.getVersionName();
            tvVersionNumber.setText("版本号 "+versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        tvVersionNumber = findViewById(R.id.tv_VersionNumber);
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
