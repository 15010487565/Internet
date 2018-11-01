package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
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

public class CashInfoUSDTActivity extends SimpleTopbarActivity {

    private LinearLayout llCashUsdtAddress;
    private TextView tvCashUsdtAddress;
    private TextView tv_CashUsdt;
    private TextView tvCashUsdtNum, tvCashUsdtConversion;
    String sign;
    String cashUsdtAddress;//地址
    @Override
    protected Object getTopbarTitle() {
        return "USDT提现";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_info_usdt);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //USDT数量
        tvCashUsdtNum = findViewById(R.id.tv_CashUsdtNum);
        BaseApplication instance = BaseApplication.getInstance();
        String usdt = instance.getUsdt();
        tvCashUsdtNum.setText(usdt);
        //可兑换数量
        tvCashUsdtConversion = findViewById(R.id.tv_CashUsdtConversion);
        tvCashUsdtConversion.setText(String.valueOf(Double.valueOf(usdt)*0.5));

        llCashUsdtAddress = findViewById(R.id.ll_CashUsdtAddress);
        llCashUsdtAddress.setOnClickListener(this);
        tvCashUsdtAddress = findViewById(R.id.tv_CashUsdtAddress);
        tv_CashUsdt = findViewById(R.id.tv_CashUsdt);
        tv_CashUsdt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_CashUsdtAddress:
                Intent intent1 = new Intent(this, CashUSDTAddressActivity.class);
                startActivityForResult(intent1, 10000);
                break;
            case R.id.tv_CashUsdt:
                BaseApplication instance = BaseApplication.getInstance();
                String passwordPay = instance.getPasswordPay();
                if (TextUtils.isEmpty(passwordPay)){
                    ToastUtil.showToast("请先设置支付密码！");
                    return;
                }

                sign = BaseApplication.getInstance().getSign();
                if (TextUtils.isEmpty(cashUsdtAddress)){
                    ToastUtil.showToast("提现地址不能为空");
                    return;
                }
                DialogUtil.getInstance()
                        .setContext(this)
                        .setCancelable(true)
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
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
                        })
                        .setCancelOnClickListener(new DialogUtil.OnClickListener() {

                            @Override
                            public void onClick(View view, String message) {

                            }
                        })
                        .showEditDialog();


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 10000:
                    cashUsdtAddress = data.getStringExtra("CashUsdtAddress");
                    tvCashUsdtAddress.setText(cashUsdtAddress);
                    break;
            }
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
                case 100:
                    EventBusMsg msg = new EventBusMsg("RefreshBag");
                    EventBus.getDefault().post(msg);
                    finish();
                    ToastUtil.showToast(returnMsg);
                    break;
                case 101:
                    if (TextUtils.isEmpty(cashUsdtAddress)){
                        ToastUtil.showToast("提现地址不能为空");
                        return;
                    }
                    PasswordVerifyModel passwordVerifyModel = JSON.parseObject(returnData, PasswordVerifyModel.class);
                    PasswordVerifyModel.DataBean data = passwordVerifyModel.getData();
                    String code = data.getSign();
                    Map<String, String> params = new HashMap<>();
                    params.put("coin", "usdt");
                    params.put("sign", sign);
                    params.put("code", code );
                    params.put("url", cashUsdtAddress);//  "url": 币种为usdt时的提现地址
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
