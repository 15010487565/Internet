package com.xcd.www.internet.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.model.GroupInfoModel;
import com.xcd.www.internet.rong.BaseExtensionModule;
import com.xcd.www.internet.rong.RedPackageItemProvider;
import com.xcd.www.internet.rong.RedPackageMessage;
import com.xcd.www.internet.rong.RongReceiveMessageListener;
import com.xcd.www.internet.rong.SendMessageListener;
import com.xcd.www.internet.rong.module.RongFriendsModel;
import com.xcd.www.internet.sq.BlackDao;
import com.yonyou.sns.im.core.YYIMChat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import www.xcd.com.mylibrary.config.HttpConfig;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.AppManager;
import www.xcd.com.mylibrary.utils.NetUtil;

import static www.xcd.com.mylibrary.utils.ToastUtil.showToast;


/**
 * application基类
 *
 * @author litfb
 * @version 1.0
 * @date 2014年4月10日
 */
public class BaseApplication extends BuglyApplication implements
        RongIM.UserInfoProvider,
        RongIM.GroupUserInfoProvider,
        HttpInterface, RongIM.ConversationListBehaviorListener,
        RongIMClient.ConnectionStatusListener {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    private List<ContactModel> phoneList;
    private List<ContactModel> friendList;
    private String account;
    private String sign;
    private long id;
    private String headportrait;
    private String token;
    private String name;
    private String nick;
    private String country;//区号
    private String passwordPay;

    private String cardNum;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPasswordPay() {
        return passwordPay;
    }

    public void setPasswordPay(String passwordPay) {
        this.passwordPay = passwordPay;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(String headportrait) {
        this.headportrait = headportrait;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<ContactModel> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<ContactModel> phoneList) {
        this.phoneList = phoneList;
    }

    public List<ContactModel> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<ContactModel> friendList) {
        this.friendList = friendList;
    }

    private String usdt;

    public String getUsdt() {
        return usdt;
    }

    public void setUsdt(String usdt) {
        this.usdt = usdt;
    }

    private String usdt_dollar;//usdt兑美元汇率

    public String getUsdt_dollar() {
        return usdt_dollar;
    }

    public void setUsdt_dollar(String usdt_dollar) {
        this.usdt_dollar = usdt_dollar;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        phoneList = new ArrayList<ContactModel>();
        RongIM.init(this);
        try {

//            //初始化第三方jar
            YYIMChat.getInstance().init(getApplicationContext());
//            YYIMChat.getInstance().configLogger(Log.VERBOSE, true, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            /**
             * 初始化融云
             */
            RongIM.init(this);
            //自定义消息
            RongIM.registerMessageType(RedPackageMessage.class);
            RongIM.getInstance().registerMessageTemplate(new RedPackageItemProvider());
            //消息监听
            RongIM.setOnReceiveMessageListener(new RongReceiveMessageListener());
            RongIM.getInstance().setSendMessageListener(new SendMessageListener());
            RongIM.setUserInfoProvider(this, true);
            RongIM.setGroupUserInfoProvider(this, true);
            RongIM.setConnectionStatusListener(this);
//            setMyExtensionModule();
            /**
             * 设置会话列表界面操作的监听器。
             */
            RongIM.setConversationListBehaviorListener(this);
            RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
                @Override
                public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback callback) {
                }
            });
            Log.e("TAG_融云", "=========BaseApplication");
            RongPushClient.checkManifest(getApplicationContext());
//            RongIM.setConversationListBehaviorListener(new RongConversationListBehaviorListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getVersionName() throws Exception {
        // 获取packagemanager的实例  
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息  
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_RUNNING_CRITICAL://内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
            case TRIM_MEMORY_RUNNING_LOW://内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
            case TRIM_MEMORY_RUNNING_MODERATE://内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
                System.gc();
                break;
            default:
                break;
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            // 杀死进程前需要关闭app 相关的服务，activity
            AppManager.getInstance().finishAllActivity();
            //停止所有服务
            ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * 当点击会话头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param targetId         被点击的用户id。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String targetId) {
        Log.e("TAG_会话列表头像", "targetId=" + targetId);
        return true;
    }

    /**
     * 当长按会话头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param targetId         被点击的用户id。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String targetId) {

        return false;
    }

    /**
     * 长按会话列表中的 item 时执行。
     *
     * @param context        上下文。
     * @param view           触发点击的 View。
     * @param uiConversation 长按时的会话条目。
     * @return 如果用户自己处理了长按会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    /**
     * 点击会话列表中的 item 时执行。
     *
     * @param context        上下文。
     * @param view           触发点击的 View。
     * @param uiConversation 会话条目。
     * @return 如果用户自己处理了点击会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
     */
