package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.PasswordVerifyModel;
import com.xcd.www.internet.util.EventBusMsg;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class CashInfoDollarActivity extends SimpleTopbarActivity {

    private TextView tvCashDollarNum;
    private TextView tvCashDollarMomey;
    private EditText etCashDollarMomey;
    private TextView tvCashDollar;
    String sign;

    @Override
    protected Object getTopbarTitle() {
        return "美金提现";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_info_dollar);
        sign = BaseApplication.getInstance().getSign();

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //可兑换数量
        tvCashDollarNum = findViewById(R.id.tv_CashDollarNum);
        String usdt = BaseApplication.getInstance().getUsdt();
        tvCashDollarNum.setText(usdt);
        //可兑换美金
        tvCashDollarMomey = findViewById(R.id.tv_CashDollarMomey);
        //
        etCashDollarMomey = findViewById(R.id.et_CashDollarMomey);
        tvCashDollar = findViewById(R.id.tv_CashDollar);
        tvCashDollar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.tv_CashDollar:
                BaseApplication instance = BaseApplication.getInstance();
                String passwordPay = instance.getPasswordPay();
                if (TextUtils.isEmpty(passwordPay)){
                    ToastUtil.showToast("请先设置支付密码！");
                    return;
                }
                DialogUtil.getInstance()
                        .setContext(this)
                        .setCancelable(true)
                        .title("温馨提示")
                        .hint("请输入支付密码")
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .sureText("确定")
                        .cancelText("取消")
                        .setSureOnClickListener(new DialogUtil.OnClickListener() {
                            @Override
                            public void onClick(View view, String message) {
                                Map<String, String> map = new HashMap<>();
                                map.put("password", message);
                                map.put("sign", sign);
                                okHttpPostBody(101, GlobalParam.VERIFYPASSWORD, map);

                            }
                        })
                        .setCancelOnClickListener(new DialogUtil.OnClickListener() {

                            @Override
                            public void onClick(View view, String message) {

                            }
                        }).showEditDialog();


                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    EventBusMsg msg = new EventBusMsg("RefreshBag");
                    EventBus.getDefault().post(msg);
                    ToastUtil.showToast(returnMsg);
                    finish();
                    break;
                case 101:
                    PasswordVerifyModel passwordVerifyModel = JSON.parseObject(returnData, PasswordVerifyModel.class);
                    PasswordVerifyModel.DataBean data = passwordVerifyModel.getData();
                    String code = data.getSign();
                    Map<String, String> params = new HashMap<>();
                    params.put("coin", "dollar");
                    params.put("sign", sign);
                    params.put("code", code);
                    okHttpPostBody(100, GlobalParam.CASH, params);
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
