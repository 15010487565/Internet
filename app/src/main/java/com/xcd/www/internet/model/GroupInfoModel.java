package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/24.
 */

public class GroupInfoModel implements Serializable {

    /**
     * data : {"group":{"id":30,"userid":2,"name":"wo","avatar":"","des":"","memberNum":1,"code":"http://www.quantusd.com/group/130E4BD9ACD767F4639CFC375120C7EB","type":0,"time":"2018-09-13 10:59:06","isGag":0},"relaton":{"gid":39,"isAdmin":0,"isNotify":0,"isShield":0,"time":1536910912000,"uid":18}}
     * code : 200
     * msg : 请求成功
     * timestamp : 1536822379858
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
         * group : {"id":30,"userid":2,"name":"wo","avatar":"","des":"","memberNum":1,"code":"http://www.quantusd.com/group/130E4BD9ACD767F4639CFC375120C7EB","type":0,"time":"2018-09-13 10:59:06","isGag":0}
         * relaton : {"gid":39,"isAdmin":0,"isNotify":0,"isShield":0,"time":1536910912000,"uid":18}
         */

        private GroupBean group;
        private RelatonBean relaton;

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public RelatonBean getRelaton() {
            return relaton;
        }

        public void setRelaton(RelatonBean relaton) {
            this.relaton = relaton;
        }

        public static class GroupBean {
            /**
             * id : 30
             * userid : 2
             * name : wo
             * avatar :
             * des :
             * memberNum : 1
             * code : http://www.quantusd.com/group/130E4BD9ACD767F4639CFC375120C7EB
             * type : 0
             * time : 2018-09-13 10:59:06
             * isGag : 0
             */

            private int id;
            private int userid;
            private String name;
            private String avatar;
            private String des;
            private int memberNum;
            private String code;
            private int type;
            private String time;
            private int isGag;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
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

            public int getIsGag() {
                return isGag;
            }

            public void setIsGag(int isGag) {
                this.isGag = isGag;
            }
        }

        public static class RelatonBean {
            /**
             * gid : 39
             * isAdmin : 0
             * isNotify : 0
             * isShield : 0
             * time : 1536910912000
             * uid : 18
             */

            private int gid;
            private int isAdmin;
            private int isNotify;
            private int isShield;
            private long time;
            private int uid;

            public int getGid() {
                return gid;
            }

            public void setGid(int gid) {
                this.gid = gid;
            }

            public int getIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(int isAdmin) {
                this.isAdmin = isAdmin;
            }

            public int getIsNotify() {
                return isNotify;
            }

            public void setIsNotify(int isNotify) {
                this.isNotify = isNotify;
            }

            public int getIsShield() {
                return isShield;
            }

            public void setIsShield(int isShield) {
                this.isShield = isShield;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }
    }
}
