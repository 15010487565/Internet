package com.xcd.www.internet.util;

/**
 * Created by gs on 2018/2/11.
 */

public class EventBusMsg {

    public EventBusMsg() {

    }
    private String msg;
    public EventBusMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }
    private String msgCon;

    public String getMsgCon() {
        return msgCon;
    }

    public void setMsgCon(String msgCon) {
        this.msgCon = msgCon;
    }
}
