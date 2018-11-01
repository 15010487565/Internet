package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class AuthorBankActivity extends SimpleTopbarActivity {

    private EditText etAuthotBankName,
            etAuthotBankNumber,
            etAuthotBankCountry,
            etAuthotBankDeposit,
            etAuthotBankPhone,
            etAuthotBankCard
                    ;
    private TextView tvAuthorBank;
    @Override
    protected Object getTopbarTitle() {
        return "绑定银行卡";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_bank);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        etAuthotBankName = findViewById(R.id.et_AuthotBankName);
        etAuthotBankNumber = findViewById(R.id.et_AuthotBankNumber);
        etAuthotBankCountry = findViewById(R.id.et_AuthotBankCountry);
        etAuthotBankDeposit = findViewById(R.id.et_AuthotBankDeposit);
        etAuthotBankPhone = findViewById(R.id.et_AuthotBankPhone);
        etAuthotBankCard = findViewById(R.id.et_AuthotBankCard);

        tvAuthorBank = findViewById(R.id.tv_AuthorBank);
        tvAuthorBank.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_AuthorBank:
                BaseApplication instance = BaseApplication.getInstance();
                String passwordPay = instance.getPasswordPay();
                if (TextUtils.isEmpty(passwordPay)){
                    ToastUtil.showToast("请先设置支付密码！");
                    return;
                }
                String name = etAuthotBankName.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    ToastUtil.showToast("持卡人姓名不能为空！");
                    return;
                }
                String banknumber = etAuthotBankNumber.getText().toString().trim();
                if (TextUtils.isEmpty(banknumber)){
                    ToastUtil.showToast("卡号不能为空！");
                    return;
                }
                String country = etAuthotBankCountry.getText().toString().trim();
                if (TextUtils.isEmpty(country)){
                    ToastUtil.showToast("国家不能为空！");
                    return;
                }
                String deposit = etAuthotBankDeposit.getText().toString().trim();
                if (TextUtils.isEmpty(deposit)){
                    ToastUtil.showToast("开户行不能为空！");
                    return;
                }
                String phone = etAuthotBankPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    ToastUtil.showToast("手机号不能为空！");
                    return;
                }
                String iDCard = etAuthotBankCard.getText().toString().trim();
                if (TextUtils.isEmpty(iDCard)){
                    ToastUtil.showToast("身份证号不能为空！");
                    return;
                }
                String sign = instance.getSign();
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("cardNum", banknumber);
                map.put("country",country);
                map.put("bankDeposit", deposit);
                map.put("phone", phone);
                map.put("idnum", iDCard);
                map.put("sign", sign);
                okHttpPostBody(100, GlobalParam.BINDCARD, map);
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
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
