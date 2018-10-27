package com.xcd.www.internet.activity;

import android.os.Bundle;

import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class CashInfoOKDActivity extends SimpleTopbarActivity {

    @Override
    protected Object getTopbarTitle() {
        return "购买okd送lit";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_info_okd);
        String sign = BaseApplication.getInstance().getSign();
        Map<String, String> params = new HashMap<>();
        params.put("coin", "usdt");
        params.put("sign", sign);
        okHttpPostBody(100, GlobalParam.CASH, params);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
                case 100:
                    finish();
                    break;
            }
        }
        ToastUtil.showToast(returnMsg);
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
