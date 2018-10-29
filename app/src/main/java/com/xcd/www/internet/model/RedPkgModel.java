package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/27.
 */

public class RedPkgModel implements Serializable {

    /**
     * result : 1
     * data : {"id":170,"userId":18,"amount":0.01,"sendDate":"2018-10-27 09:40:37","total":1,"remainAmount":0.01,"stock":1,"type":1,"target":"101","content":"恭喜发财，大吉大利","coin":"usdt","isRepeatGet":"","code":"MTU0MDYzMzIzNzM5NzhEQzcxNkI4OTQ3MzIwODgyRDFDOUEyRTFCOTkxQTg5"}
     * code : 200
     * msg : OK
     * timestamp : 1540633237837
     */

    private int result;
    private DataBean data;
    private String code;
    private String msg;
    private long timestamp;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

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
         * id : 170
         * userId : 18
         * amount : 0.01
         * sendDate : 2018-10-27 09:40:37
         * total : 1
         * remainAmount : 0.01
         * stock : 1
         * type : 1
         * target : 101
         * content : 恭喜发财，大吉大利
         * coin : usdt
         * isRepeatGet :
         * code : MTU0MDYzMzIzNzM5NzhEQzcxNkI4OTQ3MzIwODgyRDFDOUEyRTFCOTkxQTg5
         */

        private int id;
        private int userId;
        private double amount;
        private String sendDate;
        private int total;
        private double remainAmount;
        private int stock;
        private int type;
        private String target;
        private String content;
        private String coin;
        private String isRepeatGet;
        private String code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getSendDate() {
            return sendDate;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public double getRemainAmount() {
            return remainAmount;
        }

        public void setRemainAmount(double remainAmount) {
            this.remainAmount = remainAmount;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getIsRepeatGet() {
            return isRepeatGet;
        }

        public void setIsRepeatGet(String isRepeatGet) {
            this.isRepeatGet = isRepeatGet;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
