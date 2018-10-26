package www.xcd.com.mylibrary.entity;

/**
 * Created by dell on 2015-11-23.
 */
public class GlobalParam {

    public final static String APPLICATIONID = "com.xcd.www.internet";

    public final static int MSG_SHOW_LISTVIEW_DATA=7;

    public final static String IP="http://www.quantusd.com";//线上
//    public final static String IP="http://192.168.1.114";//本地测试
    //登录
    public final static String LOGIN=IP+"/robot/api/user/login";
    //注册
    public final static String REGISTER=IP+"/robot/api/user/reg";
    //登录注册验证码
    public final static String GETCODE=IP+"/robot/api/user/code";
    //重置密码
    public final static String RESETPWD=IP+"/robot/api/user/resetPwd";
    //创建群组
    public final static String CREATEGROUP=IP+"/robot/api/group/create";
    //修改群组信息
    public final static String GROUPUPDATE=IP+"/robot/api/group/update";
    //添加群成员
    public final static String ADDGROUPFRIEND=IP+"/robot/api/group/add";
    //好友信息
    public final static String FRIENDSINFO=IP+"/robot/api/friends/info";
    //获取群组信息
    public final static String GETGROUPINFO=IP+"/robot/api/group/info";
    //获取群组信息
    public final static String GETGROUPMEMBERLIST=IP+"/robot/api/group/member";

    //发红包
    public final static String SENDREDPACKET=IP+"/robot/api/redPacket/create";
    //抢红包
    public final static String GRAPREDPACKET=IP+"/robot/api/redPacket/grap";
    //钱包记录
    public final static String REDPACKETLIST=IP+"/robot/api/bag/list";
}
