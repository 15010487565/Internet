package com.xcd.www.internet.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2018/10/24.
 */

public class GroupInfoListModel implements Serializable {


    /**
     * count : 1
     * data : [{"id":2,"p":"18035541466","h":"https://nim.nosdn.127.net/NTE5Njg0Nw==/bmltYV8yNDcwNTM1OTgyXzE1MzM4OTgxNzU5MTJfOGVlNTgwMWYtZWU2NC00ZTc2LWFhNTktOTU5NjdkYTEyZjQx","n":"刘"}]
     * code : 200
     * msg : 请求成功
     * timestamp : 1536828771384
     */

    private int count;
    private String code;
    private String msg;
    private long timestamp;
    private List<DataBean> data;

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
         * id : 2
         * p : 18035541466
         * h : https://nim.nosdn.127.net/NTE5Njg0Nw==/bmltYV8yNDcwNTM1OTgyXzE1MzM4OTgxNzU5MTJfOGVlNTgwMWYtZWU2NC00ZTc2LWFhNTktOTU5NjdkYTEyZjQx
         * n : 刘
         */

        private int id;
        private String p;
        private String h;
        private String n;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getH() {
            return h;
        }

        public void setH(String h) {
            this.h = h;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", p='" + p + '\'' +
                    ", h='" + h + '\'' +
                    ", n='" + n + '\'' +
                    '}';
        }
    }
}
