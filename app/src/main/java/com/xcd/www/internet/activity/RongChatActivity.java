package com.xcd.www.internet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.model.GroupInfoModel;
import com.xcd.www.internet.rong.module.RongFriendsModel;
import com.xcd.www.internet.view.CircleImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.rong.imlib.model.Conversation;
import www.xcd.com.mylibrary.action.QuickAction;
import www.xcd.com.mylibrary.entity.BaseActionItem;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * 单聊
 * Created by Android on 2017/8/21.
 */

public class RongChatActivity extends BaseInternetActivity {

    private LinearLayout llBack, tvChatTopMore;
    private CircleImageView ivChatTopHead;
    private TextView tvChatTopName, tvChatTopNumber;
    private String targetId;//当前界面id
//    private String targetIds;//当前界面id
    private Conversation.ConversationType mConversationType; //会话类型
    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    @Override
    protected Object getTopbarTitle() {
        return "消息";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        String title = getIntent().getData().getQueryParameter("title");
        Log.e("TAG_聊天界面","title="+title);
//        resetTopbarTitle(title);
        Uri data = getIntent().getData();
        targetId = data.getQueryParameter("targetId");
        Log.e("TAG_聊天界面","targetId="+targetId);
//        targetIds = data.getQueryParameter("targetIds");
        mConversationType = Conversation.ConversationType.valueOf(data.getLastPathSegment().toUpperCase(Locale.US));
        Log.e("TAG_聊天界面","mConversationType="+mConversationType);
        String sign = BaseApplication.getInstance().getSign();
        if (Conversation.ConversationType.PRIVATE.equals(mConversationType)){
            Map<String, String> map = new HashMap<>();
            map.put("id", targetId);
            map.put("sign", sign);
            okHttpPostBody(101, GlobalParam.FRIENDSINFO, map);
        }else if (Conversation.ConversationType.GROUP.equals(mConversationType)){
            Map<String, String> map = new HashMap<>();
            map.put("id", targetId);
            map.put("sign", sign);
            okHttpPostBody(100, GlobalParam.GETGROUPINFO, map);
        }
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        initView();
    }

