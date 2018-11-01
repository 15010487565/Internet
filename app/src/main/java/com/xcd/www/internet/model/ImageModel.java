package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/30.
 */

public class ImageModel implements Serializable {

    /**
     * data : http://39.104.101.29/group1/M00/00/08/rBiD9VtP_DuAATtfAAAFaLTswr4904.png
     * code : 200
     * msg : 请求成功
     * timestamp : 1534733429957
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
