package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;

import java.io.IOException;
import java.util.Map;

/**
 * 设置页
 */
public class SettingActivity extends BaseInternetActivity {

    private LinearLayout llSettingSafety;
    private TextView tvExitApp;
    @Override
    protected Object getTopbarTitle() {
        return R.string.setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //隐私与安全
        llSettingSafety = findViewById(R.id.ll_SettingSafety);
        llSettingSafety.setOnClickListener(this);

        tvExitApp = findViewById(R.id.tv_ExitApp);
        tvExitApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_SettingSafety:
                startActivity(new Intent(this,SettingSafetyActivity.class));
                break;
            case R.id.tv_ExitApp:
                BaseApplication.getInstance().exitApp();
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
