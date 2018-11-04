package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.RedPkgOpenDetAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.RedPkgRecordTopBtnFunc;
import com.xcd.www.internet.model.RedPkgDetailsModel;
import com.xcd.www.internet.ui.RecyclerViewDecoration;
import com.xcd.www.internet.view.CircleImageView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;

import static com.xcd.www.internet.R.id.iv_OpenRedPkgDetHead;

public class RedPkgDetailsActivity extends SimpleTopbarActivity implements MultiSwipeRefreshLayout.OnLoadListener {


    private RecyclerView rcOpenRedPkgDet;
    private LinearLayoutManager mLinearLayoutManager;
    private MultiSwipeRefreshLayout loadGroupInfo;
    private CircleImageView ivOpenRedPkgDetHead;
    private RedPkgOpenDetAdapter adapter;
    private TextView tvOpenRedPkgDetName, tvOpenRedPkgDetRemark;
    private TextView tvOpenRedPkgbagNum, ivOpenRedPkgDetNumber, tvGo,tvOpemRedPkgHint;
    //总个数
    String total;
    //总金额
    String amout;
    long accountId;
    String sign;
    String redPkgId;
    private String groupId;//来源id
    private static Class<?> rightFuncArray[] = {RedPkgRecordTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "红包";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_pkg_details);
        //账号id
        accountId = BaseApplication.getInstance().getId();
        Intent intent = getIntent();
        redPkgId = intent.getStringExtra("redPkgId");
        String headUrl = intent.getStringExtra("headUrl");
        Glide.with(this)
                .load(headUrl)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivOpenRedPkgDetHead);
        String sendName = intent.getStringExtra("sendName");
        tvOpenRedPkgDetName.setText(sendName);
        String contentStr = intent.getStringExtra("content");
        tvOpenRedPkgDetRemark.setText(contentStr);
        //总个数
        total = intent.getStringExtra("total");
        //总金额
        amout = intent.getStringExtra("amout");
        groupId = intent.getStringExtra("groupId");
        sign = BaseApplication.getInstance().getSign();
        Map<String, String> map = new HashMap<>();
        map.put("id", redPkgId);//红包id
        map.put("userId", String.valueOf(accountId));//userId用户id
        map.put("sign", sign);
        okHttpPostBody(100, GlobalParam.GRAPREDPACKET, map);
    }

    public void startRecord() {
        startActivity(new Intent(this,MeRedPkgActivity.class));
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        topbat_parent.setBackgroundResource(R.color.redpkg);
        viewTitle.setTextColor(ContextCompat.getColor(this, R.color.orange_red));
        //头像
        ivOpenRedPkgDetHead = findViewById(iv_OpenRedPkgDetHead);
        ivOpenRedPkgDetHead.setOnClickListener(this);
        //群组名称
        tvOpenRedPkgDetName = findViewById(R.id.tv_OpenRedPkgDetName);
        //备注
        tvOpenRedPkgDetRemark = findViewById(R.id.tv_OpenRedPkgDetRemark);
        //剩余次数
        tvOpenRedPkgbagNum = findViewById(R.id.tv_OpenRedPkgbagNum);
        //抢到的金额
        ivOpenRedPkgDetNumber = findViewById(R.id.iv_OpenRedPkgDetNumber);
        //继续
        tvGo = findViewById(R.id.tv_Go);
        tvGo.setOnClickListener(this);
        tvOpemRedPkgHint = findViewById(R.id.tv_OpemRedPkgHint);

        //分页加载
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        //初始化tabRecyclerView
        rcOpenRedPkgDet = findViewById(R.id.rc_OpenRedPkgDet);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcOpenRedPkgDet.setLayoutManager(mLinearLayoutManager);
        //创建Adapter
        adapter = new RedPkgOpenDetAdapter(this);
//        adapter.setOnItemClickListener(this);
//        rcOpenRedPkgDet.setLoadingMoreEnabled(false);
        //rc线
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.black_ee));
        rcOpenRedPkgDet.addItemDecoration(recyclerViewDecoration);
        rcOpenRedPkgDet.setAdapter(adapter);
        rcOpenRedPkgDet.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                boolean isTop = recyclerView.canScrollVertically(-1);//返回false表示不能往下滑动，即代表到顶部了；
