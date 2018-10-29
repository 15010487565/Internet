package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.InviteFriendAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.InviteFriendTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * 邀请好友界面
 */
public class InviteFriendActivity extends SimpleTopbarActivity implements InviteFriendAdapter.OnItemClickListener{

    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private InviteFriendAdapter adapter;
    List<ContactModel> listApp;
    private String targetId;
    private String friendId;
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
        rcCreateGroup =  findViewById(R.id.rc_CreateGroupNext);
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
        adapter.setData(listApp);
    }
    public void getInviteFriend(){
        if (!"0".equals(friendId)){
            String sign = BaseApplication.getInstance().getSign();
            Map<String, String> map = new HashMap<>();
            map.put("id", targetId );//群id
            map.put("friend", friendId);//好友id 临时默认id=34
            map.put("sign", sign);
            okHttpPostBody(100, GlobalParam.ADDGROUPFRIEND, map);
        }
    }
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
                case 100:
                    ToastUtil.showToast(returnMsg);
                    setResult(1);
                    finish();
                    break;
            }
        }else {
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
        boolean select1 = contactModel.isSelect();
        if (select1){
            friendId = contactModel.getUserId();
        }else {
            friendId = "0";
        }
        adapter.setData(listApp);
    }

}
