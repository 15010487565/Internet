package com.xcd.www.internet.rong;

import android.util.Log;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;

/**
 * Created by Android on 2017/8/28.
 */

public class SendMessageListener implements RongIM.OnSendMessageListener {
    @Override
    public Message onSend(Message message) {
        Log.e("TAG_"," onSend "+message.getContent()+" id "+message.getSenderUserId()+"  "+message.getTargetId());
        return message;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        return false;
    }
}
