package com.xcd.www.internet.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.MainActivity;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.model.LoginInfoModel;
import com.xcd.www.internet.util.CommonHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Created by gs on 2018/10/16.
 */

public class LoginPasswordFragment extends SimpleTopbarFragment {

    private EditText etLoginPhone, etLoginPassword;
    private ImageView ivPswVisibleType;
    private TextView tvLoginPassword;
    private LinearLayout llPswVisibleType;
    private boolean isVisiblePws = false;//密码显示状态

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loginpassword;
    }


    @Override
    protected void initView(LayoutInflater inflater, View view) {
        RelativeLayout topbatparent = view.findViewById(R.id.topbat_parent);
        topbatparent.setVisibility(View.GONE);
        //手机号
        etLoginPhone = view.findViewById(R.id.et_LoginPhone);
        //密码
        etLoginPassword = view.findViewById(R.id.et_LoginPassword);
        ivPswVisibleType = view.findViewById(R.id.iv_PswVisibleType);
        llPswVisibleType = view.findViewById(R.id.ll_PswVisibleType);
        llPswVisibleType.setOnClickListener(this);
        //登录
        tvLoginPassword = view.findViewById(R.id.tv_LoginPassword);
        tvLoginPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_PswVisibleType:
                setEtLoginPasswordVisible();
                break;
            case R.id.tv_LoginPassword:
                //手机号
                String phone = etLoginPhone.getText().toString().trim();
                phone = "17600368411";
                if (!CommonHelper.with().checkPhone(phone)) {
                    ToastUtil.showToast("请输入正确手机号！");
                    return;
                }
                String password = etLoginPassword.getText().toString().trim();
                password = "gong1234";
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast("密码不能为空！");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("account", phone);
                map.put("password", password);
                map.put("country", "86");
                okHttpPostBody(100, GlobalParam.LOGIN, map);
                break;
        }
    }

    private void setEtLoginPasswordVisible() {
        if (!isVisiblePws) {
            ivPswVisibleType.setBackgroundResource(R.mipmap.pwdvisible);
            etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isVisiblePws = true;
        } else {
            ivPswVisibleType.setBackgroundResource(R.mipmap.pwdgone);
            isVisiblePws = false;
            etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etLoginPhone.clearFocus();
        etLoginPassword.clearFocus();
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


}
