package com.xcd.www.internet.model;

import java.util.List;

/**
 * Created by gs on 2018/10/31.
 */

public class MainModel {

    /**
     * count : 1
     * data : [{"id":56,"fcodeid":0,"field":"rate_market","fieldname":"汇率","code":"","codedesc":"","enabled":1,"seq":0,"sub":[{"id":57,"fcodeid":56,"field":"dollar_rmb","fieldname":"美元兑人民币","code":"6.83","codedesc":"","enabled":1,"seq":0,"sub":""},{"id":58,"fcodeid":56,"field":"usdt_dollar","fieldname":"usdt兑美元","code":"1","codedesc":"","enabled":1,"seq":0,"sub":""}]}]
     * code : 200
     * msg : 请求成功
     * timestamp : 1536649472809
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
         * id : 56
         * fcodeid : 0
         * field : rate_market
         * fieldname : 汇率
         * code :
         * codedesc :
         * enabled : 1
         * seq : 0
         * sub : [{"id":57,"fcodeid":56,"field":"dollar_rmb","fieldname":"美元兑人民币","code":"6.83","codedesc":"","enabled":1,"seq":0,"sub":""},{"id":58,"fcodeid":56,"field":"usdt_dollar","fieldname":"usdt兑美元","code":"1","codedesc":"","enabled":1,"seq":0,"sub":""}]
         */

        private int id;
        private int fcodeid;
        private String field;
        private String fieldname;
        private String code;
        private String codedesc;
        private int enabled;
        private int seq;
        private List<SubBean> sub;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFcodeid() {
            return fcodeid;
        }

        public void setFcodeid(int fcodeid) {
            this.fcodeid = fcodeid;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getFieldname() {
            return fieldname;
        }

        public void setFieldname(String fieldname) {
            this.fieldname = fieldname;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodedesc() {
            return codedesc;
        }

        public void setCodedesc(String codedesc) {
            this.codedesc = codedesc;
        }

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public List<SubBean> getSub() {
            return sub;
        }

        public void setSub(List<SubBean> sub) {
            this.sub = sub;
        }

        public static class SubBean {
            /**
             * id : 57
             * fcodeid : 56
             * field : dollar_rmb
             * fieldname : 美元兑人民币
             * code : 6.83
             * codedesc :
             * enabled : 1
             * seq : 0
             * sub :
             */

            private int id;
            private int fcodeid;
            private String field;
            private String fieldname;
            private String code;
            private String codedesc;
            private int enabled;
            private int seq;
            private String sub;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getFcodeid() {
                return fcodeid;
            }

            public void setFcodeid(int fcodeid) {
                this.fcodeid = fcodeid;
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public String getFieldname() {
                return fieldname;
            }

            public void setFieldname(String fieldname) {
                this.fieldname = fieldname;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCodedesc() {
                return codedesc;
            }

            public void setCodedesc(String codedesc) {
                this.codedesc = codedesc;
            }

            public int getEnabled() {
                return enabled;
            }

            public void setEnabled(int enabled) {
                this.enabled = enabled;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public String getSub() {
                return sub;
            }

            public void setSub(String sub) {
                this.sub = sub;
            }
        }
    }
}
