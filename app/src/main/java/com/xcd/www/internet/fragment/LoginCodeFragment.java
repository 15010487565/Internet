package com.xcd.www.internet.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.MainActivity;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.common.Config;
import com.xcd.www.internet.model.LoginInfoModel;
import com.xcd.www.internet.util.CommonHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.help.HelpUtils;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Created by gs on 2018/10/16.
 */

public class LoginCodeFragment extends SimpleTopbarFragment {

    private EditText etLoginPhone, etLoginCode;
    private TextView tvLoginGetCode, tvLoginCode;
    private int recLen = Config.CODETIME;//验证码倒计时
    Thread thread;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_logincode;
    }


    @Override
    protected void initView(LayoutInflater inflater, View view) {
        RelativeLayout topbatparent = view.findViewById(R.id.topbat_parent);
        topbatparent.setVisibility(View.GONE);
        //手机号
        etLoginPhone = view.findViewById(R.id.et_LoginPhone);
        //验证码
        etLoginCode = view.findViewById(R.id.et_LoginCode);
        //获取验证码
        tvLoginGetCode = view.findViewById(R.id.tv_LoginGetCode);
        tvLoginGetCode.setOnClickListener(this);
        //登录
        tvLoginCode = view.findViewById(R.id.tv_LoginCode);
        tvLoginCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_LoginGetCode://获取验证码
                thread = new Thread(networkTask);
                thread.start();
                break;
            case R.id.tv_LoginCode:
                //手机号
                etLoginPhone.setText("15010487565");
                String phone = etLoginPhone.getText().toString().trim();
                if (!CommonHelper.with().checkPhone(phone)) {
                    ToastUtil.showToast("请输入正确手机号！");
                    return;
                }
                String code = etLoginCode.getText().toString().trim();
//                code = "111111";
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast("验证码不能为空！");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("account", phone);
                map.put("code", code);
                map.put("country", "86");
                okHttpPostBody(100, GlobalParam.LOGIN, map);
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100://验证码登录
                    LoginInfoModel loginIndoModel = JSON.parseObject(returnData, LoginInfoModel.class);
                    LoginInfoModel.DataBean data = loginIndoModel.getData();
                    String sign = data.getSign();
                    String account = data.getAccount();
                    long id = data.getId();
                    BaseApplication.getInstance().setAccount(account);
                    BaseApplication.getInstance().setSign(sign);
                    BaseApplication.getInstance().setId(id);
                    //头像
                    String headportrait = data.getHeadportrait();
                    BaseApplication.getInstance().setHeadportrait(headportrait);
                    //区号
                    String country = data.getCountry();
                    BaseApplication.getInstance().setCountry(country);

                    String token = data.getToken();
                    connect(token);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    break;
                case 101://获取验证码
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
                    //手机号
                    String phone = etLoginPhone.getText().toString().trim();
                    String timeStr = (String) msg.obj;
                    Map<String, String> mapCode = new HashMap<>();
                    mapCode.put("account", phone);
                    mapCode.put("country ", "86");
                    mapCode.put("date ", timeStr);
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
                tvLoginGetCode.setText(recLen + "s重新发送");
//                } else {
//                    tvLoginGetCode.setText(recLen + "s重新发送");
//                }
                tvLoginGetCode.setBackgroundResource(R.drawable.shape_black_aa);
                tvLoginGetCode.setEnabled(false);
                handler.postDelayed(this, 1000);
            } else {
                tvLoginGetCode.setText("获取验证码");
                tvLoginGetCode.setEnabled(true);
                tvLoginGetCode.setBackgroundResource(R.drawable.shape_blue);
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
