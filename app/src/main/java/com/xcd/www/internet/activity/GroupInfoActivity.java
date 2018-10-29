package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.GroupinfoListAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.GroupInfoTopBtnFunc;
import com.xcd.www.internet.model.GroupInfoListModel;
import com.xcd.www.internet.view.CircleImageView;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;


public class GroupInfoActivity extends SimpleTopbarActivity implements
        CompoundButton.OnCheckedChangeListener
        , MultiSwipeRefreshLayout.OnLoadListener {

    private CircleImageView ivGroupInfoTopHead;
    private Switch swGroupInfoMessage;
    private TextView tvGroupInfoName, tvGroupInfoMemberNum, tvGroupInfoMember;
    private int page = 1;//页数（默认从1开始）

    private GroupinfoListAdapter adapter;

    private AppBarLayout appBarLayout;

    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private MultiSwipeRefreshLayout loadGroupInfo;
    private LinearLayout llAddMember, llGroupQRCode;
    String groupInfoHead;
    String groupInfoName;
    String sign;
    String targetId;
    String groupInfoDes;//简介
    String groupInfoCode;//群Code;
    int type;
    int memberNum;
    private static Class<?> rightFuncArray[] = {GroupInfoTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return R.string.group_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        Intent intent = getIntent();
        groupInfoHead = intent.getStringExtra("GroupInfoHead");
        Glide.with(this)
                .load(groupInfoHead)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.launcher_login)
                .error(R.mipmap.launcher_login)
                .into(ivGroupInfoTopHead);

        groupInfoName = intent.getStringExtra("GroupInfoName");
        tvGroupInfoName.setText(groupInfoName);
        //简介
        groupInfoDes = intent.getStringExtra("GroupInfoDes");
        //是否开始消息通知
        type = intent.getIntExtra("GroupInfoType", 0);
        if (type == 0) {
            swGroupInfoMessage.setChecked(false);
        } else {
            swGroupInfoMessage.setChecked(true);
        }
        memberNum = intent.getIntExtra("memberNum", 0);
        tvGroupInfoMemberNum.setText(memberNum + "位成员");
        tvGroupInfoMember.setText(memberNum + "人");
        sign = BaseApplication.getInstance().getSign();
        targetId = intent.getStringExtra("targetId");
        groupInfoCode = intent.getStringExtra("GroupInfoCode");
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("id", targetId);
        map.put("page", String.valueOf(page));//页数（默认从1开始）
        map.put("limit", "10");//
        map.put("sign", sign);
        okHttpPostBody(100, GlobalParam.GETGROUPMEMBERLIST, map);
    }


    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        appBarLayout = findViewById(R.id.appbar);
        //头像
        ivGroupInfoTopHead = findViewById(R.id.iv_GroupInfoTopHead);
        ivGroupInfoTopHead.setOnClickListener(this);
        //群组名称
        tvGroupInfoName = findViewById(R.id.tv_GroupInfoName);
        swGroupInfoMessage = findViewById(R.id.sw_GroupInfoMessage);
        swGroupInfoMessage.setOnCheckedChangeListener(this);
        //群二维码
        llGroupQRCode = findViewById(R.id.ll_GroupQRCode);
        llGroupQRCode.setOnClickListener(this);
        //成员数量
        tvGroupInfoMemberNum = findViewById(R.id.tv_GroupInfoMemberNum);
        tvGroupInfoMember = findViewById(R.id.tv_GroupInfoMember);
        //添加新成员
        llAddMember = findViewById(R.id.ll_AddMember);
        llAddMember.setOnClickListener(this);
        //分页加载
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        //初始化tabRecyclerView
        rcCreateGroup = findViewById(R.id.rc_GroupInfo);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcCreateGroup.setLayoutManager(mLinearLayoutManager);
        //创建Adapter
        adapter = new GroupinfoListAdapter(this);
//        adapter.setOnItemClickListener(this);
//        rcCreateGroup.setLoadingMoreEnabled(false);
        //rc线
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcCreateGroup.addItemDecoration(recyclerViewDecoration);
        rcCreateGroup.setAdapter(adapter);
        rcCreateGroup.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        appBarLayout = findViewById(R.id.appbar);

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

    //编辑
    public void getEditorGroupInfo() {
        Intent intent = new Intent(this, GroupUpDataActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("GroupInfoHead", groupInfoHead);
        intent.putExtra("GroupInfoName", groupInfoName);
        intent.putExtra("GroupInfoDes", groupInfoDes);
        intent.putExtra("GroupInfoType", type);
        intent.putExtra("memberNum", memberNum);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_AddMember:
                Intent intent = new Intent(this, InviteFriendActivity.class);
                intent.putExtra("targetId", targetId);
                startActivityForResult(intent, 11000);
                break;
            case R.id.ll_GroupQRCode:
                Intent intent1 = new Intent(this, GroupCodeActivity.class);
                intent1.putExtra("GroupInfoHead", groupInfoHead);
                intent1.putExtra("GroupInfoName", groupInfoName);
                intent1.putExtra("GroupInfoDes", groupInfoDes);
                intent1.putExtra("GroupInfoCode", groupInfoCode);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11000:
                if (resultCode == 1) {//保存
                    page = 0;
                    getData();
                }
                break;

        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    GroupInfoListModel groupInfoListModel = JSON.parseObject(returnData, GroupInfoListModel.class);
                    List<GroupInfoListModel.DataBean> data = groupInfoListModel.getData();
                    Log.e("TAG_群组信息", "data=" + data.size());
                    if (data == null || data.size() == 0) {
//                        adapter.upFootText();
                    } else {
                        if (page > 1) {
                            adapter.addData(data);
                        } else {
                            adapter.setData(data);
                        }
                    }

                    loadGroupInfo.setLoading(false);
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            Log.e("TAG_SW", "开启");

        } else {
            Log.e("TAG_SW", "关闭");

        }
    }


    @Override
    public void onLoad() {
        page++;
        getData();
    }
}
