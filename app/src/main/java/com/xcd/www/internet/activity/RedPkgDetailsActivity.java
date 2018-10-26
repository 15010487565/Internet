package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.RedPkgOpenDetAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.RedPkgRecordTopBtnFunc;
import com.xcd.www.internet.model.RedPkgDetailsModel;
import com.xcd.www.internet.view.CircleImageView;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;

public class RedPkgDetailsActivity extends SimpleTopbarActivity implements MultiSwipeRefreshLayout.OnLoadListener {


    private RecyclerView rcOpenRedPkgDet;
    private LinearLayoutManager mLinearLayoutManager;
    private MultiSwipeRefreshLayout loadGroupInfo;
    private CircleImageView ivOpenRedPkgDetHead;
    private RedPkgOpenDetAdapter adapter;
    private TextView tvOpenRedPkgDetName, tvOpenRedPkgDetRemark;
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
        Intent intent = getIntent();
        String redPkgId = intent.getStringExtra("redPkgId");
        String sign = BaseApplication.getInstance().getSign();
        long id = BaseApplication.getInstance().getId();
        Map<String, String> map = new HashMap<>();
        map.put("id", redPkgId);//红包id
        map.put("userId", String.valueOf(id));//userId用户id
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
        ivOpenRedPkgDetHead = findViewById(R.id.iv_OpenRedPkgDetHead);
        ivOpenRedPkgDetHead.setOnClickListener(this);
        //群组名称
        tvOpenRedPkgDetName = findViewById(R.id.tv_OpenRedPkgDetName);
        //备注
        tvOpenRedPkgDetRemark = findViewById(R.id.tv_OpenRedPkgDetRemark);
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
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    RedPkgDetailsModel redPkgDetailsModel = JSON.parseObject(returnData, RedPkgDetailsModel.class);
                    List<RedPkgDetailsModel.DataBean> data = redPkgDetailsModel.getData();
                    adapter.setData(data);
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
