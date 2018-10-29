package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/28.
 */

public class MeBagModel implements Serializable {

    /**
     * data : {"bagNum":0,"idnum":"","bankDeposit":"开户行","btc":0,"cardNum":"6226095711989751","cash":0,"eth":0,"id":22,"lit":0,"litFreeze":0,"ltc":0,"money":4612.485,"moneyFreeze":2612.49,"name":"巩梦南","okd":0,"usdt":0.22,"usdtFreeze":0.22}
     * code : 200
     * msg : 请求成功
     * timestamp : 1534838560310
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
         * bagNum : 0
         * idnum :
         * bankDeposit : 开户行
         * btc : 0.0
         * cardNum : 6226095711989751
         * cash : 0.0
         * eth : 0.0
         * id : 22
         * lit : 0.0
         * litFreeze : 0.0
         * ltc : 0.0
         * money : 4612.485
         * moneyFreeze : 2612.49
         * name : 巩梦南
         * okd : 0.0
         * usdt : 0.22
         * usdtFreeze : 0.22
         */

        private int bagNum;
        private String idnum;
        private String bankDeposit;
        private double btc;
        private String cardNum;
        private double cash;
        private double eth;
        private int id;
        private double lit;
        private double litFreeze;
        private double ltc;
        private double money;
        private double moneyFreeze;
        private String name;
        private double okd;
        private double usdt;
        private double usdtFreeze;

        public int getBagNum() {
            return bagNum;
        }

        public void setBagNum(int bagNum) {
            this.bagNum = bagNum;
        }

        public String getIdnum() {
            return idnum;
        }

        public void setIdnum(String idnum) {
            this.idnum = idnum;
        }

        public String getBankDeposit() {
            return bankDeposit;
        }

        public void setBankDeposit(String bankDeposit) {
            this.bankDeposit = bankDeposit;
        }

        public double getBtc() {
            return btc;
        }

        public void setBtc(double btc) {
            this.btc = btc;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public double getEth() {
            return eth;
        }

        public void setEth(double eth) {
            this.eth = eth;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLit() {
            return lit;
        }

        public void setLit(double lit) {
            this.lit = lit;
        }

        public double getLitFreeze() {
            return litFreeze;
        }

        public void setLitFreeze(double litFreeze) {
            this.litFreeze = litFreeze;
        }

        public double getLtc() {
            return ltc;
        }

        public void setLtc(double ltc) {
            this.ltc = ltc;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getMoneyFreeze() {
            return moneyFreeze;
        }

        public void setMoneyFreeze(double moneyFreeze) {
            this.moneyFreeze = moneyFreeze;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getOkd() {
            return okd;
        }

        public void setOkd(double okd) {
            this.okd = okd;
        }

        public double getUsdt() {
            return usdt;
        }

        public void setUsdt(double usdt) {
            this.usdt = usdt;
        }

        public double getUsdtFreeze() {
            return usdtFreeze;
        }

        public void setUsdtFreeze(double usdtFreeze) {
            this.usdtFreeze = usdtFreeze;
        }
    }
}
