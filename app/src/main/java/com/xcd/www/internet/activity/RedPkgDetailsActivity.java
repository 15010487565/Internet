package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.RedPkgOpenDetAdapter;
import com.xcd.www.internet.func.RedPkgRecordTopBtnFunc;
import com.xcd.www.internet.view.CircleImageView;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;

public class RedPkgDetailsActivity extends SimpleTopbarActivity implements   MultiSwipeRefreshLayout.OnLoadListener{


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
    }
    public void startRecord(){
        ToastUtil.showToast("点击红包记录");
    }
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        topbat_parent.setBackgroundResource(R.color.red);
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
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
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
                        adapter.upFootText();
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

//        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state) {
////                Log.e("STATE", state.name());
//                if (state == State.EXPANDED) {
//                    //展开状态
//                    loadGroupInfo.setEnabled(true);
//                } else if (state == State.COLLAPSED) {
//                    //折叠状态
//                    loadGroupInfo.setEnabled(false);
//                } else {
//                    //中间状态
//                    loadGroupInfo.setEnabled(false);
//                }
//            }
//        });
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

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
