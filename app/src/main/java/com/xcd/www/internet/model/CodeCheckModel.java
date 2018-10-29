package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/28.
 */

public class CodeCheckModel implements Serializable {

    /**
     * data : {"sign":"MTU0MDcwNzI1ODQ4MDJCQ0VDOEE4NkJEODcwRUUyODYzRjJCMTAwNkI5QzIy"}
     * code : 200
     * msg : OK
     * timestamp : 1540707258480
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
         * sign : MTU0MDcwNzI1ODQ4MDJCQ0VDOEE4NkJEODcwRUUyODYzRjJCMTAwNkI5QzIy
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
