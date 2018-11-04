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
import com.xcd.www.internet.sq.BlackNumBean;
import com.xcd.www.internet.util.EventBusMsg;
import com.xcd.www.internet.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import www.xcd.com.mylibrary.action.QuickAction;
import www.xcd.com.mylibrary.entity.BaseActionItem;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.SharePrefHelper;

import static www.xcd.com.mylibrary.utils.ToastUtil.showToast;

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
    String sign;
    String title;
    Conversation.ConversationNotificationStatus conversationNotificationStatus1;
    int value;
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
        EventBus.getDefault().register(this);
//        String title = getIntent().getData().getQueryParameter("title");
//        resetTopbarTitle(title);
        Uri data = getIntent().getData();
        targetId = data.getQueryParameter("targetId");
        Log.e("TAG_聊天界面", "targetId=" + targetId);
//        targetIds = data.getQueryParameter("targetIds");
        mConversationType = Conversation.ConversationType.valueOf(data.getLastPathSegment().toUpperCase(Locale.US));
        Log.e("TAG_聊天界面", "mConversationType=" + mConversationType);
        BaseApplication instance = BaseApplication.getInstance();
        sign = instance.getSign();
        getData();

        BlackNumBean blackNumBean = blackDao.rawQuery(targetId);
        String name = blackNumBean.getName();
        String nick = blackNumBean.getNick();
        String headUrl = blackNumBean.getHeadUrl();
        Log.e("TAG_聊天界面", "name=" + name);
        Log.e("TAG_聊天界面", "nick=" + nick);
        Log.e("TAG_聊天界面", "headUrl=" + headUrl);
        tvChatTopName.setText(TextUtils.isEmpty(name) ? "" : name);
        //头像
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(this)
                    .load(headUrl)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivChatTopHead);
        }
        boolean isHistoryMessages = SharePrefHelper.getInstance(this).getSpBoolean("isHistoryMessages", false);
        if (isHistoryMessages){
            int latestMessageId = SharePrefHelper.getInstance(this).getSpInt("latestMessageId");
            if (latestMessageId !=0){
                RongIM.getInstance().getHistoryMessages(mConversationType, targetId, "RC:TxtMsg", latestMessageId, 0,
                        new RongIMClient.ResultCallback<List<Message>>() {
                            @Override
                            public void onSuccess(List<Message> messages) {
                                SharePrefHelper.getInstance(RongChatActivity.this).putSpBoolean("isHistoryMessages", false);
                                SharePrefHelper.getInstance(RongChatActivity.this).putSpInt("latestMessageId", 0);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                SharePrefHelper.getInstance(RongChatActivity.this).putSpBoolean("isHistoryMessages", false);
                                SharePrefHelper.getInstance(RongChatActivity.this).putSpInt("latestMessageId", 0);
                            }
                        });
            }
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
    public void onClick(final View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_Back:
                finish();
                break;
            case R.id.iv_ChatTopHead:
//                ToastUtil.showToast("点击顶部头像");
                break;
            case R.id.tv_ChatTopMore:
                if (Conversation.ConversationType.PRIVATE.equals(mConversationType)) {
                    showChatRightActionBar(v, 0);
                } else if (Conversation.ConversationType.GROUP.equals(mConversationType)) {
                    showChatRightActionBar(v, 1);
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
                    tvChatTopName.setText(TextUtils.isEmpty(name) ? "" : name);
                    int memberNum = group.getMemberNum();
                    tvChatTopNumber.setVisibility(View.VISIBLE);
                    tvChatTopNumber.setText(memberNum + "位成员");
                    //头像
                    String avatar = group.getAvatar();
                    Glide.with(this)
                            .load(avatar)
                            .fitCenter()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivChatTopHead);

                    break;
                case 101:
                    RongFriendsModel result = JSON.parseObject(returnData, RongFriendsModel.class);
                    RongFriendsModel.DataBean userdata = result.getData();
                    friendInfo = userdata.getInfo();
                    String n = friendInfo.getN();
                    tvChatTopName.setText(TextUtils.isEmpty(n) ? "" : n);
                    tvChatTopNumber.setVisibility(View.GONE);
                    String h = friendInfo.getH();
                    Glide.with(this)
                            .load(h)
                            .fitCenter()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivChatTopHead);
                    break;
            }
        } else {
            showToast(returnMsg);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusMsg event) {
        String msg = event.getMsg();
        if ("RefreshGroupHead".equals(msg)) {

            getData();
        } else if ("RefreshGroupExit".equals(msg)) {
            finish();
        } else if ("RefreshGroupInfo".equals(msg)) {
            getData();
        }
    }

    public void showChatRightActionBar(View view, final int chatType) {
        final QuickAction quickAction = new QuickAction(RongChatActivity.this, QuickAction.VERTICAL);
        quickAction.addActionItem(new BaseActionItem(0, RongChatActivity.this.getString(R.string.chat_search),
                ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_search)));
        if ( value == 1) {
            quickAction.addActionItem(new BaseActionItem(1, RongChatActivity.this.getString(R.string.chat_message_close),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_message_close)));
        } else {
            quickAction.addActionItem(new BaseActionItem(1, RongChatActivity.this.getString(R.string.chat_message_open),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_message_open)));
        }
        if (chatType == 0) {
            quickAction.addActionItem(new BaseActionItem(2, RongChatActivity.this.getString(R.string.chat_friendfo),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_groupinfo)));
        } else if (chatType == 1) {
            quickAction.addActionItem(new BaseActionItem(2, RongChatActivity.this.getString(R.string.chat_groupinfo),
                    ContextCompat.getDrawable(RongChatActivity.this, R.mipmap.chat_groupinfo)));
        }

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                Intent intent = null;
                switch (actionId) {
                    case 0://搜索
                        intent = new Intent(RongChatActivity.this, SearchAllActivity.class);
                        if (chatType == 0) {//0是全部；1是个人,2是群组
                            intent.putExtra("searchType", 1);
                        } else if (chatType == 1) {
                            intent.putExtra("searchType", 2);
                        }
                        startActivity(intent);
                        break;

                    case 1://关闭通知
                        Conversation.ConversationType conversationtype = null;
                        String conversationTargetId = targetId;
                        if (chatType == 0) {
                            conversationtype = Conversation.ConversationType.PRIVATE;
                        } else if (chatType == 1) {
                            conversationtype = Conversation.ConversationType.GROUP;
                        }
                        RongIM.getInstance().setConversationNotificationStatus(conversationtype,conversationTargetId,
                                conversationNotificationStatus1, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                                    @Override
                                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                                        value = conversationNotificationStatus.getValue();
                                        if(value==1){
                                            conversationNotificationStatus1=conversationNotificationStatus.setValue(0);
                                        }else{
                                            conversationNotificationStatus1=conversationNotificationStatus.setValue(1);
                                        }
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }

                                });

                        break;

                    case 2://好友信息
                        if (chatType == 0) {
                            intent = new Intent(RongChatActivity.this, FriendInfoActivity.class);
                            if (friendInfo != null) {
                                //头像
//                            String h = friendInfo.getH();
//                            intent.putExtra("ChatInfoHead", TextUtils.isEmpty(h)?"":h);
//                            String n = friendInfo.getN();
//                            intent.putExtra("ChatInfoName", TextUtils.isEmpty(n)?"":n);
//                            //描述
//                            String p = friendInfo.getP();
//                            intent.putExtra("ChatInfoPhone", TextUtils.isEmpty(p)?"":p);
//                            //是否开始消息通知
                                intent.putExtra("targetId", friendInfo.getId());
                                startActivity(intent);
                            } else {
                                showToast("获取信息失败！");
                            }

                        } else if (chatType == 1) {
                            intent = new Intent(RongChatActivity.this, GroupInfoActivity.class);
                            if (group != null) {
                                //头像
                                String avatar = group.getAvatar();
                                intent.putExtra("GroupInfoHead", TextUtils.isEmpty(avatar) ? "" : avatar);
                                String name = group.getName();
                                intent.putExtra("GroupInfoName", TextUtils.isEmpty(name) ? "" : name);
                                //描述
                                String des = group.getDes();
                                intent.putExtra("GroupInfoDes", TextUtils.isEmpty(des) ? "" : des);
                                //是否开始消息通知
                                int type = group.getType();
                                intent.putExtra("GroupInfoType", type);
                                intent.putExtra("targetId", targetId);
                                int memberNum = group.getMemberNum();
                                intent.putExtra("memberNum", memberNum);
                                String code = group.getCode();
                                intent.putExtra("GroupInfoCode", code);
                                //群创建者
                                int groupUserid = group.getUserid();
                                intent.putExtra("groupUserid", String.valueOf(groupUserid));
                                startActivity(intent);
                            } else {
                                showToast("获取信息失败！");
                            }
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
        attributes.alpha = 0.3f;
        //此行代码主要是解决在华为手机上半透明效果无效的bug
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        window.setAttributes(attributes);
        quickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                attributes.alpha = 1.0f;
                window.setAttributes(attributes);
            }
        });
    }

    private void getData() {
        if (Conversation.ConversationType.PRIVATE.equals(mConversationType)) {
            Map<String, String> map = new HashMap<>();
            map.put("id", targetId);
            map.put("sign", sign);
            okHttpPostBody(101, GlobalParam.FRIENDSINFO, map);
        } else if (Conversation.ConversationType.GROUP.equals(mConversationType)) {
            Map<String, String> map = new HashMap<>();
            map.put("id", targetId);
            map.put("sign", sign);
            okHttpPostBody(100, GlobalParam.GETGROUPINFO, map);
        }
        Conversation.ConversationType conversationtype = null;
        String conversationTargetId = targetId;
        if (Conversation.ConversationType.PRIVATE.equals(mConversationType)) {
            conversationtype = Conversation.ConversationType.PRIVATE;
        } else if (Conversation.ConversationType.GROUP.equals(mConversationType)) {
            conversationtype = Conversation.ConversationType.GROUP;
        }
        RongIM.getInstance().getConversationNotificationStatus(conversationtype,conversationTargetId
                , new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        value = conversationNotificationStatus.getValue();
                        if(value==1){
                            conversationNotificationStatus1=conversationNotificationStatus.setValue(0);
                            title="免打扰";
                        }else{
                            conversationNotificationStatus1=conversationNotificationStatus.setValue(1);
                            title="取消免打扰";
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }

                });

    }
}

