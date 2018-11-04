package com.xcd.www.internet.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.zxing.Intents;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.fragment.ContactFragment;
import com.xcd.www.internet.fragment.FindFragment;
import com.xcd.www.internet.fragment.HomeFragment;
import com.xcd.www.internet.fragment.MeFragment;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import com.yonyou.sns.im.util.common.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.base.fragment.BaseFragment;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.SharePrefHelper;
import www.xcd.com.mylibrary.view.BadgeView;
import www.xcd.com.mylibrary.widget.SnsTabWidget;

import static io.rong.eventbus.util.ErrorDialogManager.KEY_TITLE;

/**
 * 主页面
 *
 * @author litfb
 * @version 1.0
 * @date 2014年9月23日
 */
public class MainActivity extends BaseInternetActivity {

    /**
     * 供应商
     * fragment classes
     */
    public static Class<?> fragmentArray[] = {
            HomeFragment.class,
            ContactFragment.class,
            FindFragment.class,
            MeFragment.class,
    };
    /**
     * tabs text
     */
    private static int[] MAIN_TAB_TEXT = new int[]{
            R.string.home,
            R.string.contact,
            R.string.find,
            R.string.me
    };
    /**
     * tabs image normal
     */
    private static int[] MAIN_TAB_IMAGE = new int[]{
            R.mipmap.icon_tab_home,
            R.mipmap.icon_tab_contact,
            R.mipmap.icon_tab_find,
            R.mipmap.main_tab_me
    };
    /**
     * tabs image selected
     */
    private static int[] MAIN_TAB_IMAGEHL = new int[]{
            R.mipmap.icon_tab_home_press,
            R.mipmap.icon_tab_contact_press,
            R.mipmap.icon_tab_find_press,
            R.mipmap.main_tab_me_press
    };

    @Override
    protected Object getTopbarTitle() {
        return R.string.app_name;
    }

    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    /**
     * fragment列表
     */
    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

    private ViewPager viewPager;

    private SnsTabWidget tabWidget;

    /**
     * 第一次返回按钮时间
     */
    private long firstTime;
    private int currentItem = 0;
    /**
     * 初始化分享弹出框
     */
    Dialog mShareDialog;
    TextView share_wexin, share_wexinfriends, share_qq, share_qzone;
    private ShareEntity testBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String account = SharePrefHelper.getInstance(this).getSpString("account");
        Log.e("TAG_主界面", "帐号=" + account);

