package com.xcd.www.internet.rong.module;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/25.
 */

public class RongFriendsModel implements Serializable{

    /**
     * data : {"info":{"id":18,"p":"17600368410","h":"http://39.104.101.29/group1/M00/00/08/rBiD9VuWI82AL_LXAADsWE7Hy5Y391.jpg","n":"昵称"},"relation":{"me":2,"friend":18,"isShield":0,"isNotify":0}}
     * code : 200
     * msg : 请求成功
     * timestamp : 1536570492976
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
         * info : {"id":18,"p":"17600368410","h":"http://39.104.101.29/group1/M00/00/08/rBiD9VuWI82AL_LXAADsWE7Hy5Y391.jpg","n":"昵称"}
         * relation : {"me":2,"friend":18,"isShield":0,"isNotify":0}
         */

        private InfoBean info;
        private RelationBean relation;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public RelationBean getRelation() {
            return relation;
        }

        public void setRelation(RelationBean relation) {
            this.relation = relation;
        }

        public static class InfoBean {
            /**
             * id : 18
             * p : 17600368410
             * h : http://39.104.101.29/group1/M00/00/08/rBiD9VuWI82AL_LXAADsWE7Hy5Y391.jpg
             * n : 昵称
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

        public static class RelationBean {
            /**
             * me : 2
             * friend : 18
             * isShield : 0
             * isNotify : 0
             */

            private int me;
            private int friend;
            private int isShield;
            private int isNotify;

            public int getMe() {
                return me;
            }

            public void setMe(int me) {
                this.me = me;
            }

            public int getFriend() {
                return friend;
            }

            public void setFriend(int friend) {
                this.friend = friend;
            }

            public int getIsShield() {
                return isShield;
            }

            public void setIsShield(int isShield) {
                this.isShield = isShield;
            }

            public int getIsNotify() {
                return isNotify;
            }

            public void setIsNotify(int isNotify) {
                this.isNotify = isNotify;
            }
        }
    }
}
