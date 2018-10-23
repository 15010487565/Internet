package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.common.Config;
import com.xcd.www.internet.util.CommonHelper;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class UpdataPwdActivity extends SimpleTopbarActivity {

    private EditText etUpdataPwdPhone, etUpdataPwd, etUpdataPwdCode;
    private ImageView ivPswVisibleType;
    private LinearLayout llPswVisibleType, llBack;
    private TextView tvUpdataPwdOk;
    private boolean isVisiblePws = false;//密码显示状态
    private TextView tvUpdataPwdGetCode;
    private int recLen = Config.CODETIME;//验证码倒计时
    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_pwd);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //手机号
        etUpdataPwdPhone = findViewById(R.id.et_UpdataPwdPhone);
        //密码
        etUpdataPwd = findViewById(R.id.et_UpdataPwd);
        ivPswVisibleType = findViewById(R.id.iv_PswVisibleType);
        llPswVisibleType = findViewById(R.id.ll_PswVisibleType);
        llPswVisibleType.setOnClickListener(this);
        //验证码
        etUpdataPwdCode = findViewById(R.id.et_UpdataPwdCode);
        //获取验证码
        tvUpdataPwdGetCode = findViewById(R.id.tv_UpdataPwdGetCode);
        tvUpdataPwdGetCode.setOnClickListener(this);
        //确定
        tvUpdataPwdOk = findViewById(R.id.tv_UpdataPwdOk);
        tvUpdataPwdOk.setOnClickListener(this);
        //返回
        llBack = findViewById(R.id.ll_Back);
        llBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_PswVisibleType://密码显示隐藏
                setEtLoginPasswordVisible();
                break;
            case R.id.tv_UpdataPwdGetCode://获取验证码
                handler.postDelayed(runnable, 1000);
                ToastUtil.showToast("获取验证码");
                break;
            case R.id.tv_UpdataPwdOk://立即注册
                //手机号
                String phone = etUpdataPwdPhone.getText().toString().trim();
                String password = etUpdataPwd.getText().toString().trim();
                String code = etUpdataPwdCode.getText().toString().trim();

                if (!CommonHelper.with().checkPhone(phone)) {
                    ToastUtil.showToast("请输入正确手机号！");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    ToastUtil.showToast("密码不能为空且长度在6-18位！");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast("验证码不能为空！");
                    return;
                }
                ToastUtil.showToast("修改密码");
                break;
            case R.id.ll_Back:
                finish();
                break;
        }
    }

    private void setEtLoginPasswordVisible() {
        if (!isVisiblePws) {
            ivPswVisibleType.setBackgroundResource(R.mipmap.pwdvisible);
            etUpdataPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVisiblePws = true;
        } else {
            ivPswVisibleType.setBackgroundResource(R.mipmap.pwdgone);
            isVisiblePws = false;
            etUpdataPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etUpdataPwdPhone.clearFocus();
        etUpdataPwd.clearFocus();
        etUpdataPwdCode.clearFocus();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            if (recLen > 0) {
//                if (recLen >= 10) {
                tvUpdataPwdGetCode.setText(recLen + "s重新发送");
//                } else {
//                    tvLoginGetCode.setText(recLen + "s重新发送");
//                }
                tvUpdataPwdGetCode.setBackgroundResource(R.drawable.shape_black_aa);
                tvUpdataPwdGetCode.setEnabled(false);
                handler.postDelayed(this, 1000);
            } else {
                tvUpdataPwdGetCode.setText("获取验证码");
                tvUpdataPwdGetCode.setEnabled(true);
                tvUpdataPwdGetCode.setBackgroundResource(R.drawable.shape_blue);
                recLen = Config.CODETIME;
            }
        }
    };

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
}
