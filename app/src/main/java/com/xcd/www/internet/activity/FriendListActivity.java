package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.FriendListAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.ui.RecyclerViewDecoration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class FriendListActivity extends SimpleTopbarActivity implements FriendListAdapter.OnItemClickListener{

    private RecyclerView rcFriendList;
    private LinearLayoutManager mLinearLayoutManager;
    private FriendListAdapter adapter;
    List<ContactModel> listApp;

    @Override
    protected Object getTopbarTitle() {
        return "好友列表";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        rcFriendList =  findViewById(R.id.rc_FriendList);
        rcFriendList.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcFriendList.setLayoutManager(mLinearLayoutManager);
        adapter = new FriendListAdapter(this);
        adapter.setOnItemClickListener(this);
        rcFriendList.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.black_ee));
        rcFriendList.addItemDecoration(recyclerViewDecoration);
        //判断是否能邀请到群主
        listApp = BaseApplication.getInstance().getFriendList();
        adapter.setData(listApp);
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
    public void onItemClick(View view, int position) {
        ContactModel contactModel = listApp.get(position);
        String userId = contactModel.getUserId();
        String name = contactModel.getName();
        if (!TextUtils.isEmpty(userId))
        RongIM.getInstance().startPrivateChat(this,userId, TextUtils.isEmpty(name)?"":name);
    }

}

