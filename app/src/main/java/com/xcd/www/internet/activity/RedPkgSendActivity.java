package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.rong.RedPackageMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class RedPkgSendActivity extends BaseInternetActivity {

    private TextView tvRedSend;
    private EditText etRedPkgAmount, etRedPkgTotal, etRedPkgContent;
    String targetId;
    @Override
    protected Object getTopbarTitle() {
        return "发红包";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpkg_send);
        Intent intent = getIntent();
        targetId = intent.getStringExtra("targetId");
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        topbat_parent.setBackgroundResource(R.color.red);
        viewTitle.setTextColor(ContextCompat.getColor(this, R.color.orange_red));

        //总金额
        etRedPkgAmount = findViewById(R.id.et_RedPkgAmount);
        //个数
        etRedPkgTotal = findViewById(R.id.et_RedPkgTotal);
        //备注
        etRedPkgContent = findViewById(R.id.et_RedPkgContent);
        //发送红包
        tvRedSend = findViewById(R.id.tv_RedSend);
        tvRedSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_RedSend:
                String amount = etRedPkgAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)||Double.valueOf(amount)<=0){
                    ToastUtil.showToast("红包总额必须大于0！");
                    return;
                }
                String total = etRedPkgTotal.getText().toString().trim();
                if (TextUtils.isEmpty(total)||Double.valueOf(total)<=0){
                    ToastUtil.showToast("红包总额必须大于0！");
                    return;
                }
                String contentStr = etRedPkgContent.getText().toString().trim();
                if (TextUtils.isEmpty(total)||Double.valueOf(total)<=0){
                    ToastUtil.showToast("红包总额必须大于0！");
                    return;
                }

                long userId = BaseApplication.getInstance().getId();
                String sign = BaseApplication.getInstance().getSign();
                Map<String, String> map = new HashMap<>();
                map.put("amount", amount);
                map.put("total", total);
                map.put("type", total);//红包类型(0好友1群2聊天室)
                map.put("content", TextUtils.isEmpty(contentStr)?"恭喜发财，大吉大利":contentStr);//内容
                map.put("target", targetId);//目标id
                map.put("userId", String.valueOf(userId));//用户id
//                map.put("coin", coin );//币种（默认usdt）
                map.put("sign", sign);
                okHttpPostBody(100, GlobalParam.SENDREDPACKET, map);

                break;
        }
    }

    private void rongSendRedPkg(String targetId) {
        long userId = BaseApplication.getInstance().getId();
        String contentStr = etRedPkgContent.getText().toString().trim();
        RedPackageMessage c= new RedPackageMessage();
        String account = BaseApplication.getInstance().getAccount();
        c.setSendName(account);
        c.setId(String.valueOf(userId));
        c.setSendRedType("USDT");
        c.setRemark(TextUtils.isEmpty(contentStr)?"恭喜发财，大吉大利":contentStr);
        byte[] b=c.encode();
        Log.e("TAG_紅包","b="+new String(b));
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
         * 通过 {@link IRongCallback.ISendMessageCallback}
         * 中的方法回调发送的消息状态及消息体。</p>
         *
         * @param message     将要发送的消息体。
         * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
         *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
         *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
         * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
         * @param callback    发送消息的回调，参考 {@link IRongCallback.ISendMessageCallback}。
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
                Log.e("TAG_紅包失败","message="+message.toString());
                Log.e("TAG_紅包失败","errorCode="+errorCode.toString());
            }
        });
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            rongSendRedPkg(targetId);
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
}
