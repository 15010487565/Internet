package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/24.
 */

public class LoginInfoModel implements Serializable {

    /**
     * data : {"id":22,"name":"奖金","role":0,"account":"17600368411","country":"86","password":"","nick":"qq","sex":1,"birthday":"","phone":"","wechat":"","qq":"","email":"","address":"","headportrait":"https://nim.nosdn.127.net/NTE5Njg0Nw==/bmltYV81MjIwOTAwNjMyXzE1MzU3MDY1OTAxMjRfZDBjZTMyZDItYTBkNi00NjNiLTk1OGUtYTIxYTk5ZDEwNzM3","time":"2018-09-05 16:42:50","token":"YtxBEjhW6h3BEot4NgPzNxPwFqmzZEjRwl7uItElu1w7bF2hpUBB2yeXnQqqcRPH2ELlHgulPWfa8HKE+G8HEw==","sign":"ddfb2e60acbab7ee2fd4c5af9a6e2b23","desc":"","code":"","status":0,"passwordPay":"8400a64c9cfb1f75727552698d9b9c8e","certificationName":2}
     * code : 200
     * msg : OK
     * timestamp : 1540350684147
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
         * id : 22
         * name : 奖金
         * role : 0
         * account : 17600368411
         * country : 86
         * password :
         * nick : qq
         * sex : 1
         * birthday :
         * phone :
         * wechat :
         * qq :
         * email :
         * address :
         * headportrait : https://nim.nosdn.127.net/NTE5Njg0Nw==/bmltYV81MjIwOTAwNjMyXzE1MzU3MDY1OTAxMjRfZDBjZTMyZDItYTBkNi00NjNiLTk1OGUtYTIxYTk5ZDEwNzM3
         * time : 2018-09-05 16:42:50
         * token : YtxBEjhW6h3BEot4NgPzNxPwFqmzZEjRwl7uItElu1w7bF2hpUBB2yeXnQqqcRPH2ELlHgulPWfa8HKE+G8HEw==
         * sign : ddfb2e60acbab7ee2fd4c5af9a6e2b23
         * desc :
         * code :
         * status : 0
         * passwordPay : 8400a64c9cfb1f75727552698d9b9c8e
         * certificationName : 2
         */

        private long id;
        private String name;
        private int role;
        private String account;
        private String country;
        private String password;
        private String nick;
        private int sex;
        private String birthday;
        private String phone;
        private String wechat;
        private String qq;
        private String email;
        private String address;
        private String headportrait;
        private String time;
        private String token;
        private String sign;
        private String desc;
        private String code;
        private int status;
        private String passwordPay;
        private int certificationName;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHeadportrait() {
            return headportrait;
        }

        public void setHeadportrait(String headportrait) {
            this.headportrait = headportrait;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPasswordPay() {
            return passwordPay;
        }

        public void setPasswordPay(String passwordPay) {
            this.passwordPay = passwordPay;
        }

        public int getCertificationName() {
            return certificationName;
        }

        public void setCertificationName(int certificationName) {
            this.certificationName = certificationName;
        }
    }
}
