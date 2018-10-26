package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.base.BaseInternetActivity;

import java.io.IOException;
import java.util.Map;

/**
 * 设置页
 */
public class SettingActivity extends BaseInternetActivity {

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
    public void onClick(View v) {
        super.onClick(v);

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
