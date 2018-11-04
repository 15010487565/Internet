package com.xcd.www.internet.model;

/**
 * Created by gs on 2018/11/3.
 */

public class SearchModel {

    private String targetId;
    private String nick;
    private String headUrl;
    private String messageCon;
    private int messageNum;
    private int latestMessageId;

    private int mseeageType;//0为title,1单聊，2为群聊

    public int getMseeageType() {
        return mseeageType;
    }

    public void setMseeageType(int mseeageType) {
        this.mseeageType = mseeageType;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getMessageCon() {
        return messageCon;
    }

    public void setMessageCon(String messageCon) {
        this.messageCon = messageCon;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setMessageNum(int messageNum) {

        this.messageNum = messageNum;
    }

    public int getLatestMessageId() {
        return latestMessageId;
    }

    public void setLatestMessageId(int latestMessageId) {
        this.latestMessageId = latestMessageId;
    }
}
