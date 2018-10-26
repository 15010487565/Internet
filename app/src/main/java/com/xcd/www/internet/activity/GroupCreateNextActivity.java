package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.CreateGroupNextAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.CreateGroupNextTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class GroupCreateNextActivity extends SimpleTopbarActivity implements CreateGroupNextAdapter.OnItemClickListener{

    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateGroupNextAdapter adapter;
    List<ContactModel> listApp;
    private static Class<?> rightFuncArray[] = {CreateGroupNextTopBtnFunc.class};
    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return R.string.home_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_next);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        rcCreateGroup =  findViewById(R.id.rc_CreateGroupNext);
        rcCreateGroup.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcCreateGroup.setLayoutManager(mLinearLayoutManager);
        adapter = new CreateGroupNextAdapter(this);
        adapter.setOnItemClickListener(this);
        rcCreateGroup.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcCreateGroup.addItemDecoration(recyclerViewDecoration);
        //判断是否能邀请到群主
       listApp = BaseApplication.getInstance().getListApp();
        for (int i = 0; i < listApp.size(); i++) {
            ContactModel contactModel = listApp.get(i);
            String mobile = contactModel.getMobile();
            if (i%3==0||i%5==0){
                contactModel.setEnable(true);
            }else {
                contactModel.setEnable(false);
            }
        }
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
        boolean select = contactModel.isSelect();
        contactModel.setSelect(!select);

        adapter.setData(listApp);
    }
    //下一步
    public void createGroupNext(){
        List<ContactModel> createGroupNextList = new ArrayList<>();
        for (int i = 0,j = listApp.size(); i < j; i++) {
            ContactModel contactModel = listApp.get(i);
            //是否选中
            boolean select = contactModel.isSelect();
            if (select){
                createGroupNextList.add(contactModel);
            }
        }
        Log.e("TAG_创建","list="+createGroupNextList.size());
        Intent intent = new Intent(this, GroupCreateActivity.class);
        intent.putExtra("createGroupNextList", (Serializable)createGroupNextList);
        startActivity(intent);
        finish();
    }
}
