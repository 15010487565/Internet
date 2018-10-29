package com.xcd.www.internet.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xcd.www.internet.R;
import com.xcd.www.internet.util.ReadImgToBinary;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class AuthorImageNextActivity extends PhotoActivity {

    private ImageView ivUploadCard1, ivUploadCard2;
    private String ivUploadCardA;//身份证正面
    private String ivUploadCardB;//身份证反面
    private TextView tvAuthorUploadNext;

    @Override
    protected Object getTopbarTitle() {
        return "实名认证";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_imagenext);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //身份证正面
        ivUploadCard1 = findViewById(R.id.iv_UploadCard1);
        ivUploadCard1.setOnClickListener(this);
        //身份证反面
        ivUploadCard2 =findViewById(R.id.iv_UploadCard2);
        ivUploadCard2.setOnClickListener(this);
        //上传
        tvAuthorUploadNext =findViewById(R.id.tv_AuthorUploadNext);
        tvAuthorUploadNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_UploadCard1:
                if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                    // 请求权限
                    PermissionsActivity.startActivityForResult(this,11000,AUTHORIMAGE);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
                } else {
                    // 全部权限都已获取
                    setShowViewid(R.id.iv_UploadCard1);
                    getChoiceDialog().show();
                }
                break;
            case R.id.iv_UploadCard2:
                if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                    // 请求权限
                    PermissionsActivity.startActivityForResult(this,11000,AUTHORIMAGE);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
                } else {
                    // 全部权限都已获取
                    setShowViewid(R.id.iv_UploadCard2);
                    getChoiceDialog().show();
                }
                break;
            case R.id.tv_AuthorUploadNext://上传
                if (TextUtils.isEmpty(ivUploadCardA)){
                    ToastUtil.showToast("请上传身份证正面照！");
                    return;
                }
                if (TextUtils.isEmpty(ivUploadCardB)){
                    ToastUtil.showToast("请上传身份证背面照！");
                    return;
                }
                Intent intent = new Intent(this,AuthorImageActivity.class);
                startActivity(intent);
                finish();
//                Map<String, String> params = new HashMap<>();
//                params.put("coin", "dollar");
//                params.put("sign", sign);
//                params.put("password", message );
//                okHttpPostBody(100, GlobalParam.UPLOADIMAGENNEXT, params);
                break;
        }
    }
    @Override
    public void uploadImage(List<File> list) {
        super.uploadImage(list);
        Map<String, String> params = new HashMap<>();
        int showViewid = getShowViewid();
        try {
            for (File imagepath : list) {
                final String imageurl = imagepath.getPath();
                Log.e("TAG_认证上传","imageurl="+imageurl);
                switch (showViewid) {
                    case R.id.iv_UploadCard1://正面
                        Glide.with(this)
                                .load(imageurl)
                                .fitCenter()
                                .dontAnimate()
                                .placeholder(R.mipmap.author_idcard_1)
                                .error(R.mipmap.author_idcard_1)
                                .into(ivUploadCard1);
                        if (imageurl != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //将图片转化为字符串
                                    ivUploadCardA = ReadImgToBinary.imgToBase64(imageurl);
                                }
                            });
                        }
//                        params.put("type", "dollar");//（jpg，png）RSA加密
//                        params.put("data", data );//base64加密数据
//                        params.put("date ", date  );//系统时间戳 rsa加密
//                        params.put("sign ", sign  );//rsa加密sign
//                        okHttpPostBody(100, GlobalParam.UPLOADIMAGE, params);
                        break;
                    case R.id.iv_UploadCard2://背面

                        Glide.with(this)
                                .load(imageurl)
                                .fitCenter()
                                .dontAnimate()
                                .placeholder(R.mipmap.author_idcard_2)
                                .error(R.mipmap.author_idcard_2)
                                .into(ivUploadCard2);
                        if (imageurl != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //将图片转化为字符串
                                    ivUploadCardB = ReadImgToBinary.imgToBase64(imageurl);
                                }
                            });
                        }
//                        params.put("type", "dollar");//（jpg，png）RSA加密
//                        params.put("data", data);//base64加密数据
//                        params.put("date ", date  );//系统时间戳 rsa加密
//                        params.put("sign ", sign  );//rsa加密sign
//                        okHttpPostBody(100, GlobalParam.UPLOADIMAGE, params);

                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        super.onSuccessResult(requestCode,returnCode,returnMsg,returnData,paramsMaps);
        switch (requestCode) {
            case 100:

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 11000:
                if (hasAllPermissionsGranted(grantResults)){
                    getChoiceDialog().show();
                }else {
                    ToastUtil.showToast("请在应用管理中打开“相机”访问权限！");
                }
                break;
        }
    }
    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
