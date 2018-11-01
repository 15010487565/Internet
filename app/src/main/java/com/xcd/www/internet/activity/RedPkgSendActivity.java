package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.model.PasswordVerifyModel;
import com.xcd.www.internet.model.RedPkgModel;
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
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class RedPkgSendActivity extends BaseInternetActivity {

    private TextView tvRedSend;
    private EditText etRedPkgAmount, etRedPkgTotal, etRedPkgContent;
    private String targetId;
    private String sign;
    private RedPkgModel.DataBean data;
    private int id;
    String conversationType;
    private TextView tvNumHint, tvMonetHint;
    @Override
    protected Object getTopbarTitle() {
        return "发红包";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpkg_send);
        sign = BaseApplication.getInstance().getSign();
        Intent intent = getIntent();
        targetId = intent.getStringExtra("targetId");
        conversationType = intent.getStringExtra("conversationType");
        if (conversationType.equals("group")){
            tvNumHint.setVisibility(View.VISIBLE);
            tvMonetHint.setVisibility(View.VISIBLE);
        }else if (conversationType.equals("private")){
            tvNumHint.setVisibility(View.GONE);
            tvMonetHint.setVisibility(View.GONE);
        }else {
            return;
        }
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        topbat_parent.setBackgroundResource(R.color.redpkg);
        viewTitle.setTextColor(ContextCompat.getColor(this, R.color.orange_red));
        tvNumHint = findViewById(R.id.tv_NumHint);
        tvMonetHint = findViewById(R.id.tv_MonetHint);
        //总金额
        etRedPkgAmount = findViewById(R.id.et_RedPkgAmount);
        etRedPkgAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);
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
                BaseApplication instance = BaseApplication.getInstance();
                String passwordPay = instance.getPasswordPay();
                if (TextUtils.isEmpty(passwordPay)){
                    ToastUtil.showToast("请先设置支付密码！");
                    return;
                }

                String amount = etRedPkgAmount.getText().toString().trim();
                try {
                    if (TextUtils.isEmpty(amount) || Double.valueOf(amount) <= 0) {
                        ToastUtil.showToast("红包总额必须大于0！");
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtil.showToast("请输入正确的红包金额！");
                    return;
                }
                String total = etRedPkgTotal.getText().toString().trim();
                try {
                    if (TextUtils.isEmpty(total) || Double.valueOf(total) <= 0) {
                        ToastUtil.showToast("红包数量必须大于0！");
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtil.showToast("请输入正确的红包数量");
                    return;
                }
                DialogUtil.getInstance()
                        .setContext(this)
                        .setCancelable(true)
                        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .title("温馨提示")
                        .hint("请输入支付密码")
                        .sureText("确定")
                        .cancelText("取消")
                        .setSureOnClickListener(new DialogUtil.OnClickListener() {
                            @Override
                            public void onClick(View view, String message) {
                                Map<String, String> map = new HashMap<>();
                                map.put("password", message );
                                map.put("sign", sign);
                                okHttpPostBody(101, GlobalParam.VERIFYPASSWORD, map);
                            }
                        })
                        .setCancelOnClickListener(new DialogUtil.OnClickListener() {

                            @Override
                            public void onClick(View view, String message) {

                            }
                        }).showEditDialog();


                break;
        }
    }

    private void rongSendRedPkg( String returnData) {
        RedPkgModel redPkgModel = JSON.parseObject(returnData, RedPkgModel.class);
        data = redPkgModel.getData();
        id = data.getId();
        String contentStr = etRedPkgContent.getText().toString().trim();
        RedPackageMessage c = new RedPackageMessage();
        String  headportrait = BaseApplication.getInstance().getHeadportrait();
        c.setHeadUrl(headportrait);
        c.setRedPacketId(String.valueOf(id));//redPacketId
//        c.setAmout();
        c.setContent(TextUtils.isEmpty(contentStr) ? "恭喜发财，大吉大利" : contentStr);
        c.setAmout(String.valueOf(data.getAmount()));
        c.setCoin(data.getCoin());
        c.setTotal(String.valueOf(data.getTotal()));
        String name = BaseApplication.getInstance().getNick();
        c.setSendName(name);
        long userId = BaseApplication.getInstance().getId();
        c.setSendID(String.valueOf(userId));
        byte[] b = c.encode();
        c.setExtra(new String(b));
        RedPackageMessage myTextMessage = new RedPackageMessage(b);
        Log.e("TAG_紅包发送", "myTextMessage=" + myTextMessage.toString());
        Message myMessage = null;
        if (conversationType.equals("group")){
            myMessage = Message.obtain(targetId, Conversation.ConversationType.GROUP, myTextMessage);
        }else if (conversationType.equals("private")){
            myMessage = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, myTextMessage);
        }else {
            return;
        }

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
        RongIM.getInstance().sendMessage(myMessage, "RCD:RedPacketMsg", "RCD:RedPacketMsg", new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                Log.e("TAG_紅包成功", "message=" + message.toString());
                finish();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                Log.e("TAG_紅包失败", "message=" + message.toString());
                Log.e("TAG_紅包失败", "errorCode=" + errorCode.getMessage()+"===="+errorCode.getValue());
            }
        });
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode){
                case 100:
                    rongSendRedPkg(returnData);
                    break;
                case 101:
                    String amount = etRedPkgAmount.getText().toString().trim();
                    try {
                        if (TextUtils.isEmpty(amount) || Double.valueOf(amount) <= 0) {
                            ToastUtil.showToast("红包总额必须大于0！");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ToastUtil.showToast("请输入正确的红包金额！");
                        return;
                    }
                    String total = etRedPkgTotal.getText().toString().trim();
                    try {
                        if (TextUtils.isEmpty(total) || Double.valueOf(total) <= 0) {
                            ToastUtil.showToast("红包数量必须大于0！");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ToastUtil.showToast("请输入正确的红包数量");
                        return;
                    }
                    String contentStr = etRedPkgContent.getText().toString().trim();

                    PasswordVerifyModel passwordVerifyModel = JSON.parseObject(returnData, PasswordVerifyModel.class);
                    PasswordVerifyModel.DataBean data = passwordVerifyModel.getData();
                    String code = data.getSign();
                    long userId = BaseApplication.getInstance().getId();

                    Map<String, String> map = new HashMap<>();
                    map.put("amount", amount);
                    map.put("total", total);
                    map.put("type", "1");//红包类型(0好友1群2聊天室)
                    map.put("content", TextUtils.isEmpty(contentStr) ? "恭喜发财，大吉大利" : contentStr);//内容
                    map.put("target", targetId);//目标id
                    map.put("userId", String.valueOf(userId));//用户id
                    map.put("coin", "usdt" );//币种（默认usdt）
                    map.put("code", code);
                    map.put("sign", sign);
                    okHttpPostBody(100, GlobalParam.SENDREDPACKET, map);
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
}
