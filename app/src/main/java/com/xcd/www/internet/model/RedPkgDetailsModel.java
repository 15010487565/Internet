package com.xcd.www.internet.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2018/10/26.
 */

public class RedPkgDetailsModel implements Serializable {

    /**
     * result : 0
     * bagNum : 1
     * data : [{"id":"","redPacketId":5,"userId":2,"amount":0.01,"grabTime":"2018-08-15 17:26:54","note":"","nick":"1","head":"http://39.104.101.29/group1/M00/00/08/rBiD9VtP_DuAATtfAAAFaLTswr4904.png"}]
     * code : 200
     * msg : 请求成功
     * timestamp : 1534326774106
     */

    private int result;
    private int bagNum;
    private String code;
    private String msg;
    private long timestamp;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getBagNum() {
        return bagNum;
    }

    public void setBagNum(int bagNum) {
        this.bagNum = bagNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id :
         * redPacketId : 5
         * userId : 2
         * amount : 0.01
         * grabTime : 2018-08-15 17:26:54
         * note :
         * nick : 1
         * head : http://39.104.101.29/group1/M00/00/08/rBiD9VtP_DuAATtfAAAFaLTswr4904.png
         */

        private String id;
        private int redPacketId;
        private int userId;
        private double amount;
        private String grabTime;
        private String note;
        private String nick;
        private String head;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getRedPacketId() {
            return redPacketId;
        }

        public void setRedPacketId(int redPacketId) {
            this.redPacketId = redPacketId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getGrabTime() {
            return grabTime;
        }

        public void setGrabTime(String grabTime) {
            this.grabTime = grabTime;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }
    }
}
