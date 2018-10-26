package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/24.
 */

public class CreateGroupModel implements Serializable {


    /**
     * data : {"id":32,"userid":2,"name":"wo","avatar":"","des":"","memberNum":1,"code":"http://www.quantusd.com/group/606FB8A03383CA62D38133EA6430E9BC","type":0,"time":"2018-09-13 11:02:07"}
     * code : 200
     * msg : 请求成功
     * timestamp : 1536807728801
     */

    private DataBean data;
    private String code;
    private String msg;
    private long timestamp;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * id : 32
         * userid : 2
         * name : wo
         * avatar :
         * des :
         * memberNum : 1
         * code : http://www.quantusd.com/group/606FB8A03383CA62D38133EA6430E9BC
         * type : 0
         * time : 2018-09-13 11:02:07
         */

        private long id;
        private long userid;
        private String name;
        private String avatar;
        private String des;
        private int memberNum;
        private String code;
        private int type;
        private String time;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getUserid() {
            return userid;
        }

        public void setUserid(long userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public int getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(int memberNum) {
            this.memberNum = memberNum;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
