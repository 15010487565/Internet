package com.xcd.www.internet.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.InviteFriendBitActivity;
import com.xcd.www.internet.activity.SearchActivity;
import com.xcd.www.internet.adapter.FriendAdapter;
import com.xcd.www.internet.adapter.SortAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.func.ContactRightTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.model.FriendModel;
import com.xcd.www.internet.sq.BlackDao;
import com.xcd.www.internet.ui.RecyclerViewDecoration;
import com.xcd.www.internet.util.ContactUtil;
import com.xcd.www.internet.util.EventBusMsg;
import com.xcd.www.internet.util.PinyinComparator;
import com.xcd.www.internet.view.WaveSideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;

import static com.xcd.www.internet.R.id.swipe_layout;
import static com.xcd.www.internet.common.Config.TYPE_FRIEND;
import static com.xcd.www.internet.common.Config.TYPE_TITLE;

/**
 * Created by gs on 2018/10/16.
 */

public class ContactFragment extends SimpleTopbarFragment implements
        FriendAdapter.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener{

    private static final String[] AUTHORIMAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_CONTACTS
            , Manifest.permission.READ_CONTACTS
    };
    private PermissionsChecker mChecker;

    private RecyclerView rcContact,rcFriend;
    private LinearLayoutManager mLinearLayoutManager;
    private WaveSideBar mSideBar;
    private PinyinComparator mComparator;
    private SortAdapter mAdapter;
    private FriendAdapter friendAdapter;
    private RelativeLayout reSearch;
    private LinearLayout llInviteFriend;
    private MultiSwipeRefreshLayout contactSwLayout;
    List<ContactModel> contactInfo;//联系人数据
    String sign;
    //重绘fragment
    private LayoutInflater inflater;
    private View view;

    private BlackDao blackDao;

    private static Class<?> rightFuncArray[] = {ContactRightTopBtnFunc.class};

    @Override
    protected Object getTopbarTitle() {
        return R.string.contact;
    }

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        sign = BaseApplication.getInstance().getSign();

        blackDao = BlackDao.getInstance(getActivity());

        initSwipeRefreshLayout(view);
        initRecyclerView(view);
        reSearch = view.findViewById(R.id.re_ContactSearch);
        reSearch.setOnClickListener(this);
        llInviteFriend = view.findViewById(R.id.ll_InviteFriend);
        llInviteFriend.setOnClickListener(this);
        initPhone();
    }
    private void initSwipeRefreshLayout(View view) {
        contactSwLayout = view.findViewById(swipe_layout);
//        contactSwLayout.setOnRefreshListener(this);
        //禁止下拉
//        contactSwLayout.setEnabled(false);
        //下拉刷新监听
        contactSwLayout.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        contactSwLayout.setProgressViewOffset(true, -20, 100);
        contactSwLayout.setColorSchemeResources(R.color.red, R.color.orange, R.color.blue, R.color.black);
        AppBarLayout appBarLayout =  view.findViewById(R.id.appbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.e("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    contactSwLayout.setEnabled(true);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    contactSwLayout.setEnabled(false);
                } else {
                    //中间状态
                    contactSwLayout.setEnabled(false);
                }
            }
        });
    }
    private void initPhone() {
        try {
            mComparator = new PinyinComparator();
            mChecker = new PermissionsChecker(getActivity());
            if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                // 请求权限
                PermissionsActivity.startActivityForResult(getActivity(), 11000, AUTHORIMAGE);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
            } else {
                // 全部权限都已获取
                initContactData();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initRecyclerView(View view) {
        //好友
        rcFriend = view.findViewById(R.id.rc_Friend);
        //联系人
        rcContact = view.findViewById(R.id.rc_Contact);
        mSideBar = view.findViewById(R.id.sideBar);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcContact.setLayoutManager(mLinearLayoutManager);
        mAdapter = new SortAdapter(getActivity());
        rcContact.setAdapter(mAdapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                getActivity(), LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcContact.addItemDecoration(recyclerViewDecoration);
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.re_ContactSearch:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                intent.putExtra("list", (Serializable) contactInfo);
                startActivity(intent);
                break;
            case R.id.ll_InviteFriend:
                Intent intent1 = new Intent(getActivity(), InviteFriendBitActivity.class);
                intent1.putExtra("FriendType",1);
                startActivity(intent1);
                break;

        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    contactSwLayout.setRefreshing(false);
                    List<ContactModel> rongFrieng = new ArrayList<>();
                    FriendModel friendModel = JSON.parseObject(returnData, FriendModel.class);
                    List<FriendModel.DataBean> data = friendModel.getData();
                    Iterator it = contactInfo.iterator();
                    for (int i = 0, j = data.size(); i < j; i++) {
                        FriendModel.DataBean dataBean = data.get(i);
                        String p = dataBean.getP();
                        while (it.hasNext()) {
                            ContactModel contactModel = (ContactModel) it.next();
                            String mobile = contactModel.getMobile();
                            if (p.equals(mobile)) {
                                it.remove();
                            }
                        }
                        ContactModel model = new ContactModel();
                        model.setMobile(p);
                        String n = dataBean.getN();
                        model.setName(n);
                        String h = dataBean.getH();
                        model.setLogo(h);
                        String id = String.valueOf(dataBean.getId());
                        model.setUserId(id);
                        rongFrieng.add(model);
                        blackDao.addBlackNum(id,n,n, h,p);
                    }
                    BaseApplication.getInstance().setFriendList(rongFrieng);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setAutoMeasureEnabled(true);
                    rcFriend.setLayoutManager(linearLayoutManager);
                    friendAdapter = new FriendAdapter(getActivity());
                    friendAdapter.setOnItemClickListener(this);
                    rcFriend.setAdapter(friendAdapter);
                    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                            getActivity(), LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
                    rcFriend.addItemDecoration(recyclerViewDecoration);
                    friendAdapter.setData(rongFrieng);

                    BaseApplication.getInstance().setPhoneList(contactInfo);
                    List<ContactModel> listCon = new ArrayList<>();
                    for (int i = 0,j =contactInfo.size(); i < j ; i++) {
                        ContactModel contactModel_ = contactInfo.get(i);
                        ContactModel contactModel = null;
                        if (i == 0) {//等于0的时候绘制title
                            contactModel = new ContactModel();
                            contactModel.setType(TYPE_TITLE);
                            String letters = contactInfo.get(i).getLetters();
                            contactModel.setLetters(letters);
                            listCon.add(contactModel);
                            contactModel_.setType(TYPE_FRIEND);
                            listCon.add(contactModel_);
                        } else {

                            String letters = contactModel_.getLetters();
                            ContactModel contactModel_1 = contactInfo.get(i - 1);
                            if (null != letters && !letters.equals(contactModel_1.getLetters())) {
                                //字母不为空，并且不等于前一个，也要title
                                contactModel = new ContactModel();
                                contactModel.setType(TYPE_TITLE);
                                contactModel.setLetters(letters);
                                listCon.add(contactModel);
                                contactModel_.setType(TYPE_FRIEND);
                                listCon.add(contactModel_);
                            }else {
                                contactModel_.setType(TYPE_FRIEND);
                                listCon.add(contactModel_);
                            }
                        }
                    }
                    mAdapter.setData(listCon);
                    break;
                case 101:
                    Map<String, String> params = new HashMap<>();
                    params.put("sign", sign);
                    okHttpPostBody(100, GlobalParam.FRIENDSLIST, params);
                    break;
            }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 11000:
                if (hasAllPermissionsGranted(grantResults)) {
                    initContactData();
                } else {
                    ToastUtil.showToast("请在应用管理中打开“相机”访问权限！");
                }
                break;
        }
    }

    private void initContactData() {
        try {
            ContactUtil contactUtil = new ContactUtil(getActivity());
            List<ContactModel> contactInfo = contactUtil.getContactInfo();
            Iterator it = contactInfo.iterator();
            StringBuffer sb = new StringBuffer();
            while (it.hasNext()) {
                ContactModel contactModel = (ContactModel) it.next();
                String mobile = contactModel.getMobile();
                if (TextUtils.isEmpty(mobile) || "null".equals(mobile)) {
                    it.remove();
                }else {
                    sb.append(mobile);
                    sb.append(",");
                }
            }

            this.contactInfo = contactInfo;
            // 根据a-z进行排序源数据
            Collections.sort(this.contactInfo, mComparator);
            Log.e("TAG_contact","contactInfo="+contactInfo.toString());
            //如果add两个，那么按照先后顺序，依次渲染。
//            TitleItemDecoration mDecoration = new TitleItemDecoration(getActivity(), this.contactInfo);
//            rcContact.addItemDecoration(mDecoration);
//            rcContact.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            //保存到Application中
            Log.e("TAG_好友","sb="+sb.toString());
            Map<String, String> params = new HashMap<>();
            params.put("phoneBook", sb.toString());
            params.put("sign", sign);
            okHttpPostBody(101, GlobalParam.FRIENDSUPDATE, params);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        List<ContactModel> data = friendAdapter.getData();
        ContactModel contactModel = data.get(position);
        String userId = contactModel.getUserId();
        String name = contactModel.getName();
        RongIM.getInstance().startPrivateChat(getActivity(),userId, TextUtils.isEmpty(name)?"":name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusMsg event){
        String msg = event.getMsg();
        if ("RefreshContact".equals(msg)) {
//            initView(inflater, view);
            initPhone();
//            getActivity().onBackPressed();//销毁自己
        }
    }

    @Override
    public void onRefresh() {
        setDialogShow(false);
        initPhone();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = getActivity();
                handler.sendMessage(msg);
//                swipeRefreshLayout.setRefreshing(false);
            }
        };
        new Timer().schedule(timerTask, 2000);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    contactSwLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
