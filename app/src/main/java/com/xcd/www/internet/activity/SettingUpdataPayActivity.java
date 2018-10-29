package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.CodeCheckModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class SettingUpdataPayActivity extends SimpleTopbarActivity {

    private TextView tvPaypasswordOk;
    private EditText etPaypassword1, etPaypassword2;
    String payCode;

    @Override
    protected Object getTopbarTitle() {
        return "修改支付密码";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_updata_pay);
        payCode = getIntent().getStringExtra("payCode");
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        etPaypassword1 = findViewById(R.id.et_Paypassword1);
        etPaypassword2 = findViewById(R.id.et_Paypassword2);

        tvPaypasswordOk = findViewById(R.id.tv_PaypasswordOk);
        tvPaypasswordOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_PaypasswordOk:
                String trim = etPaypassword1.getText().toString().trim();
                String trim2 = etPaypassword2.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.showToast("支付密码不能为空");
                    return;
                }
                if (trim.length() < 6) {
                    ToastUtil.showToast("密码长度不能小于6位！");
                    return;
                }

                if (!trim.equals(trim2)) {
                    ToastUtil.showToast("您输入的两次密码不相同");
                    return;
                }
                String account = BaseApplication.getInstance().getAccount();
                String sign = BaseApplication.getInstance().getSign();
                String country = BaseApplication.getInstance().getCountry();
                Map<String, String> mapCode = new HashMap<>();
                mapCode.put("country ", country);
                mapCode.put("sign", sign);
                mapCode.put("account", account);
                mapCode.put("code", payCode);
                okHttpPostBody(101, GlobalParam.CHECKCODE, mapCode);


                break;

        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

        switch (requestCode) {
            case 100:

                if (returnCode == 200) {
                    String passwordPay = etPaypassword1.getText().toString().trim();
                    BaseApplication.getInstance().setPasswordPay(passwordPay);
                    finish();
                } else {
                    ToastUtil.showToast(returnMsg);
                }
                break;
            case 101:
                if (returnCode == 200) {

                    CodeCheckModel codeCheckModel = JSON.parseObject(returnData, CodeCheckModel.class);
                    String sign1 = codeCheckModel.getData().getSign();
                    String passwordPay = etPaypassword1.getText().toString().trim();
                    String sign = BaseApplication.getInstance().getSign();
                    Map<String, String> params = new HashMap<>();
                    params.put("code", sign1);
                    params.put("sign", sign);
                    params.put("password", passwordPay);
                    okHttpPostBody(100, GlobalParam.PAYPASSWORD, params);
                } else {
                    ToastUtil.showToast(returnMsg);
                }

                break;
        }


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
