package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.view.CircleImageView;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class FriendInfoActivity extends SimpleTopbarActivity {

    private CircleImageView ivChatInfoTopHead;
    private TextView tvChatInfoName, tvChatInfoPhone;

    @Override
    protected Object getTopbarTitle() {
        return "个人信息";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        Intent intent = getIntent();
        String chatInfoHead = intent.getStringExtra("ChatInfoHead");
        Glide.with(this)
                .load(chatInfoHead)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.launcher_login)
                .error(R.mipmap.launcher_login)
                .into( ivChatInfoTopHead);
        String chatInfoName = intent.getStringExtra("ChatInfoName");
        tvChatInfoName.setText(chatInfoName);
        String chatInfoPhone = intent.getStringExtra("ChatInfoPhone");
        tvChatInfoPhone.setText(chatInfoPhone);
    }
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();

        //头像
        ivChatInfoTopHead = findViewById(R.id.iv_ChatInfoTopHead);

        //名称
        tvChatInfoName = findViewById(R.id.tv_ChatInfoName);

        tvChatInfoPhone = findViewById(R.id.tv_ChatInfoPhone);
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
}