        // 初始化fragments
        initFragments();
        // 初始化Tab
        initTabWidget();
        //实例化红点
        resetRedPoint(0, 0);
        resetRedPoint(1, 0);
        resetRedPoint(2, 0);
        resetRedPoint(3, 0);
        clickFragmentBtn(currentItem);

    }

    private void initView() {
        tabWidget =  findViewById(R.id.main_tabwidget);
        // 为布局添加fragment,开启事物
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tran = fm.beginTransaction();

        //R.id.relative为布局
        tran.add(R.id.frame_content, fragmentList.get(0), "home").show(fragmentList.get(0))
                .add(R.id.frame_content, fragmentList.get(1), "contact").hide(fragmentList.get(1))
                .add(R.id.frame_content, fragmentList.get(2), "find").hide(fragmentList.get(2))
                .add(R.id.frame_content, fragmentList.get(3), "me").hide(fragmentList.get(3));

        tran.commit();

    }

    /**
     * 初始化fragments
     */
    protected void initFragments() {
        // 初始化fragments
        for (int i = 0; i < fragmentArray.length; i++) {
            BaseFragment fragment = null;
            try {
                fragment = (BaseFragment) fragmentArray[i].newInstance();
                fragment.setActivity(this);
            } catch (Exception e) {
            }
            fragmentList.add(fragment);
        }
        initView();
    }

    /**
     * 初始化Tab
     */
    protected void initTabWidget() {
        // LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < fragmentArray.length; i++) {
            // 实例化tabitem
            View view = inflater.inflate(R.layout.view_main_tabitem, null);
            // 为每一个Tab按钮设置图标、文字和内容
            setTextViewStyle(view, i, (i == 0));
            tabWidget.addView(view);
        }
        // 选中第一个
        tabWidget.setCurrentTab(0);
        // 添加监听
        tabWidget.setTabSelectionListener(new MainTabSelectionListener());
    }

    /**
     * 重设TextView的样式
     *
     * @param index
     * @param isCur
     */
    protected void setTextViewStyle(View view, int index, boolean isCur) {
        // TextView
        TextView textView = (TextView) view.findViewById(R.id.main_tabitem_text);
        // 设置文字
        textView.setText(MAIN_TAB_TEXT[index]);

        // TextView
        TextView textViewHl = (TextView) view.findViewById(R.id.main_tabitem_texthl);
        // 设置文字
        textViewHl.setText(MAIN_TAB_TEXT[index]);

        // ImageView
        ImageView imageView = (ImageView) view.findViewById(R.id.main_tabitem_icon);
        // 非高亮图标
        imageView.setImageResource(MAIN_TAB_IMAGE[index]);

        // ImageView
        ImageView imageViewHl = (ImageView) view.findViewById(R.id.main_tabitem_iconhl);
        // 高亮图标
        imageViewHl.setImageResource(MAIN_TAB_IMAGEHL[index]);

        resetTextViewStyle(view, index, isCur);
    }

    /**
     * 重设TextView的样式
     *
     * @param index
     * @param isCur
     */
    protected void resetTextViewStyle(View view, int index, boolean isCur) {
        // 选中页签
        if (isCur) {
            resetTextViewAlpha(view, 1);
        } else {// 未选中页签
            resetTextViewAlpha(view, 0);
        }
    }

    /**
     * 重设TextView的Alpha值
     *
     * @param view
     * @param f
     */
    private void resetTextViewAlpha(View view, float f) {
        if (view == null) {
            return;
        }
        // ViewHl  通过设置透明度实现切换
        View itemViewHl = (View) view.findViewById(R.id.main_tabitem_viewhl);
        itemViewHl.setAlpha(f);
        //通过布局隐藏实现切换
        View itemViewH = (View) view.findViewById(R.id.main_tabitem_view);
        if (f == 1) {
            itemViewH.setVisibility(View.GONE);
        }
        if (f == 0) {
            itemViewH.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重设页面的Alpha
     *
     * @param index
     * @param f
     */
    private void resetFragmentAlpha(int index, float f) {
        if (index < 0 || index >= fragmentList.size()) {
            return;
        }
        View view = fragmentList.get(index).getView();
        if (view != null) {
            view.setAlpha(f);
        }
    }
    private void clickFragmentBtn(int tabIndex) {
        // 得到Fragment事务管理器
        FragmentTransaction fragmentTransaction = this
                .getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == tabIndex) {

                fragmentTransaction.show(fragmentList.get(i));
                resetTextViewAlpha(tabWidget.getChildAt(i), 1);
            } else {
                fragmentTransaction.hide(fragmentList.get(i));
                resetTextViewAlpha(tabWidget.getChildAt(i), 0);
            }
        }
        fragmentTransaction.commit();
    }
    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            BaseApplication.getInstance().exitApp();
        } else {
            firstTime = System.currentTimeMillis();
            // 再按一次返回桌面

            ToastUtil.showShort(this, R.string.main_press_again_back);
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        switch (requestCode) {

            case 101:
                try {
                    JSONObject jsonObject = new JSONObject(returnData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int id = data.optInt("id");
                    RongIM.getInstance().startGroupChat(MainActivity.this,String.valueOf(id), "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    /**
     * tab change监听
     *
     * @author litfb
     * @version 1.0
     * @date 2014年9月23日
     */
//    private boolean getIntentStoreApp = true;
    private class MainTabSelectionListener implements SnsTabWidget.ITabSelectionListener {

        @Override
        public void onTabSelectionChanged(int tabIndex) {

            // 重设当前页
            clickFragmentBtn(tabIndex);
        }
    }

    /**
     * 重设红点
     *
     * @param index
     * @param number
     */
    private void resetRedPoint(int index, int number) {
        View view = tabWidget.getChildAt(index);
        // red number
        BadgeView textRedpoint = (BadgeView) view.findViewById(R.id.main_tabitem_redpoint);
        if (number > 0) {
            if (String.valueOf(number).length() > 2) {
                textRedpoint.setText("...");
            } else {
                textRedpoint.setText(String.valueOf(number));
            }
            //隐藏红点
            textRedpoint.setVisibility(View.VISIBLE);
//			textRedpoint.setVisibility(View.GONE);
        } else {
            textRedpoint.setText("");
            textRedpoint.setVisibility(View.GONE);
        }
    }


    //分享
    public void onClickShare() {
        ToastUtil.showShort(this, "暂不支持分享功能！");
//        if (dialogIsActivity()) {
//            initShareDialog();
//        }
    }

    public void initShareDialog() {
        if (mShareDialog != null && mShareDialog.isShowing()) {
            return;
        }
        mShareDialog = new Dialog(this, R.style.dialog_bottom_full);
        mShareDialog.setCanceledOnTouchOutside(true);
        mShareDialog.setCancelable(true);
        Window window = mShareDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = View.inflate(this, R.layout.dialog_lay_share, null);
        share_wexin = view.findViewById(R.id.share_wexin);
        share_wexin.setOnClickListener(this);
        share_wexinfriends = view.findViewById(R.id.share_wexinfriends);
        share_wexinfriends.setOnClickListener(this);
        share_qq = view.findViewById(R.id.share_qq);
        share_qq.setOnClickListener(this);
//        share_qzone =  view.findViewById(R.id.share_qzone);
//        share_qzone.setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShareDialog != null && mShareDialog.isShowing()) {
                    mShareDialog.dismiss();
                }
            }
        });
        mShareDialog.show();
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 分享回调处理
         */
        if (requestCode == ShareConstant.REQUEST_CODE) {
            if (data != null) {
                int channel = data.getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, -1);
                int status = data.getIntExtra(ShareConstant.EXTRA_SHARE_STATUS, -1);
                switch (status) {
                    /** 成功 **/
                    case ShareConstant.SHARE_STATUS_COMPLETE:

                        break;
                    /** 失败 **/
                    case ShareConstant.SHARE_STATUS_FAILED:
                        if (dialogIsActivity()) {
                            initShareDialog();
                        }
                        break;
                    /** 取消 **/
                    case ShareConstant.SHARE_STATUS_CANCEL:
                        if (dialogIsActivity()) {
                            initShareDialog();
                        }
                        break;
                }
            }
        } else {
            if (resultCode == RESULT_OK && data != null) {
                switch (requestCode) {
                    case REQUEST_CODE_SCAN:
                        String result = data.getStringExtra(Intents.Scan.RESULT);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String type = jsonObject.optString("type");
                            String userId = jsonObject.optString("userId");

                            Log.e("TAG_二维码", "result=" + result);
                            if ("1".equals(type)){//群二维码
                                String code = jsonObject.optString("code");
                                Map<String, String> map = new HashMap<>();
                                map.put("id", userId );
                                map.put("code", code );
                                map.put("sign", BaseApplication.getInstance().getSign());
                                okHttpPostBody(101, GlobalParam.GROUPINVITE, map);
                            }else {
                                Intent  intent = new Intent(this, FriendInfoActivity.class);
                                intent.putExtra("targetId",Integer.valueOf(userId));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 11000:
                        scanAQRCode();
                        break;

                }

            }
        }
    }

    //扫一扫功能
    public static final int REQUEST_CODE_SCAN = 0X01;
    private PermissionsChecker mChecker;
    public void scanAQRCode() {
        mChecker = new PermissionsChecker(this);
//        Intent intent = new Intent(MainActivity.this, WeChatCaptureActivity.class);
//        startActivity(intent);
        String[] perms = {Manifest.permission.CAMERA};
        if (mChecker.lacksPermissions(perms)) {
            // 请求权限
            PermissionsActivity.startActivityForResult(this,11000,perms);
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.in, R.anim.out);
            Intent intent = new Intent(this, CustomCaptureActivity.class);
            intent.putExtra(KEY_TITLE, "扫一扫");
            ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_SCAN, optionsCompat.toBundle());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share_qq:
                tempSaveFragmentShare();
                ShareUtil.startShare(this, ShareConstant.SHARE_CHANNEL_QQ, testBean, ShareConstant.REQUEST_CODE);
                break;
//            case R.id.share_qzone:
//                tempSaveFragmentShare();
//                ShareUtil.startShare(this, ShareConstant.SHARE_CHANNEL_QZONE, testBean, ShareConstant.REQUEST_CODE);
//                break;
            case R.id.share_wexin:
                tempSaveFragmentShare();
                ShareUtil.startShare(this, ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND, testBean, ShareConstant.REQUEST_CODE);
                break;
            case R.id.share_wexinfriends:
                tempSaveFragmentShare();
                ShareUtil.startShare(this, ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE, testBean, ShareConstant.REQUEST_CODE);
                break;
        }
    }

    public void tempSaveFragmentShare() {
        testBean = new ShareEntity("传智", "嗨，我正在使用传智聊天，方便快捷，快\n" +
                "来试试！从这里下载");
        testBean.setUrl("www.xx.com"); //分享链接
        testBean.setImgUrl("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().disconnect();
    }
}
