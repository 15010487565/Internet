package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.rong.RedPackageMessage;

import java.io.IOException;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class RedPkgSendActivity extends SimpleTopbarActivity {

    private TextView tvRedSend;

    @Override
    protected Object getTopbarTitle() {
        return "发红包";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpkg_send);
        topbat_parent.setBackgroundResource(R.color.red);
        viewTitle.setTextColor(ContextCompat.getColor(this, R.color.orange_red));
        //发送红包
        tvRedSend = findViewById(R.id.tv_RedSend);
        tvRedSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_RedSend:
                Intent intent = getIntent();
                String targetId = intent.getStringExtra("targetId");
                RedPackageMessage c= new RedPackageMessage();
                c.setSendName("我的昵称");
                c.setFromUserId("我的帐号");
                c.setSendRedType("USDT");
                byte[] b=c.encode();
                Log.e("TAG_紅包","b="+b.toString());
                RedPackageMessage myTextMessage = new RedPackageMessage(b);
                /* 生成 Message 对象。
                * "12345678" 为目标 Id。根据不同的 conversationType，可能是用户 Id、群组 Id 或聊天室 Id。
                * Conversation.ConversationType.PRIVATE 为私聊会话类型，根据需要，也可以传入其它会话类型，如群组。
                */
//                Intent intent = getIntent();
//                String targetId = intent.getStringExtra("targetId");
                Message myMessage = Message.obtain(targetId, Conversation.ConversationType.GROUP, myTextMessage);

                /**
                 * <p>发送消息。
                 * 通过 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}
                 * 中的方法回调发送的消息状态及消息体。</p>
                 *
                 * @param message     将要发送的消息体。
                 * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
                 *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
                 *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
                 * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
                 * @param callback    发送消息的回调，参考 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}。
                 */
                RongIM.getInstance().sendMessage(myMessage, "app:RedPkgMsg", null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        //消息本地数据库存储成功的回调
                    }

                    @Override
                    public void onSuccess(Message message) {
                        //消息通过网络发送成功的回调
                        finish();
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        //消息发送失败的回调
                        Log.e("TAG_红包发送","message="+message.toString());
                        Log.e("TAG_红包发送","errorCode="+errorCode.toString());
                    }
                });
                break;
        }
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
