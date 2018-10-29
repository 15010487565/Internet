package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/28.
 */

public class CodeModer implements Serializable {

    /**
     * data : 883661
     * code : 200
     * msg : OK
     * timestamp : 1540705983644
     */

    private String data;
    private String code;
    private String msg;
    private long timestamp;

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
}
