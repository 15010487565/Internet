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

public class CashInfoRmbActivity extends SimpleTopbarActivity {

    private TextView tvCashRmbNum;
    private TextView tvCashRmbMomey;
    private EditText etCashRmbMomey;
    private TextView tvCashRmb;
    String sign;

    @Override
    protected Object getTopbarTitle() {
        return "人民币提现";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_info_rmb);
        sign = BaseApplication.getInstance().getSign();
//        Map<String, String> params = new HashMap<>();
//        params.put("coin", "usdt");
//        params.put("sign", sign);
//        okHttpPostBody(100, GlobalParam.CASH, params);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //可兑换数量
        tvCashRmbNum = findViewById(R.id.tv_CashRmbNum);
        tvCashRmbNum.setText(BaseApplication.getInstance().getUsdt());
        //可兑换美金
        tvCashRmbMomey = findViewById(R.id.tv_CashRmbMomey);
        //
        etCashRmbMomey = findViewById(R.id.et_CashRmbMomey);
        tvCashRmb = findViewById(R.id.tv_CashRmb);
        tvCashRmb.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.tv_CashRmb:
                BaseApplication instance = BaseApplication.getInstance();
                String passwordPay = instance.getPasswordPay();
                if (TextUtils.isEmpty(passwordPay)){
                    ToastUtil.showToast("请先设置支付密码！");
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
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
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
                    params.put("coin", "rmb");
                    params.put("sign", sign);
                    params.put("code", code );
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