    private void initView() {
        llBack = findViewById(R.id.ll_Back);
        llBack.setOnClickListener(this);
        //头像
        ivChatTopHead = findViewById(R.id.iv_ChatTopHead);
        ivChatTopHead.setOnClickListener(this);
        //名字
        tvChatTopName = findViewById(R.id.tv_ChatTopName);
        //成员数量
        tvChatTopNumber = findViewById(R.id.tv_ChatTopNumber);
        //更多
        tvChatTopMore = findViewById(R.id.tv_ChatTopMore);
        tvChatTopMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_Back:
                finish();
                break;
            case R.id.iv_ChatTopHead:
                ToastUtil.showToast("点击顶部头像");
                break;
            case R.id.tv_ChatTopMore:
                if (Conversation.ConversationType.PRIVATE.equals(mConversationType)){
                    showChatRightActionBar(v);
                }else if (Conversation.ConversationType.GROUP.equals(mConversationType)){
                    showGroupRightActionBar(v);
                }


                break;
        }
    }
    private GroupInfoModel.DataBean.GroupBean group;
    private RongFriendsModel.DataBean.InfoBean friendInfo;
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100://获取群组信息
                    GroupInfoModel groupInfoModel = JSON.parseObject(returnData, GroupInfoModel.class);
                    GroupInfoModel.DataBean data = groupInfoModel.getData();
                    group = data.getGroup();
                    String name = group.getName();
                    tvChatTopName.setText(TextUtils.isEmpty(name)?"":name);
                    int memberNum = group.getMemberNum();
                    tvChatTopNumber.setVisibility(View.VISIBLE);
                    tvChatTopNumber.setText(memberNum+"位成员");
                    //头像
                    String avatar = group.getAvatar();
                    Glide.with(this)
                            .load(avatar)
                            .fitCenter()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.launcher_login)
                            .error(R.mipmap.launcher_login)
                            .into( ivChatTopHead);
                    
                    break;
                case 101:
                    RongFriendsModel result = JSON.parseObject(returnData, RongFriendsModel.class);
                    RongFriendsModel.DataBean userdata = result.getData();
                    friendInfo = userdata.getInfo();
                    String n = friendInfo.getN();
                    tvChatTopName.setText(TextUtils.isEmpty(n)?"":n);
                    tvChatTopNumber.setVisibility(View.GONE);
                    String h = friendInfo.getH();
                    Glide.with(this)
                            .load(h)
                            .fitCenter()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.launcher_login)
                            .error(R.mipmap.launcher_login)
                            .into( ivChatTopHead);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    public void showGroupRightActionBar(View view) {
        QuickAction quickAction = new QuickAction(RongChatActivity.this, QuickAction.VERTICAL);
        quickAction.addActionItem(new BaseActionItem(0, RongChatActivity.this.getString(R.string.chat_search),
                ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_search)));
        if (true){
            quickAction.addActionItem(new BaseActionItem(1, RongChatActivity.this.getString(R.string.chat_message_close),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_message_close)));
        }else {
            quickAction.addActionItem(new BaseActionItem(1, RongChatActivity.this.getString(R.string.chat_message_open),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_message_open)));
        }
        quickAction.addActionItem(new BaseActionItem(2, RongChatActivity.this.getString(R.string.chat_groupinfo),
                ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_groupinfo)));

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                Intent intent = null;
                switch (actionId) {
                    case 0://搜索

                        break;

                    case 1://关闭通知

                        break;

                    case 2://群组信息

                        intent = new Intent(RongChatActivity.this, GroupInfoActivity.class);
                        if (group != null){
                            //头像
                            String avatar = group.getAvatar();
                            intent.putExtra("GroupInfoHead", TextUtils.isEmpty(avatar)?"":avatar);
                            String name = group.getName();
                            intent.putExtra("GroupInfoName", TextUtils.isEmpty(name)?"":name);
                            //描述
                            String des = group.getDes();
                            intent.putExtra("GroupInfoDes", TextUtils.isEmpty(des)?"":des);
                            //是否开始消息通知
                            int type = group.getType();
                            intent.putExtra("GroupInfoType", type);
                            intent.putExtra("targetId",targetId);
                            int memberNum = group.getMemberNum();
                            intent.putExtra("memberNum",memberNum);
                            String code = group.getCode();
                            intent.putExtra("GroupInfoCode",code);
                            startActivity(intent);
                        }else {
                            ToastUtil.showToast("获取信息失败！");
                        }
                        break;

                    default:
                        break;
                }
            }
        });
        quickAction.show(view);
        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
        final Window window = RongChatActivity.this.getWindow();
        final WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha=0.3f;
        //此行代码主要是解决在华为手机上半透明效果无效的bug
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        window.setAttributes(attributes);
        quickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                attributes.alpha=1.0f;
                window.setAttributes(attributes);
            }
        });
    }
    public void showChatRightActionBar(View view) {
        QuickAction quickAction = new QuickAction(RongChatActivity.this, QuickAction.VERTICAL);
        quickAction.addActionItem(new BaseActionItem(0, RongChatActivity.this.getString(R.string.chat_search),
                ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_search)));
        if (true){
            quickAction.addActionItem(new BaseActionItem(1, RongChatActivity.this.getString(R.string.chat_message_close),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_message_close)));
        }else {
            quickAction.addActionItem(new BaseActionItem(1, RongChatActivity.this.getString(R.string.chat_message_open),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_message_open)));
        }
        quickAction.addActionItem(new BaseActionItem(2, RongChatActivity.this.getString(R.string.chat_friendfo),
                ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_groupinfo)));

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                Intent intent = null;
                switch (actionId) {
                    case 0://搜索

                        break;

                    case 1://关闭通知

                        break;

                    case 2://好友信息

                        intent = new Intent(RongChatActivity.this, FriendInfoActivity.class);
                        if (friendInfo != null){
                            //头像
                            String h = friendInfo.getH();
                            intent.putExtra("ChatInfoHead", TextUtils.isEmpty(h)?"":h);
                            String n = friendInfo.getN();
                            intent.putExtra("ChatInfoName", TextUtils.isEmpty(n)?"":n);
                            //描述
                            String p = friendInfo.getP();
                            intent.putExtra("ChatInfoPhone", TextUtils.isEmpty(p)?"":p);
                            //是否开始消息通知
                            intent.putExtra("targetId",friendInfo.getId());
                            startActivity(intent);
                        }else {
                            ToastUtil.showToast("获取信息失败！");
                        }


                        break;

                    default:
                        break;
                }
            }
        });
        quickAction.show(view);
        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
        final Window window = RongChatActivity.this.getWindow();
        final WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha=0.3f;
        //此行代码主要是解决在华为手机上半透明效果无效的bug
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        window.setAttributes(attributes);
        quickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                attributes.alpha=1.0f;
                window.setAttributes(attributes);
            }
        });
    }
}

