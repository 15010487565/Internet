package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.PasswordVerifyModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class CashInfoOKDActivity extends SimpleTopbarActivity implements TextWatcher{

    private EditText tvCashOkdBuyNum;
    private TextView tvCashOkdGiveNum;
    private TextView tvCashOkd;
    String sign;
    @Override
    protected Object getTopbarTitle() {
        return "购买okd送lit";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_info_okd);
        sign = BaseApplication.getInstance().getSign();
//        Map<String, String> params = new HashMap<>();
//        params.put("coin", "usdt");
//        params.put("sign", sign);
//        okHttpPostBody(100, GlobalParam.CASH, params);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //可购买数量
        tvCashOkdBuyNum = findViewById(R.id.tv_CashOkdBuyNum);
        tvCashOkdBuyNum.addTextChangedListener(this);
        //可赠送美金
        tvCashOkdGiveNum = findViewById(R.id.tv_CashOkdGiveNum);

        tvCashOkd = findViewById(R.id.tv_CashOkd);
        tvCashOkd.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.tv_CashOkd:

                String trim = tvCashOkdBuyNum.getText().toString().trim();
                try {
                    if (TextUtils.isEmpty(trim)&&Double.valueOf(trim)>0){
                        ToastUtil.showToast("购买数量必须大于0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtil.showToast("请输入正确的购煤数量!");
                    return;
                }
                DialogUtil.getInstance()
                        .setContext(this)
                        .setCancelable(true)
                        .title("温馨提示")
                        .hint("请输入支付密码")
                        .sureText("确定")
                        .cancelText("取消")
                        .setSureOnClickListener(new DialogUtil.OnClickListener() {
                            @Override
                            public void onClick(View view, String message) {
                                Map<String, String> map = new HashMap<>();
                                map.put("password", message );
                                map.put("sign", sign);
                                okHttpPostBody(101, GlobalParam.VERIFYPASSWORD, map);
                            }
                        }).showEditDialog();


                break;
        }
    }
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
                case 100:
                    ToastUtil.showToast(returnMsg);
                    finish();
                    break;
                case 101:
                    String trim = tvCashOkdBuyNum.getText().toString().trim();

                    PasswordVerifyModel passwordVerifyModel = JSON.parseObject(returnData, PasswordVerifyModel.class);
                    PasswordVerifyModel.DataBean data = passwordVerifyModel.getData();
                    String code = data.getSign();

                    Map<String, String> params = new HashMap<>();
                    params.put("money", "okd");
                    params.put("sign", sign);
                    params.put("code", code );
                    params.put("money", trim );
                    okHttpPostBody(100, GlobalParam.CASHOKD, params);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String trim = tvCashOkdBuyNum.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)){
            tvCashOkdGiveNum.setText(trim);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