//    public String uid;
//    private String conversationTargetId;
//    private String uiConversationTitle;
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        Conversation.ConversationType conversationType = uiConversation.getConversationType();
//        if (Conversation.ConversationType.PRIVATE.equals(conversationType)) {
//            if (uiConversation != null) {
//                conversationTargetId = uiConversation.getConversationTargetId();
//                Log.e("TAG_融云会话列表id", "conversationTargetId=" + conversationTargetId);
//                if (conversationTargetId != null && !"".equals(conversationTargetId)) {
//                    Map<String, Object> params = new HashMap<String, Object>();
//                    params.put("id", conversationTargetId);
////                    okHttpGet(104, GlobalParam.GETUSERINFOFORID, params);
////                    uiConversationTitle = uiConversation.getUIConversationTitle();
//                }
//            }
//            return true;
//        } else {
//            Log.e("TAG_融云====","4");
//            return false;
//        }
        return false;
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        Log.e("TAG_扩展栏", "moduleList=" + moduleList.size());
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }

            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new BaseExtensionModule());
            }
        }
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    @Override
    public UserInfo getUserInfo(String userid) {
        UserInfo userInfo = null;
//        if (userid.indexOf("系统")!=-1){
//            userInfo =  new UserInfo(userid,
//                    "系统消息",
//                    Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.mipmap.launcher_login));
//            Log.e("TAG_融云getUserInfo","系统消息="+userid);
//        }else {
//            userInfo =  new UserInfo(userid,
//                    userid,
//                    Uri.parse("https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=c1f24fd359e736d147138a08ab514ffc/241f95cad1c8a786b0eb4b016a09c93d71cf50ff.jpg"));
//        }
//        RongIM.getInstance().refreshUserInfoCache(userInfo);
        Log.e("TAG_融云个人", "userid=" + userid);
        Map<String, String> params = new HashMap<>();
        params.put("id", userid);
        params.put("sign", sign);
        okHttpPostBody(100, GlobalParam.FRIENDSINFO, params);
        return null;
    }

    public void okHttpPostBody(final int requestCode, String url, final Map<String, String> paramsMaps) {
        if (NetUtil.getNetWorking(this) == false) {
            showToast("请检查网络。。。");
            return;
        }
        OkHttpHelper.getInstance().postBodyHttp(requestCode, url, paramsMaps, new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    //请求错误
                    case HttpConfig.REQUESTERROR:
                        IOException error = (IOException) msg.obj;
                        onErrorResult(HttpConfig.REQUESTERROR, error);
                        break;
                    //解析错误
                    case HttpConfig.PARSEERROR:
                        onParseErrorResult(HttpConfig.PARSEERROR);
                        break;
                    //网络错误
                    case HttpConfig.NETERROR:
                        break;
                    //请求成功
                    case HttpConfig.SUCCESSCODE:
                        Bundle bundle = msg.getData();
                        int requestCode = bundle.getInt("requestCode");
                        int returnCode = bundle.getInt("returnCode");
                        String returnMsg = bundle.getString("returnMsg");
                        String returnData = bundle.getString("returnData");
                        Map<String, Object> paramsMaps = (Map) msg.obj;
                        onSuccessResult(requestCode, returnCode, returnMsg, returnData, paramsMaps);
                        break;
                }
            }
        });
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        switch (requestCode) {
            case 100:
                if (returnCode == 200) {
                    String name = null;
                    int id = 0;
                    String avatar = null;

                    RongFriendsModel result = JSON.parseObject(returnData, RongFriendsModel.class);
                    RongFriendsModel.DataBean userdata = result.getData();
                    RongFriendsModel.DataBean.InfoBean info = userdata.getInfo();
                    name = info.getN();
                    avatar = info.getH();
                    id = info.getId();

                    Log.e("TAG_融云绘画列表", "nickname=" + name + ";userid=" + id + ";avatar=" + avatar);

                    Uri parse = null;
                    if (TextUtils.isEmpty(avatar)) {

                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        View view = inflater.inflate(R.layout.wb_location_map_marker, null);
                        TextView txt = view.findViewById(R.id.tv_Sortlogo);
                        if (!TextUtils.isEmpty(name)) {
                            txt.setText(name.substring(0, 1));
                        }
                        Bitmap viewBitmap = getViewBitmap(view);
//                        Bitmap viewBitmap = getViewBitmap(TextUtils.isEmpty(name)?"":name);
                        String iamgeUrl = MediaStore.Images.Media.insertImage(getContentResolver(), viewBitmap, null, null);
//                        parse = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.mipmap.launcher_login);
                        parse = Uri.parse(iamgeUrl);
                    } else {
                        parse = Uri.parse(avatar);
                    }
                    if (name != null && id > 0) {
                        BlackDao blackDao = BlackDao.getInstance(getApplicationContext());
                        blackDao.addBlackNum(String.valueOf(id), name, name, avatar, info.getP());
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(String.valueOf(id), name, parse));
                    }
                }
                break;
            case 101:
                GroupInfoModel groupInfoModel = JSON.parseObject(returnData, GroupInfoModel.class);
                GroupInfoModel.DataBean data = groupInfoModel.getData();
                GroupInfoModel.DataBean.GroupBean group = data.getGroup();
                int groupId = group.getId();
                //头像
                String avatar = group.getAvatar();
                BlackDao blackDao = BlackDao.getInstance(getApplicationContext());
                String name = group.getName();
                blackDao.addBlackNum(String.valueOf(groupId), name, name, avatar, "");
                Uri parse = null;
                if (TextUtils.isEmpty(avatar)) {
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view = inflater.inflate(R.layout.wb_location_map_marker, null);
                    TextView txt = view.findViewById(R.id.tv_Sortlogo);
                    if (!TextUtils.isEmpty(name)) {
                        txt.setText(name.substring(0, 1));
                    }
                    Bitmap viewBitmap = getViewBitmap(view);
//                    Bitmap viewBitmap = getViewBitmap(TextUtils.isEmpty(name)?"":name);
                    String iamgeUrl = MediaStore.Images.Media.insertImage(getContentResolver(), viewBitmap, null, null);
//                        parse = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.mipmap.launcher_login);
                    parse = Uri.parse(iamgeUrl);
                } else {
                    parse = Uri.parse(avatar);
                }
                RongIM.getInstance().refreshGroupInfoCache(new Group(String.valueOf(groupId), name, parse));

                break;
        }
    }

    @Override
    public void onCancelResult() {

    }

    @Override
    public void onErrorResult(int errorCode, IOException errorExcep) {

    }

    @Override
    public void onParseErrorResult(int errorCode) {

    }

    @Override
    public void onFinishResult() {

    }

    @Override
    public GroupUserInfo getGroupUserInfo(String groupId, String userId) {
        Log.e("TAG_融云群", "groupId=" + groupId);
        Log.e("TAG_融云群", "userId=" + userId);
        Map<String, String> params = new HashMap<>();
        params.put("id", groupId);
        params.put("sign", sign);
        okHttpPostBody(101, GlobalParam.GETGROUPINFO, params);
        return null;
    }

    //把布局变成Bitmap
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
    private Bitmap getViewBitmap(String name) {

        Bitmap bitmap = Bitmap.createBitmap(100, 100,
                Bitmap.Config.ARGB_8888);

        bitmap.eraseColor(Color.parseColor("#9CAFC8"));//填充颜
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint .setTextSize(15);
        paint .setColor(Color.GREEN);
        paint .setFlags(1);
        paint .setStyle(Paint.Style.FILL);
        canvas.drawText(name, 100, 100, paint );
        return bitmap;
    }
}
