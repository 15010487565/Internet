package com.xcd.www.internet.rong;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by Android on 2017/8/14.
 */

public class RongReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

    /**
     * 收到消息的处理。
     * @param message 收到的消息实体。
     * @param left 剩余未拉取消息数目。
     * @return
     */
    @Override
    public boolean onReceived(Message message, int left) {
        //开发者根据自己需求自行处理
        Log.e("TAG_onReceived","message="+message.getSenderUserId()+";left="+left);
//        Log.e("TAG_onReceived","message="+((TextMessage)message.getContent()).getContent());
//        Log.e("TAG_onReceived","message="+message.getExtra()+";left="+left);
        return false;
    }
}