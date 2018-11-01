package com.xcd.www.internet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.InviteFriendBitAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.InviteFriendBitTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.ui.RecyclerViewDecoration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class InviteFriendBitActivity extends SimpleTopbarActivity implements InviteFriendBitAdapter.OnItemClickListener {

    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private InviteFriendBitAdapter adapter;
    List<ContactModel> listApp;
    private String targetId;
    private TextView tvInviteFriendBit;
    //    private String friendId;
    SmsManager sManager;

    private static Class<?> rightFuncArray[] = {InviteFriendBitTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "联系人";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend_bit);
        targetId = getIntent().getStringExtra("targetId");
        sManager = SmsManager.getDefault();
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        rcCreateGroup = findViewById(R.id.rc_CreateGroupNext);
        rcCreateGroup.setVisibility(View.VISIBLE);
        tvInviteFriendBit = findViewById(R.id.tv_InviteFriendBit);
        tvInviteFriendBit.setOnClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcCreateGroup.setLayoutManager(mLinearLayoutManager);
        adapter = new InviteFriendBitAdapter(this);
        adapter.setOnItemClickListener(this);
        rcCreateGroup.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcCreateGroup.addItemDecoration(recyclerViewDecoration);
        //判断是否能邀请到群主
        listApp = BaseApplication.getInstance().getPhoneList();
        for (int i = 0, j = listApp.size(); i < j; i++) {
            ContactModel contactModel = listApp.get(i);
            contactModel.setSelect(false);
        }
        adapter.setData(listApp);
    }

    private boolean isAllSelect = false;

    public void getInviteFriend() {
        if (!isAllSelect) {
            for (int i = 0, j = listApp.size(); i < j; i++) {
                ContactModel contactModel = listApp.get(i);
                contactModel.setSelect(!isAllSelect);
            }
            isAllSelect = true;
        } else {
            for (int i = 0, j = listApp.size(); i < j; i++) {
                ContactModel contactModel = listApp.get(i);
                contactModel.setSelect(!isAllSelect);
            }
            isAllSelect = false;
        }
        adapter.setData(listApp);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_InviteFriendBit:
                StringBuffer sb = new StringBuffer();
                for (int i = 0, j = listApp.size(); i < j; i++) {
                    ContactModel contactModel = listApp.get(i);
                    boolean select = contactModel.isSelect();
                    if (select){
                        String mobile = contactModel.getMobile();
                        sb.append(mobile);
                    }
                }
                Uri smsToUri = Uri.parse("smsto:" + sb.toString());
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("sms_body", "嗨，我正在使用Bichat聊天，方便快捷，快来试试！网址：http://www.quantusd.com/");
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    ToastUtil.showToast(returnMsg);
                    setResult(1);
                    finish();
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

    @Override
    public void onItemClick(View view, int position) {

        ContactModel contactModel = listApp.get(position);
        boolean select = contactModel.isSelect();
        contactModel.setSelect(!select);
//        boolean select1 = contactModel.isSelect();
//        if (select1){
//            friendId = contactModel.getUserId();
//        }else {
//            friendId = "0";
//        }
        adapter.setData(listApp);
    }
}