//                if (isTop) {
//                    loadGroupInfo.setEnabled(false);
//                } else {
//                    loadGroupInfo.setEnabled(true);
//                }
                boolean isBottom = recyclerView.canScrollVertically(1);//返回false表示不能往上滑动，即代表到底部了；
                //屏幕中最后一个可见子项的position
//                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                //当前屏幕所看到的子项个数
                int visibleItemCount = mLinearLayoutManager.getChildCount();
                //当前RecyclerView的所有子项个数
                int totalItemCount = mLinearLayoutManager.getItemCount();
//                Log.e("TAG_底部","isBottom="+isBottom+"visibleItemCount="+visibleItemCount+";totalItemCount="+totalItemCount);
                if (isBottom) {
                    loadGroupInfo.setBottom(false);
                } else {
                    if (visibleItemCount == totalItemCount) {
                        loadGroupInfo.setBottom(false);
//                        adapter.upFootText();
                    } else {
                        loadGroupInfo.setBottom(true);
                    }
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        loadGroupInfo = findViewById(R.id.load_GroupInfo);
//        loadGroupInfo.setOnRefreshListener(this);
        //禁止下拉
        loadGroupInfo.setEnabled(false);
        //上拉加載监听
        loadGroupInfo.setOnLoadListener(this);
        //设置样式刷新显示的位置
        loadGroupInfo.setProgressViewOffset(true, -20, 100);
        loadGroupInfo.setColorSchemeResources(R.color.red, R.color.orange, R.color.blue, R.color.black);
        AppBarLayout appBarLayout =  findViewById(R.id.appbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
//                Log.e("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    loadGroupInfo.setEnabled(true);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    loadGroupInfo.setEnabled(false);
                } else {
                    //中间状态
                    loadGroupInfo.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_Go:
                Map<String, String> map = new HashMap<>();
                map.put("id", redPkgId);//红包id
                map.put("userId", String.valueOf(accountId));//userId用户id
                map.put("sign", sign);
                okHttpPostBody(100, GlobalParam.GRAPREDPACKET, map);
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    try {
                        RedPkgDetailsModel redPkgDetailsModel = JSON.parseObject(returnData, RedPkgDetailsModel.class);
                        List<RedPkgDetailsModel.DataBean> data = redPkgDetailsModel.getData();
                        adapter.setData(data);
                        int bagNum = redPkgDetailsModel.getBagNum();
                        tvOpenRedPkgbagNum.setText(String.valueOf("您还有"+bagNum+"次抢红包机会，"));
                        int openSize = data.size();
                        //所有人钱数
                        double openAllMoney = 0;
                        for (int i = 0; i < openSize; i++) {
                            RedPkgDetailsModel.DataBean dataBean = data.get(i);
                            int userId = dataBean.getUserId();
                            double amount = dataBean.getAmount();
                            Log.e("TAG_accountId","accountId="+accountId);
                            Log.e("TAG_accountId","userId="+userId);
                            if (accountId == userId){
                                ivOpenRedPkgDetNumber.setText(String.valueOf(amount));
                            }
                            BigDecimal b1 = new BigDecimal(openAllMoney);
                            BigDecimal b2 = new BigDecimal(Double.toString(amount));

                            openAllMoney = b1.add(b2).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        BigDecimal b3 = new BigDecimal(openAllMoney);
                        BigDecimal b4 = new BigDecimal(amout);
                        openAllMoney = b4.subtract(b3).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                        tvOpemRedPkgHint.setText("领取"+openSize+"/"+total+"个，剩余"+openAllMoney+"元");
                        ////0来晚了1成功2已领取3红包过期失效4不能领取私聊自己的5次数不足
                        int result = redPkgDetailsModel.getResult();
                        if (result == 1){
                            String nick = BaseApplication.getInstance().getNick();
                            MessageContent content = InformationNotificationMessage.obtain(
                                    nick+ "抢了一个红包"
                            );
                            RongIM.getInstance().insertIncomingMessage(
                                    Conversation.ConversationType.GROUP,
                                    groupId,
                                    String.valueOf(accountId),
                                    new Message.ReceivedStatus(1),
                                    content,
                                    new RongIMClient.ResultCallback<Message>() {
                                        @Override
                                        public void onSuccess(Message message) {
                                            Log.e("TAG_发送小灰条","成功="+message.getContent());
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            Log.e("TAG_发送小灰条","失败="+errorCode.getMessage());
                                        }
                                    }
                            );
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            ToastUtil.showToast(returnMsg);
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
    public void onLoad() {

    }
}
