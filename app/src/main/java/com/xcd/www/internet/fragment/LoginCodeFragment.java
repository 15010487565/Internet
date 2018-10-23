package com.xcd.www.internet.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.MainActivity;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.common.Config;
import com.xcd.www.internet.util.CommonHelper;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Created by gs on 2018/10/16.
 */

public class LoginCodeFragment extends SimpleTopbarFragment {

    private EditText etLoginPhone, etLoginCode;
    private TextView tvLoginGetCode, tvLoginCode;
    private int recLen = Config.CODETIME;//验证码倒计时
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_logincode;
    }


    @Override
    protected void initView(LayoutInflater inflater, View view) {
        RelativeLayout topbatparent =  view.findViewById(R.id.topbat_parent);
        topbatparent.setVisibility(View.GONE);
        //手机号
        etLoginPhone = view.findViewById(R.id.et_LoginPhone);
        //验证码
        etLoginCode =  view.findViewById(R.id.et_LoginCode);
        //获取验证码
        tvLoginGetCode =  view.findViewById(R.id.tv_LoginGetCode);
        tvLoginGetCode.setOnClickListener(this);
        //登录
        tvLoginCode = view.findViewById(R.id.tv_LoginCode);
        tvLoginCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_LoginGetCode://获取验证码
                handler.postDelayed(runnable, 1000);
                ToastUtil.showToast("获取验证码");
                break;
            case R.id.tv_LoginCode:
                //手机号
                etLoginPhone.setText("15010487565");
                String phone = etLoginPhone.getText().toString().trim();
                if (!CommonHelper.with().checkPhone(phone)) {
                    ToastUtil.showToast("请输入正确手机号！");
                    return;
                }

                startActivity(new Intent(getActivity(), MainActivity.class));
                ToastUtil.showToast("验证码登录");
                break;
        }
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

    Handler handler = new Handler();
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
}
