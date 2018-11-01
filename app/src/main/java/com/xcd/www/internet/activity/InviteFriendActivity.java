package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.InviteFriendAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.InviteFriendTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.ui.RecyclerViewDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * 邀请好友界面
 */
public class InviteFriendActivity extends SimpleTopbarActivity implements InviteFriendAdapter.OnItemClickListener {

    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private InviteFriendAdapter adapter;
    List<ContactModel> listApp;//总好友
    List<ContactModel> listAdd;
    private String targetId;
    //    private String friendId;
    private static Class<?> rightFuncArray[] = {InviteFriendTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "成员";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        targetId = getIntent().getStringExtra("targetId");
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        rcCreateGroup = findViewById(R.id.rc_CreateGroupNext);
        rcCreateGroup.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcCreateGroup.setLayoutManager(mLinearLayoutManager);
        adapter = new InviteFriendAdapter(this);
        adapter.setOnItemClickListener(this);
        rcCreateGroup.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcCreateGroup.addItemDecoration(recyclerViewDecoration);
        //判断是否能邀请到群主
        listApp = BaseApplication.getInstance().getFriendList();
        listAdd = new ArrayList<>();
        Intent intent = getIntent();
        List<ContactModel> groupFriendList = (List<ContactModel>) intent.getSerializableExtra("groupFriendList");
        for (int i = 0, j = listApp.size(); i < j; i++) {
            ContactModel contactModel = listApp.get(i);
            String userId = contactModel.getUserId();
            contactModel.setSelect(false);
            boolean isexist = false;
            for (int k = 0; k < groupFriendList.size(); k++) {
                //群中已存在好友
                ContactModel contactModel1 = groupFriendList.get(k);
                String userId1 = contactModel1.getUserId();
                if (userId.equals(userId1)) {
                    isexist = true;
                    continue;
                }
            }
            if (!isexist) {
                listAdd.add(contactModel);
            }
        }
        adapter.setData(listAdd);
    }

    public void getInviteFriend() {
        StringBuffer sb = new StringBuffer();
        Log.e("TAG_sb", "listAdd=" + listAdd.toString());
        for (int i = 0, j = listAdd.size(); i < j; i++) {
            ContactModel contactModel = listAdd.get(i);
            String userId = contactModel.getUserId();
            boolean select = contactModel.isSelect();
            if (select) {
                sb.append(userId);
                if (i != j - 1) {
                    sb.append(",");
                }
            }
        }
        String s = sb.toString();
        String substring = s.substring(0, s.length() - 1);
        if (!TextUtils.isEmpty(sb.toString())) {
            String sign = BaseApplication.getInstance().getSign();
            Map<String, String> map = new HashMap<>();
            map.put("id", targetId);//群id
            map.put("friend", substring);//好友id 临时默认id=34
            map.put("sign", sign);
            okHttpPostBody(100, GlobalParam.ADDGROUPFRIEND, map);
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
        ContactModel contactModel = listAdd.get(position);
        boolean select = contactModel.isSelect();
        //单选
        for (int i = 0; i < listAdd.size(); i++) {
            ContactModel contactAll = listAdd.get(i);
            contactAll.setSelect(false);
        }

        contactModel.setSelect(!select);

        adapter.setData(listAdd);
    }

}
