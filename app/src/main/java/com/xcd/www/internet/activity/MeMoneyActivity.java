package com.xcd.www.internet.activity;

import android.os.Bundle;

import com.xcd.www.internet.R;
import com.xcd.www.internet.func.MeMoneyRightTopBtnFunc;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class MeMoneyActivity extends SimpleTopbarActivity {

    private static Class<?> rightFuncArray[] = {MeMoneyRightTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "资产";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_money);
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
