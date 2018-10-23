package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.WelcomeAdapter;
import com.xcd.www.internet.indicator.CirclePageIndicator;

import java.util.ArrayList;

import www.xcd.com.mylibrary.utils.SharePrefHelper;

/**
 * 引导页
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private CirclePageIndicator indicator;

    private WelcomeAdapter adapter;
    private int[] images;
    private boolean isRegister;
    ArrayList<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
    }

    private void initData() {
        adapter = new WelcomeAdapter(this);
        if (imageList == null || imageList.size() == 0) {
            images = new int[3];
            images[0] = R.mipmap.guide_pager1_bg;
            images[1] = R.mipmap.guide_pager2_bg;
            images[2] = R.mipmap.guide_pager3_bg;
            adapter.setList(images);
        } else {
            adapter.setImageUrls(imageList);
        }

        adapter.setListener(this);
        viewPager.setAdapter(adapter);
        indicator.setFillColor(ContextCompat.getColor(this,R.color.white));
        indicator.setViewPager(viewPager);
        isRegister = false;
    }


    @Override
    public void onClick(View view) {
        // 设置结果，并进行传送
        SharePrefHelper.getInstance(this).putSpBoolean("ISFRISTLOGIN", true);
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
