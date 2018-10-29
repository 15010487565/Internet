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
    //未登录重置密码
    public final static String RESETPWDLOGIN=IP+"/robot/api/user/resetPwd";
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

    //支付密码
    public final static String VERIFYPASSWORD=IP+"/robot/api/userInfo/verifyPassword";
    //发红包
    public final static String SENDREDPACKET=IP+"/robot/api/redPacket/create";
    //抢红包
    public final static String GRAPREDPACKET=IP+"/robot/api/redPacket/grap";
    //钱包记录
    public final static String REDPACKETLIST=IP+"/robot/api/bag/list";

    //提现检测
    public final static String CASHCHECK=IP+"/robot/api/withdraw/check";
    //提现
    public final static String CASH=IP+"/robot/api/withdraw/check";
    //购买okd
    public final static String CASHOKD=IP+"/robot/api/bag/buy";

    // 检测验证码
    public final static String CHECKCODE=IP+"/robot/api/user/checkCode";
    // 修改登录密码
    public final static String RESETPWD=IP+"/robot/api/userInfo/resetPwd";
    // 设置/更新 支付密码
    public final static String PAYPASSWORD=IP+"/robot/api/userInfo/payPassword";
    // 好友列表
    public final static String FRIENDSLIST=IP+"/robot/api/friends/list";
    // 好友更新
    public final static String FRIENDSUPDATE=IP+"/robot/api/friends/update";
    // 总资产
    public final static String MEBAG=IP+"/robot/api/bag/my";
    // 更新个人信息
    public final static String ACCOUNTUPDATE=IP+"/robot/api/userInfo/update";
    // 上传图片（部分加密）
    public final static String UPLOADIMAGENNEXT=IP+"/robot/api/upload/img";
    // 身份证照片(查看/上传)
    public final static String UPLOADIMAGE=IP+"/robot/api/userInfo/cardPhoto";
    // 绑定银行卡
    public final static String BINDCARD=IP+"/robot/api/userInfo/bindCard";
    //邀请群组（通过邀请码新式） 二维码加入群
    public final static String GROUPINVITE=IP+"/robot/api/group/invite";
}
