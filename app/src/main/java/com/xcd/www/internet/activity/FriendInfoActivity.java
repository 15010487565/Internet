package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.rong.module.RongFriendsModel;
import com.xcd.www.internet.view.CircleImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class FriendInfoActivity extends SimpleTopbarActivity {

    private CircleImageView ivChatInfoTopHead;
    private TextView tvChatInfoName, tvChatInfoPhone, tvChatInfoStart, tvChatInfoAdd;
    String sign;
    String targetId;
    String chatInfoPhone;

    @Override
    protected Object getTopbarTitle() {
        return "个人信息";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        BaseApplication instance = BaseApplication.getInstance();
        sign = instance.getSign();
        Intent intent = getIntent();
        targetId = String.valueOf(intent.getIntExtra("targetId",0));
        Map<String, String> map = new HashMap<>();
        map.put("id", targetId);
        map.put("sign", sign);
        okHttpPostBody(100, GlobalParam.FRIENDSINFO, map);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();

        //头像
        ivChatInfoTopHead = findViewById(R.id.iv_ChatInfoTopHead);
        //名称
        tvChatInfoName = findViewById(R.id.tv_ChatInfoName);
        //手机号
        tvChatInfoPhone = findViewById(R.id.tv_ChatInfoPhone);
        //开始聊天
        tvChatInfoStart = findViewById(R.id.tv_ChatInfoStart);
        tvChatInfoStart.setOnClickListener(this);
        tvChatInfoStart.setVisibility(View.GONE);
        //添加好友
        tvChatInfoAdd = findViewById(R.id.tv_ChatInfoAdd);
        tvChatInfoAdd.setOnClickListener(this);
        tvChatInfoAdd.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_ChatInfoAdd:
                if (chatInfoPhone != null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("phoneBook", chatInfoPhone);
                    params.put("sign", sign);
                    okHttpPostBody(101, GlobalParam.FRIENDSUPDATE, params);
                }
                break;
            case R.id.tv_ChatInfoStart:
                RongIM.getInstance().startPrivateChat(this,targetId,tvChatInfoName.getText().toString());
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

        switch (requestCode) {
            case 100:
                if (returnCode == 200) {
                    RongFriendsModel result = JSON.parseObject(returnData, RongFriendsModel.class);
                    RongFriendsModel.DataBean userdata = result.getData();
                    RongFriendsModel.DataBean.InfoBean info = userdata.getInfo();
                    String chatInfoHead = info.getH();
                    Glide.with(this)
                            .load(chatInfoHead)
                            .fitCenter()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivChatInfoTopHead);
                    String chatInfoName = info.getN();
                    tvChatInfoName.setText(TextUtils.isEmpty(chatInfoName)?"":chatInfoName);
                    chatInfoPhone = info.getP();
                    tvChatInfoPhone.setText(chatInfoPhone);
                    RongFriendsModel.DataBean.RelationBean relation = userdata.getRelation();
                    if (relation != null) {
                        tvChatInfoAdd.setVisibility(View.GONE);
                    } else {
                        tvChatInfoAdd.setVisibility(View.VISIBLE);
                    }
                    tvChatInfoStart.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.showToast(returnMsg);
                }

                break;
            case 101:
                if (returnCode == 200) {
                    ToastUtil.showToast("好友添加成功！");
                    tvChatInfoAdd.setVisibility(View.GONE);
                }else {
                    ToastUtil.showToast("好友添加失败！");
                    tvChatInfoAdd.setVisibility(View.VISIBLE);
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
