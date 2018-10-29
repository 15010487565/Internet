package com.xcd.www.internet.activity;

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
import com.xcd.www.internet.application.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class AuthorImageActivity extends PhotoActivity {

    private ImageView ivUploadCard;
    private TextView tvAuthorUpload;

    @Override
    protected Object getTopbarTitle() {
        return "实名认证";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_image);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();

        ivUploadCard = findViewById(R.id.iv_UploadCard);
        ivUploadCard.setOnClickListener(this);

        //上传
        tvAuthorUpload =findViewById(R.id.tv_AuthorUpload);
        tvAuthorUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_UploadCard:
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

            case R.id.tv_AuthorUpload://上传
                if (TextUtils.isEmpty(imageurl)){
                    ToastUtil.showToast("请上传手持身份证照！");
                    return;
                }
                String imageuRL = "http://www.quantusd.com/group/130E4BD9ACD767F4639CFC375120C7EB";
                String sign = BaseApplication.getInstance().getSign();
                Map<String, String> params = new HashMap<>();
                params.put("p1",imageuRL);
                params.put("p2", imageuRL);
                params.put("p3",imageuRL);
                params.put("sign", sign);
                okHttpPostBody(101, GlobalParam.UPLOADIMAGE, params);
                break;
        }
    }
    String imageurl;
    @Override
    public void uploadImage(List<File> list) {
        super.uploadImage(list);
        Map<String, String> params = new HashMap<>();
        try {
            for (File imagepath : list) {
                imageurl = imagepath.getPath();
                Log.e("TAG_认证上传","imageurl="+imageurl);

                        Glide.with(this)
                                .load(imageurl)
                                .fitCenter()
                                .dontAnimate()
                                .placeholder(R.mipmap.author_idcard_1)
                                .error(R.mipmap.author_idcard_1)
                                .into(ivUploadCard);
//                        params.put("type", "dollar");//（jpg，png）RSA加密
//                        params.put("data", data );//base64加密数据
//                        params.put("date ", date  );//系统时间戳 rsa加密
//                        params.put("sign ", sign  );//rsa加密sign
//                        okHttpPostBody(100, GlobalParam.UPLOADIMAGE, params);

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
            case 101:
                if (returnCode == 200){
                    finish();
                }
                ToastUtil.showToast(returnMsg);
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
