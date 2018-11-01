package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.common.Config;
import com.xcd.www.internet.func.SettingUpdataPayNextTopBtnFunc;
import com.xcd.www.internet.model.CodeModer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.help.HelpUtils;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class SettingUpdataPayCodeActivity extends SimpleTopbarActivity {

    private TextView tvUpdataPayTopHint,tvUpdataPayCode;
    private EditText etUpdataPayCode;
    private int recLen = Config.CODETIME;//验证码倒计时
    Thread thread;
    String account;
    int updataType;//1 修改支付密码;2 修改登录密码
    private static Class<?> rightFuncArray[] = {SettingUpdataPayNextTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "修改支付密码";

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_updata_pay_code);
        Intent intent = getIntent();
        BaseApplication instance = BaseApplication.getInstance();
        String passwordPay = instance.getPasswordPay();
        updataType = intent.getIntExtra("updataType",0);
        if (updataType == 1){//修改支付密码
            if (TextUtils.isEmpty(passwordPay)){
                resetTopbarTitle("设置支付密码");
            }else {
                resetTopbarTitle("修改支付密码");
            }
        }else {//修改登录密码
            resetTopbarTitle("修改登录密码");
        }
        account = BaseApplication.getInstance().getAccount();
        String s = tvUpdataPayTopHint.getText().toString();
        Log.e("TAG_密码","S="+s);
        Log.e("TAG_密码","account="+account);
        String topHintStr = String.format(s,  account.substring(account.length()-4,account.length()));
        SpannableStringBuilder span = new SpannableStringBuilder(topHintStr);
        span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.blue)), 9, 13,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvUpdataPayTopHint.setText(span);
        thread = new Thread(networkTask);
        thread.start();
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        tvUpdataPayTopHint = findViewById(R.id.tv_UpdataPayTopHint);

        tvUpdataPayCode = findViewById(R.id.tv_UpdataPayCode);
        tvUpdataPayCode.setOnClickListener(this);
        etUpdataPayCode = findViewById(R.id.et_UpdataPayCode);
    }
    public void updataPay(){
        String trim = etUpdataPayCode.getText().toString().trim();
        if (TextUtils.isEmpty(trim)){
            ToastUtil.showToast("验证码不能为空！");
            return;
        }
        Intent intent = new Intent(this,SettingUpdataPayActivity.class);
        if (updataType == 1){//修改支付密码
            intent.setClass(this,SettingUpdataPayActivity.class);
        }else {//修改登录密码
            intent.setClass(this,SettingUpdataLoginActivity.class);
        }
        intent.putExtra("payCode",trim);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_UpdataPayCode:
                recLen = Config.CODETIME;
                thread = new Thread(networkTask);
                thread.start();
                break;

        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {

                case 101://获取验证码
                    CodeModer codeModer = JSON.parseObject(returnData, CodeModer.class);
                    String code = codeModer.getData();
                    etUpdataPayCode.setText(code);
                    handler.postDelayed(runnable, 1000);
                    break;
            }
        } else {
            ToastUtil.showToast(returnMsg);
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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String country = BaseApplication.getInstance().getCountry();
                    String timeStr = (String) msg.obj;
                    Map<String, String> mapCode = new HashMap<>();
                    mapCode.put("account", account);
                    mapCode.put("country ",country);
                    mapCode.put("date", timeStr);
                    okHttpPostBody(101, GlobalParam.GETCODE, mapCode);

                    break;
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            if (recLen > 0) {
//                if (recLen >= 10) {
                tvUpdataPayCode.setText(recLen + "s重新发送");
//                } else {
//                    tvLoginGetCode.setText(recLen + "s重新发送");
//                }
                tvUpdataPayCode.setTextColor(ContextCompat.getColor(SettingUpdataPayCodeActivity.this,R.color.black_99));
                tvUpdataPayCode.setEnabled(false);
                handler.postDelayed(this, 1000);
            } else {
                tvUpdataPayCode.setText("获取验证码");
                tvUpdataPayCode.setEnabled(true);
                tvUpdataPayCode.setTextColor(ContextCompat.getColor(SettingUpdataPayCodeActivity.this,R.color.blue));
                recLen = Config.CODETIME;
            }
        }
    };
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            long networkTime = HelpUtils.getNetworkTime();
            if (networkTime > 0) {
                String timeStr = String.valueOf(networkTime);
//                if (timeStr.length() == 13){
//                    timeStr = timeStr.substring(0,10);
//                }
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = timeStr;
                handler.sendMessage(message);
            } else {
                ToastUtil.showToast("获取验证码失败，请检查网络！");
            }
        }
    };
}
