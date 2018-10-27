package com.xcd.www.internet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.func.CashUSDTAddressTopBtnFunc;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class CashUSDTAddressActivity extends SimpleTopbarActivity implements TextWatcher {

    private EditText etDes;
    private TextView tvDesCun;
    private static Class<?> rightFuncArray[] = {CashUSDTAddressTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }
    @Override
    protected Object getTopbarTitle() {
        return "群组简介";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashusdt_address);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        etDes = findViewById(R.id.et_Des);
        etDes.addTextChangedListener(this);
        tvDesCun = findViewById(R.id.tv_DesCun);
    }
    public void getEditorAddressDes(){
        String trim = etDes.getText().toString().trim();
        if (TextUtils.isEmpty(trim)){
            ToastUtil.showToast("钱包地址不能空！");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("CashUsdtAddress",trim);
        setResult(Activity.RESULT_OK,intent);
        finish();
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int l = etDes.length();
        tvDesCun.setText(l + "/100");//需要将数字转成字符串
        if (l >= 100) {
            tvDesCun.setTextColor(getResources().getColor(R.color.orange));
            etDes.setSelection(100);//EditView设置光标到最后
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
