package com.xcd.www.internet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.GroupinfoListAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.GroupUpdataTopBtnFunc;
import com.xcd.www.internet.model.GroupInfoListModel;
import com.xcd.www.internet.view.CircleImageView;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;

public class GroupUpDataActivity extends PhotoActivity implements
        MultiSwipeRefreshLayout.OnLoadListener {

    private CircleImageView ivUploadHead;
    private LinearLayout llUploadHead;
    private static final String[] AUTHORIMAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA
    };
    private PermissionsChecker mChecker;
    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private MultiSwipeRefreshLayout loadGroupInfo;
    private LinearLayout llAddMember, llGroupUpdataDes;
    private EditText etGroupUpdataName;
    private Switch swGroupUpdataMessage;
    private TextView tvGroupUpdataDes, tvGroupInfoMemberNum, tvGroupInfoMember;
    private GroupinfoListAdapter adapter;
    private String targetId;
    private int page = 1;//页数（默认从1开始）
    String groupInfoHead;
    private String groupUpdataDes;

    private static Class<?> rightFuncArray[] = {GroupUpdataTopBtnFunc.class};

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
        setContentView(R.layout.activity_updata_group);
        mChecker = new PermissionsChecker(this);
        Intent intent = getIntent();
        targetId = intent.getStringExtra("targetId");
        groupInfoHead = intent.getStringExtra("GroupInfoHead");
        Glide.with(this)
                .load(groupInfoHead)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.launcher_login)
                .error(R.mipmap.launcher_login)
                .into(ivUploadHead);

        String groupInfoName = intent.getStringExtra("GroupInfoName");
        etGroupUpdataName.setText(TextUtils.isEmpty(groupInfoName)?"":groupInfoName);
        //描述
        groupUpdataDes = intent.getStringExtra("GroupInfoDes");
        tvGroupUpdataDes.setText(TextUtils.isEmpty(groupUpdataDes)?"":groupUpdataDes);
        //是否开始消息通知
        int type = intent.getIntExtra("GroupInfoType", 0);
        if (type == 0) {
            swGroupUpdataMessage.setChecked(false);
        } else {
            swGroupUpdataMessage.setChecked(true);
        }
        int memberNum = intent.getIntExtra("memberNum", 0);
        tvGroupInfoMemberNum.setText(memberNum + "位成员");
        tvGroupInfoMember.setText(memberNum + "人");
        getData();
    }

    private void getData() {
        String sign = BaseApplication.getInstance().getSign();
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
        //群名称
        etGroupUpdataName = findViewById(R.id.et_GroupUpdataName);
        //头像
        ivUploadHead = findViewById(R.id.iv_UploadHead);
        llUploadHead = findViewById(R.id.ll_UploadHead);
        llUploadHead.setOnClickListener(this);
        //群简介
        llGroupUpdataDes = findViewById(R.id.ll_GroupUpdataDes);
        llGroupUpdataDes.setOnClickListener(this);
        tvGroupUpdataDes = findViewById(R.id.tv_GroupUpdataDes);
        //消息通知
        swGroupUpdataMessage = findViewById(R.id.sw_GroupUpdataMessage);
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
        rcCreateGroup = findViewById(R.id.rc_GroupInfoUpdata);

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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_UploadHead:
                if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                    // 请求权限
                    PermissionsActivity.startActivityForResult(this, 11000, AUTHORIMAGE);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
                } else {
                    // 全部权限都已获取
                    setShowViewid(R.id.iv_UploadHead);
                    getChoiceDialog().show();
                }
                break;
            case R.id.ll_GroupUpdataDes:
                Intent intent1 = new Intent(this, GroupInfoDesActivity.class);

                startActivityForResult(intent1, 10000);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 10000:
                    groupUpdataDes = data.getStringExtra("GroupInfoDes");
                    tvGroupUpdataDes.setText(groupUpdataDes);
                    break;
            }
        }
    }
    @Override
    public void uploadImage(List<File> list) {
        super.uploadImage(list);
        int showViewid = getShowViewid();
        try {
            for (File imagepath : list) {
                groupInfoHead = imagepath.getPath();
                switch (showViewid) {
                    case R.id.iv_UploadHead://上传营业执照
                        Glide.with(this)
                                .load(groupInfoHead)
                                .fitCenter()
                                .dontAnimate()
                                .placeholder(R.mipmap.launcher_login)
                                .error(R.mipmap.launcher_login)
                                .into(ivUploadHead);

                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //确定
    public void createGroup() {

        String groupName = etGroupUpdataName.getText().toString().trim();
        if (TextUtils.isEmpty(groupName)) {
            ToastUtil.showToast("群名称不能为空!");
            return;
        }
        if (TextUtils.isEmpty(groupUpdataDes)) {
            ToastUtil.showToast("群简介不能为空!");
            return;
        }
        if (TextUtils.isEmpty(groupInfoHead)) {
            ToastUtil.showToast("群头像不能为空!");
            return;
        }

        String sign = BaseApplication.getInstance().getSign();
        Map<String, String> params = new HashMap<>();
        params.put("id", targetId);//群id
        params.put("name", groupName);//群名称
        params.put("des", groupUpdataDes);//描述

        params.put("avatar", groupInfoHead);//头像
        params.put("sign", sign);
        okHttpPostBody(101, GlobalParam.GROUPUPDATE, params);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100://修改群组信息

                    GroupInfoListModel groupInfoListModel = JSON.parseObject(returnData, GroupInfoListModel.class);
                    List<GroupInfoListModel.DataBean> data = groupInfoListModel.getData();
                    Log.e("TAG_群组修改信息", "data=" + data.size());
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
                case 101:
                    setResult(Activity.RESULT_OK);
                    finish();
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
        page++;
        getData();
    }
}
