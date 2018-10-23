package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import www.xcd.com.mylibrary.utils.SharePrefHelper;

/**
 * Created by chen on 2018/10/16.
 *
 * 引导页或者启动页过后的广告页面  点击跳过或者自动3秒后跳到首页 不缓存图片
 */
public class LaunchActivity extends AppCompatActivity{

    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launch);
        isFirstOpen();

    }
    private void isFirstOpen() {
        isFirstOpen(3000);
    }
    private void isFirstOpen(int skipTime) {
        Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //是否首次登陆
                boolean isFristLogin = SharePrefHelper.getInstance(LaunchActivity.this).getSpBoolean("ISFRISTLOGIN", false);
                Log.e("TAG_isShowState","isFristLogin="+isFristLogin);
                if (!isFristLogin){
                    startActivity(new Intent(LaunchActivity.this,WelcomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, skipTime);
    }
}
