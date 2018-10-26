package com.xcd.www.internet.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2018/10/26.
 */

public class RedPkgDetailsListModel implements Serializable {

    /**
     * current : 1
     * size : 10
     * count : 65
     * data : [{"id":135,"type":2,"money":2.96,"direction":0,"userId":2,"time":"2018-08-17 19:38:05","content":"领取红包"},{"id":125,"type":1,"money":1,"direction":1,"userId":2,"time":"2018-08-17 19:20:33","content":"发红包"}]
     * code : 200
     * msg : 请求成功
     * timestamp : 1534838786142
     */

    private int current;
    private int size;
    private int count;
    private String code;
    private String msg;
    private long timestamp;
    private List<DataBean> data;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
         * id : 135
         * type : 2
         * money : 2.96
         * direction : 0
         * userId : 2
         * time : 2018-08-17 19:38:05
         * content : 领取红包
         */

        private int id;
        private int type;
        private double money;
        private int direction;
        private int userId;
        private String time;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
