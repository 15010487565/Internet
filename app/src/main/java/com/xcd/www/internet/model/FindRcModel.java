package com.xcd.www.internet.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2017/12/26.
 */

public class FindRcModel implements Serializable {


    /**
     * count : 5
     * data : [{"id":5,"user":2,"title":"《8问》\u201c计算机安全教母\u201d宋晓冬：区块链中，安全和隐私保护是两个概念_巴比特_服务于区块链创新者","brief":"宋晓冬教授于1996年获得清华大学学士学位，1999年获得卡内基梅隆大学计算机系硕士学位，2002年获得加州大学伯克利分校博士学位，2002年至2007年任教于卡耐基梅隆大学，2007年起任教于加州大学伯克利分校。当前的研究方向包括深度学习, 网络安全和区块链。曾获得麦克阿瑟奖 （MacArthur Fellowship），古根海姆奖（Guggenheim Fellowship），斯隆研究奖（Alfred P. Sloan Research Fellowship），《麻省理工科技评论》\u201c35岁以下科技创新35人\u201d奖（TR-35 Award）等。宋教授还是计算机安全领域中论文被引用次数最多的学者（AMiner Award）。","text":"","time":"2018-10-30 15:58:41","img":"http://www.quantusd.com/article/files/201809270516182189.jpg?v=1","url":"http://www.quantusd.com/article/5.html","type":0,"status":1,"from":"巴比特"},{"id":4,"user":2,"title":"学术向丨区块链扩容的关键：欺诈和数据可用性证明_巴比特_服务于区块链创新者","brief":"译者按：原文作者包括伦敦大学学院的两位博士(分别是获得艾伦.图灵研究所奖学金资助的Mustafa Al-Bassam以及获得欧洲委员会地平线2020 DECODE项目资助的Alberto Sonnino)，而另一位则是以太坊的创始人Vitalik Buttern。","text":"","time":"2018-10-30 15:57:25","img":"http://www.quantusd.com/article/files/201809281541391293(1).jpg","url":"http://www.quantusd.com/article/4.html","type":0,"status":1,"from":"巴比特"},{"id":3,"user":2,"title":"美国电信巨头AT&T正式获得比特币区块链专利，比特币节点优势凸显_巴比特_服务于区块链创新者","brief":"美国电信巨头AT&T已经获得了一种基于区块链技术的归属用户服务器（HSS）专利。","text":"","time":"2018-10-30 15:47:43","img":"http://www.quantusd.com/article/files/ATT.jpg","url":"http://www.quantusd.com/article/3.html","type":1,"status":1,"from":"巴比特"},{"id":2,"user":2,"title":"防止节点贿选，Block.one或将动用1亿个EOS_巴比特_服务于区块链创新者","brief":"当加密货币公司Block.one完成ICO并且发布首个EOSIO软件版本之后，其不仅仅募集了40亿美元的资金，而且在创始区块收到了EOS总量的十分之一，即1亿个EOS代币。如今，这家资金充裕的区块链初创公司承诺用这些代币来阻止BP（区块生产者）的投票垄断行为，无论是已经发生的或可能发生的。","text":"","time":"2018-10-30 15:17:09","img":"http://www.quantusd.com/article/files/201810020425078287.jpg","url":"http://www.quantusd.com/article/2.html","type":1,"status":1,"from":"巴比特"},{"id":1,"user":2,"title":"洪蜀宁：密码货币应该作为外币监管，稳定币和法定数字货币是伪需求_巴比特_服务于区块链创新者","brief":"2011年10月，还在人民银行南京分行任职的洪蜀宁，写下了国内第一篇研究比特币的学术论文《<a title=\"人民银行专家：比特币非同凡响 可挑战美元霸权\" target=\"_blank\">比特币：一种新型货币对金融体系的挑战<\/a>》，文章认为比特币是一种新型的货币，它的出现是对现有的货币和金融体系的颠覆，建议政府和中央银行应正视比特币的存在，并提出了3点现在看起来也颇为超前的建议。<\/p>\r\n<p>文章发出后，在早期的比特币圈引起广泛关注，不少人评价：\u201c作者在2011年就能有这样的认识，看来体系内也有跟得上潮流的极客人物\u201d，更有人盛赞\u201c央行内部不泛远见卓识之士，数字货币远景可期\u201d。遗憾的是，这篇文章发出后并未受到政府和央行的重视。可喜的是，一些人受到文章启发投身区块链领域，开辟了数字化迁徙的新大陆。<\/p>\r\n<p>如今，洪蜀宁早已从体制内抽身，投入到区块链创业公司，不遗余力地呼吁监管正视比特币等密码货币。","text":"","time":"2018-10-30 15:17:09","img":"http://www.quantusd.com/article/files/201809200730234279.jpg","url":"http://www.quantusd.com/article/1.html","type":1,"status":1,"from":"巴比特"}]
     * code : 200
     * msg : 请求成功
     * timestamp : 1541042543211
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
         * id : 5
         * user : 2
         * title : 《8问》“计算机安全教母”宋晓冬：区块链中，安全和隐私保护是两个概念_巴比特_服务于区块链创新者
         * brief : 宋晓冬教授于1996年获得清华大学学士学位，1999年获得卡内基梅隆大学计算机系硕士学位，2002年获得加州大学伯克利分校博士学位，2002年至2007年任教于卡耐基梅隆大学，2007年起任教于加州大学伯克利分校。当前的研究方向包括深度学习, 网络安全和区块链。曾获得麦克阿瑟奖 （MacArthur Fellowship），古根海姆奖（Guggenheim Fellowship），斯隆研究奖（Alfred P. Sloan Research Fellowship），《麻省理工科技评论》“35岁以下科技创新35人”奖（TR-35 Award）等。宋教授还是计算机安全领域中论文被引用次数最多的学者（AMiner Award）。
         * text :
         * time : 2018-10-30 15:58:41
         * img : http://www.quantusd.com/article/files/201809270516182189.jpg?v=1
         * url : http://www.quantusd.com/article/5.html
         * type : 0
         * status : 1
         * from : 巴比特
         */

        private int id;
        private int user;
        private String title;
        private String brief;
        private String text;
        private String time;
        private String img;
        private String url;
        private int type;
        private int status;
        private String from;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
