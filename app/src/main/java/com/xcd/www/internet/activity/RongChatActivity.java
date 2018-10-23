package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcd.www.internet.R;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

/**
 * 单聊
 * Created by Android on 2017/8/21.
 */

public class RongChatActivity  extends SimpleTopbarActivity {

    private ImageView titleimage;
    private TextView chatname,chatnumber,chatlocation,chatcontext;
    private LinearLayout allchatinfo,linear_location;
//    private LinearLayout linear_number;
    @Override
    protected Object getTopbarTitle() {
        return "消息";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        String title = getIntent().getData().getQueryParameter("title");
        Log.e("TAG_聊天界面","title="+title);
        resetTopbarTitle(title);
        String targetId = getIntent().getData().getQueryParameter("targetId");
        Log.e("TAG_聊天界面","targetId="+targetId);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
