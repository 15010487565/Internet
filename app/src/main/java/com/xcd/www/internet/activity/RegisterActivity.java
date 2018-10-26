package com.xcd.www.internet.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.common.Config;
import com.xcd.www.internet.model.LoginInfoModel;
import com.xcd.www.internet.util.CommonHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.help.HelpUtils;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class RegisterActivity extends BaseInternetActivity implements TextWatcher {

    private EditText etRegisterPhone, etRegisterPassword, etRegisterCode;
    private ImageView ivPswVisibleType;
    private LinearLayout llPswVisibleType, llBack;
    private ImageView ivRegisterCheck;
    private TextView tvRegisterCheck, tvRegisterCheckAgreement, tvRegister;
    private boolean isSelectAgreement = false;
    private boolean isVisiblePws = false;//密码显示状态
    private TextView tvRegisterGetCode;
    private int recLen = Config.CODETIME;//验证码倒计时
    Thread thread;

    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //手机号
        etRegisterPhone = findViewById(R.id.et_RegisterPhone);
        etRegisterPhone.addTextChangedListener(this);
        //密码
        etRegisterPassword = findViewById(R.id.et_RegisterPassword);
        etRegisterPassword.addTextChangedListener(this);
        ivPswVisibleType = findViewById(R.id.iv_PswVisibleType);
        llPswVisibleType = findViewById(R.id.ll_PswVisibleType);
        llPswVisibleType.setOnClickListener(this);
        //验证码
        etRegisterCode = findViewById(R.id.et_RegisterCode);
        etRegisterCode.addTextChangedListener(this);
        //获取验证码
        tvRegisterGetCode = findViewById(R.id.tv_RegisterGetCode);
        tvRegisterGetCode.setOnClickListener(this);
        //协议
        ivRegisterCheck = findViewById(R.id.iv_RegisterCheck);
        ivRegisterCheck.setOnClickListener(this);
        tvRegisterCheck = findViewById(R.id.tv_RegisterCheck);
        tvRegisterCheck.setOnClickListener(this);
        tvRegisterCheckAgreement = findViewById(R.id.tv_RegisterCheckAgreement);
        tvRegisterCheckAgreement.setOnClickListener(this);
        tvRegisterCheckAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvRegisterCheckAgreement.getPaint().setAntiAlias(true);//抗锯齿
        //注册
        tvRegister = findViewById(R.id.tv_Register);
        tvRegister.setOnClickListener(this);
        tvRegister.setEnabled(false);
        tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
        //返回
        llBack = findViewById(R.id.ll_Back);
        llBack.setOnClickListener(this);
    }

    //注册手机号
    String phone;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_PswVisibleType://密码显示隐藏
                setEtLoginPasswordVisible();
                break;
            case R.id.iv_RegisterCheck://选中协议
                SettingAgreement();
                break;
            case R.id.tv_RegisterCheck://选中协议
                SettingAgreement();
                break;
            case R.id.tv_RegisterCheckAgreement://查看协议
                ToastUtil.showToast("查看协议");
                break;
            case R.id.tv_RegisterGetCode://获取验证码
                thread = new Thread(networkTask);
                thread.start();
                break;
            case R.id.tv_Register://立即注册
                //手机号
                phone = etRegisterPhone.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                String code = etRegisterCode.getText().toString().trim();

                if (!CommonHelper.with().checkPhone(phone)) {
                    ToastUtil.showToast("请输入正确手机号！");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    ToastUtil.showToast("请输入密码！");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast("请输入验证码！");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("account", phone);
                map.put("password", password);
                map.put("country", "86");
                map.put("code", code);
                okHttpPostBody(100, GlobalParam.REGISTER, map);
                break;
            case R.id.ll_Back:
                finish();
                break;
        }
    }

    private void SettingAgreement() {
        Log.e("TAG_协议", "isSelectAgreement=" + isSelectAgreement);
        if (!isSelectAgreement) {
            ivRegisterCheck.setImageResource(R.mipmap.checkbox_select);
            isSelectAgreement = true;
        } else {
            ivRegisterCheck.setImageResource(R.mipmap.checkbox_un);
            isSelectAgreement = false;
        }
        //手机号
        String phone = etRegisterPhone.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String code = etRegisterCode.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            if (!TextUtils.isEmpty(password) && password.length() >= 6) {
                if (!TextUtils.isEmpty(code) && isSelectAgreement) {
                    tvRegister.setEnabled(true);
                    tvRegister.setBackgroundResource(R.drawable.shape_gradient);
                } else {
                    tvRegister.setEnabled(false);
                    tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
                }
            } else {
                tvRegister.setEnabled(false);
                tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
            }
        } else {
            tvRegister.setEnabled(false);
            tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
        }
    }

    private void setEtLoginPasswordVisible() {
        if (!isVisiblePws) {
            ivPswVisibleType.setBackgroundResource(R.mipmap.pwdvisible);
            etRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVisiblePws = true;
        } else {
            ivPswVisibleType.setBackgroundResource(R.mipmap.pwdgone);
            isVisiblePws = false;
            etRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etRegisterPhone.clearFocus();
        etRegisterPassword.clearFocus();
        etRegisterCode.clearFocus();
    }


    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100://注册
                    LoginInfoModel loginIndoModel = JSON.parseObject(returnData, LoginInfoModel.class);
                    LoginInfoModel.DataBean data = loginIndoModel.getData();
                    String sign = data.getSign();
                    String account = data.getAccount();
                    long id = data.getId();
                    BaseApplication.getInstance().setAccount(account);
                    BaseApplication.getInstance().setSign(sign);
                    BaseApplication.getInstance().setId(id);
                    String token = data.getToken();
                    connect(token);
                    Intent intent = new Intent(this, MainActivity.class);
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
                    String phone = etRegisterPhone.getText().toString().trim();
                    String timeStr = (String) msg.obj;
                    Map<String, String> mapCode = new HashMap<>();
                    mapCode.put("account", phone);
                    mapCode.put("country", "86");
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
                tvRegisterGetCode.setText(recLen + "s重新发送");
//                } else {
//                    tvLoginGetCode.setText(recLen + "s重新发送");
//                }
                tvRegisterGetCode.setBackgroundResource(R.drawable.shape_black_aa);
                tvRegisterGetCode.setEnabled(false);
                handler.postDelayed(this, 1000);
            } else {
                tvRegisterGetCode.setText("获取验证码");
                tvRegisterGetCode.setEnabled(true);
                tvRegisterGetCode.setBackgroundResource(R.drawable.shape_blue);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //手机号
        String phone = etRegisterPhone.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String code = etRegisterCode.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            if (!TextUtils.isEmpty(password) && password.length() >= 6) {
                if (!TextUtils.isEmpty(code) && isSelectAgreement) {
                    tvRegister.setEnabled(true);
                    tvRegister.setBackgroundResource(R.drawable.shape_gradient);
                } else {
                    tvRegister.setEnabled(false);
                    tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
                }
            } else {
                tvRegister.setEnabled(false);
                tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
            }
        } else {
            tvRegister.setEnabled(false);
            tvRegister.setBackgroundResource(R.drawable.shape_black_aa);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
