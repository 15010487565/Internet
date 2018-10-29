package com.xcd.www.internet.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2018/10/28.
 */

public class FriendModel implements Serializable {

    /**
     * count : 5
     * data : [{"id":2,"p":"18035541466","h":"http://118.190.43.148/group1/M00/01/B0/dr4rlFu3HGGAAKQgAAEiJUNrQik928.jpg","n":"刘x"},{"id":17,"p":"13261787160","h":"","n":"传智17250"},{"id":18,"p":"17600368410","h":"http://118.190.43.148/group1/M00/01/B0/dr4rlFu5l8yAQazsAACxdvF3E68055.jpg","n":"gong2r"},{"id":19,"p":"13269768959","h":"","n":"7987988"},{"id":31,"p":"17600368410","h":"","n":"传智314965"}]
     * code : 200
     * msg : OK
     * timestamp : 1540716859986
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
         * h : http://118.190.43.148/group1/M00/01/B0/dr4rlFu3HGGAAKQgAAEiJUNrQik928.jpg
         * n : 刘x
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
    }
}
