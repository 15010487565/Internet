package com.xcd.www.internet.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.CreateGroupAdapter;
import com.xcd.www.internet.func.CreateGroupTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class CreateGroupActivity extends PhotoActivity implements CreateGroupAdapter.OnItemClickListener{

    private ImageView ivUploadHead;
    private static final String[] AUTHORIMAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.CAMERA
    };
    private PermissionsChecker mChecker ;
    private List<ContactModel> createGroupNextList;
    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateGroupAdapter adapter;
    private EditText etGroupName;
    private static Class<?> rightFuncArray[] = {CreateGroupTopBtnFunc.class};
    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return R.string.home_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mChecker = new PermissionsChecker(this);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        etGroupName = findViewById(R.id.et_GroupName);

        ivUploadHead = findViewById(R.id.iv_UploadHead);
        ivUploadHead.setOnClickListener(this);

        rcCreateGroup =  findViewById(R.id.rc_CreateGroup);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcCreateGroup.setLayoutManager(mLinearLayoutManager);
        adapter = new CreateGroupAdapter(this);
        adapter.setOnItemClickListener(this);
        rcCreateGroup.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcCreateGroup.addItemDecoration(recyclerViewDecoration);
        //实例化数据
        createGroupNextList = (List<ContactModel>) getIntent().getSerializableExtra("createGroupNextList");
        Log.e("TAG_创建","list="+createGroupNextList.size());
        adapter.setData(createGroupNextList);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_UploadHead:
                if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                    // 请求权限
                    PermissionsActivity.startActivityForResult(this,11000,AUTHORIMAGE);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
                } else {
                    // 全部权限都已获取
                    setShowViewid(R.id.iv_UploadHead);
                    getChoiceDialog().show();
                }
                break;
        }
    }

    @Override
    public void uploadImage(List<File> list) {
        super.uploadImage(list);
        int showViewid = getShowViewid();
        try {
            for (File imagepath : list) {
                String  headUrl = imagepath.getPath();
                switch (showViewid) {
                    case R.id.iv_UploadHead://上传营业执照
                        Glide.with(this)
                                .load(headUrl)
                                .fitCenter()
                                .dontAnimate()
                                .placeholder(R.mipmap.photo)
                                .error(R.mipmap.photo)
                                .into(ivUploadHead);

                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //确定
    public void createGroup(){
        ToastUtil.showToast("点击创建群组");
        String trim = etGroupName.getText().toString().trim();
        RongIM.getInstance().startGroupChat(this,trim , trim);
//        Map<String, String> params = new HashMap<>();
//
//        params.put("userId", userId);
//        params.put("groupId", groupId);
//        params.put("groupName", groupName);
//        okHttpPost(100, "http://api.cn.ronghub.com/group/create.json", params);
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
    public void onItemDeleteClick(View view, final int position) {
        DialogUtil.getInstance()
                .setContext(this)
                .setCancelable(true)
                .title("温馨提示")
                .message("您确定要删除这位即将进群的好友？")
                .sureText("确定")
                .setSureOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createGroupNextList.remove(position);
                        adapter.setData(createGroupNextList);
                    }
                }).showDefaultDialog();

    }
}
