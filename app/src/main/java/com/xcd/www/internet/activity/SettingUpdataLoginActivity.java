package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.CodeCheckModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class SettingUpdataLoginActivity extends SimpleTopbarActivity {

    private TextView tvPaypasswordOk;
    private EditText etOldPaypassword, etPaypassword1, etPaypassword2;
    String payCode;

    @Override
    protected Object getTopbarTitle() {
        return "修改登录密码";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_updata_login);
        payCode = getIntent().getStringExtra("payCode");
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        etOldPaypassword = findViewById(R.id.et_OldPaypassword);

        etPaypassword1 = findViewById(R.id.et_Paypassword1);
        etPaypassword2 = findViewById(R.id.et_Paypassword2);

        tvPaypasswordOk = findViewById(R.id.tv_PaypasswordOk);
        tvPaypasswordOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_PaypasswordOk:
//                String oldpwd = etOldPaypassword.getText().toString().trim();
                String trim = etPaypassword1.getText().toString().trim();
                String trim2 = etPaypassword2.getText().toString().trim();
//                if (TextUtils.isEmpty(oldpwd)){
//                    ToastUtil.showToast("原支付密码不能为空");
//                    return;
//                }

                if (TextUtils.isEmpty(trim)){
                    ToastUtil.showToast("新支付密码不能为空");
                    return;
                }
                if (trim.length()<6){
                    ToastUtil.showToast("密码长度不能小于6位！");
                    return;
                }

                if (!trim.equals(trim2)){
                    ToastUtil.showToast("您输入的两次新密码不相同");
                    return;
                }
                String account = BaseApplication.getInstance().getAccount();
                String sign = BaseApplication.getInstance().getSign();
                String country = BaseApplication.getInstance().getCountry();
                Map<String, String> mapCode = new HashMap<>();
                mapCode.put("country", country );
                mapCode.put("sign", sign);
                mapCode.put("account", account);
                mapCode.put("code", payCode );
                okHttpPostBody(101, GlobalParam.CHECKCODE, mapCode);

                break;

        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

        switch (requestCode){
            case 100:

                if (returnCode == 200) {
                    ToastUtil.showToast(returnMsg);
//                    startActivity(new Intent(SettingUpdataLoginActivity.this,LoginActivity.class));
                    finish();
                }else{
                    ToastUtil.showToast(returnMsg);
                }
                break;
            case 101:
                if (returnCode == 200) {

                    CodeCheckModel codeCheckModel = JSON.parseObject(returnData, CodeCheckModel.class);
                    String code = codeCheckModel.getData().getSign();
                    String trim = etPaypassword1.getText().toString().trim();
                    String sign = BaseApplication.getInstance().getSign();

                    Map<String, String> params = new HashMap<>();

                    params.put("password", trim );
//                    params.put("newPassword ", trim );
                    params.put("sign", sign);
                    params.put("code", code );
                    okHttpPostBody(100, GlobalParam.RESETPWD, params);
                }else{
                    ToastUtil.showToast(returnMsg);
                }

                break;
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