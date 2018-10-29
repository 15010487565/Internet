package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/27.
 */

public class PasswordVerifyModel implements Serializable {

    /**
     * data : {"sign":"MTU0MDYzMTcxMjQ0NkI2MURFQTM1MTIyMjRBOEFGMTE5QkE3RTk1MzM5NkU1"}
     * code : 200
     * msg : OK
     * timestamp : 1540631712446
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
         * sign : MTU0MDYzMTcxMjQ0NkI2MURFQTM1MTIyMjRBOEFGMTE5QkE3RTk1MzM5NkU1
         */

        private String sign;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
