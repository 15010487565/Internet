package com.xcd.www.internet.activity;

import android.os.Bundle;

import com.xcd.www.internet.R;
import com.xcd.www.internet.base.BaseInternetActivity;

import java.io.IOException;
import java.util.Map;

/**
 * 系统消息
 * Created by gs on 2018/10/19.
 */

public class SystemActivity extends BaseInternetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();

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
