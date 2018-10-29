package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.CreateGroupAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.CreateGroupTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.model.CreateGroupModel;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class GroupCreateActivity extends PhotoActivity implements CreateGroupAdapter.OnItemClickListener{

    private ImageView ivUploadHead;
    private List<ContactModel> createGroupNextList;
    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateGroupAdapter adapter;
    private EditText etGroupName, etGroupDes;
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

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //群名称
        etGroupName = findViewById(R.id.et_GroupName);
        //描述
        etGroupDes = findViewById(R.id.et_GroupDes);
        //头像
        ivUploadHead = findViewById(R.id.iv_UploadHead);
        ivUploadHead.setOnClickListener(this);
        //邀请人集合
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
        if (createGroupNextList !=null){
            Log.e("TAG_创建","list="+createGroupNextList.toString());
        }
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
    String  headUrl;
    @Override
    public void uploadImage(List<File> list) {
        super.uploadImage(list);
        int showViewid = getShowViewid();
        try {
            for (File imagepath : list) {
                headUrl = imagepath.getPath();
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

        String groupName = etGroupName.getText().toString().trim();
        if (TextUtils.isEmpty(groupName)){
            ToastUtil.showToast("群名称不能为空!");
            return;
        }
        String groupDes = etGroupDes.getText().toString().trim();
        if (TextUtils.isEmpty(groupDes)){
            ToastUtil.showToast("群名称不能为空!");
            return;
        }
        if (TextUtils.isEmpty(headUrl)){
            ToastUtil.showToast("群头像不能为空!");
            return;
        }
        long id = BaseApplication.getInstance().getId();
        StringBuffer sb = new StringBuffer();
        if (createGroupNextList.size()<=0){
            sb.append(34);
        }else {
            sb.append(id);
            sb.append(",");
            for (int i = 0,j =createGroupNextList.size(); i < j ; i++) {
                ContactModel contactModel = createGroupNextList.get(i);
                String mobile = contactModel.getMobile();
                sb.append(mobile);
                if (i!=j-1){
                    sb.append(",");
                }
            }
        }
        String sign = BaseApplication.getInstance().getSign();
        Map<String, String> params = new HashMap<>();
        params.put("name", groupName);//群名称
        params.put("des", groupDes);//描述
        params.put("ids", sb.toString());//群成员（id逗号分隔）
        params.put("avatar", headUrl);//头像
        params.put("sign", sign);
        okHttpPostBody(100, GlobalParam.CREATEGROUP, params);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        switch (requestCode) {
            case 100://注册
                if (returnCode == 200){
                    CreateGroupModel createGroupModel = JSON.parseObject(returnData, CreateGroupModel.class);
                    CreateGroupModel.DataBean data = createGroupModel.getData();
                    String targetGroupId = String.valueOf(data.getId());
                    String title = data.getName();
                    RongIM.getInstance().startGroupChat(this,targetGroupId , title);
                    finish();
//                    Map<String, String> params = new HashMap<>();
//                    params.put("id ", targetGroupId);//群名称
//                    params.put("friend ", friend );//描述
//                    okHttpPostBody(100, GlobalParam.CREATEGROUP, params);
                }else {
                    ToastUtil.showToast(returnMsg);
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

    @Override
    public void onItemDeleteClick(View view, final int position) {
        DialogUtil.getInstance()
                .setContext(this)
                .setCancelable(true)
                .title("温馨提示")
                .message("您确定要删除这位即将进群的好友？")
                .sureText("确定")
                .cancelText("取消")
                .setSureOnClickListener(new DialogUtil.OnClickListener() {
                    @Override
                    public void onClick(View view,String message) {
                        createGroupNextList.remove(position);
                        adapter.setData(createGroupNextList);
                    }
                }).showDefaultDialog();

    }
}
