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
import com.xcd.www.internet.sq.BlackDao;
import com.xcd.www.internet.ui.RecyclerViewDecoration;
import com.xcd.www.internet.util.ReadImgToBinary;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;
import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.utils.key.Base64_;
import www.xcd.com.mylibrary.utils.key.RSAUtils;

public class GroupCreateActivity extends PhotoActivity implements CreateGroupAdapter.OnItemClickListener {

    private ImageView ivUploadHead;
    private List<ContactModel> createGroupNextList;
    private RecyclerView rcCreateGroup;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateGroupAdapter adapter;
    private EditText etGroupName, etGroupDes;
    String headportrait;
    String sign;
    //    String avatar;
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
        sign = BaseApplication.getInstance().getSign();
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
        rcCreateGroup = findViewById(R.id.rc_CreateGroup);
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

        adapter.setData(createGroupNextList);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_UploadHead:
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
        }
    }


    @Override
    public void uploadImage(List<File> list) {
        super.uploadImage(list);
        int showViewid = getShowViewid();
        try {
            for (File imagepath : list) {
                final String headUrl = imagepath.getPath();
                switch (showViewid) {
                    case R.id.iv_UploadHead:

                        if (headUrl != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //将图片转化为字符串
                                    String proveFile = ReadImgToBinary.imgToBase64(headUrl);

                                    Map<String, String> params = new HashMap<>();
                                    if (!headUrl.matches("^image/(jpg)$")) {
                                        byte[] en_result = RSAUtils.encryptByPublicKey("jpg".getBytes(), RSAUtils.getPublicKey());
                                        String keyStr = Base64_.encode(en_result);
                                        params.put("type", keyStr);
                                    } else if (!headUrl.matches("^image/(png)$")) {
                                        byte[] en_result = RSAUtils.encryptByPublicKey("png".getBytes(), RSAUtils.getPublicKey());
                                        String keyStr = Base64_.encode(en_result);
                                        params.put("type", keyStr);
                                    }
                                    params.put("data", proveFile);

                                    long time = System.currentTimeMillis();//获取系统时间的10位的时间戳
                                    String date = String.valueOf(time);
                                    byte[] dateResult = RSAUtils.encryptByPublicKey(date.getBytes(), RSAUtils.getPublicKey());
                                    String dateStr = Base64_.encode(dateResult);
                                    params.put("date", dateStr);

                                    byte[] signResult = RSAUtils.encryptByPublicKey(sign.getBytes(), RSAUtils.getPublicKey());
                                    String signStr = Base64_.encode(signResult);
                                    params.put("sign", signStr);

                                    okHttpPostBodyImage(101, GlobalParam.UPLOADIMG, params);
                                }
                            });
                        }

                        break;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //确定
    public void createGroup() {

        String groupName = etGroupName.getText().toString().trim();
        if (TextUtils.isEmpty(groupName)) {
            ToastUtil.showToast("群名称不能为空!");
            return;
        }
        String groupDes = etGroupDes.getText().toString().trim();
        if (TextUtils.isEmpty(groupDes)) {
            ToastUtil.showToast("群名称不能为空!");
            return;
        }
        if (TextUtils.isEmpty(headportrait)) {
            ToastUtil.showToast("群头像不能为空!");
            return;
        }
        if (createGroupNextList == null || createGroupNextList.size() == 0) {
            ToastUtil.showToast("单人不能创建群组");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, j = createGroupNextList.size(); i < j; i++) {
            ContactModel contactModel = createGroupNextList.get(i);
            String userId = contactModel.getUserId();
            sb.append(userId);
            if (i != j - 1) {
                sb.append(",");
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("name", groupName);//群名称
        params.put("des", groupDes);//描述
        params.put("ids", sb.toString());//群成员（id逗号分隔）
        params.put("avatar", headportrait);//头像
        params.put("sign", sign);
        okHttpPostBody(100, GlobalParam.CREATEGROUP, params);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        switch (requestCode) {
            case 100:
                if (returnCode == 200) {
                    CreateGroupModel createGroupModel = JSON.parseObject(returnData, CreateGroupModel.class);
                    CreateGroupModel.DataBean data = createGroupModel.getData();
                    String targetGroupId = String.valueOf(data.getId());
                    String title = data.getName();

                    BaseApplication instance = BaseApplication.getInstance();
                    String nick = instance.getNick();
                    MessageContent content = InformationNotificationMessage.obtain(
                            nick + "创建了一个群组"
                    );
                    RongIM.getInstance().insertIncomingMessage(
                            Conversation.ConversationType.GROUP,
                            targetGroupId,
                            String.valueOf(instance.getId()),
                            new Message.ReceivedStatus(1),
                            content,
                            new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    Log.e("TAG_发送小灰条", "成功=" + message.getContent());
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    Log.e("TAG_发送小灰条", "失败=" + errorCode.getMessage());
                                }
                            }
                    );
                    RongIM.getInstance().startGroupChat(this, targetGroupId, title);
                    finish();
                    BlackDao blackDao = BlackDao.getInstance(this);
                    blackDao.updateBlackNumMode(targetGroupId,title,title,headportrait);
                } else {
                    ToastUtil.showToast(returnMsg);
                }
                break;
            case 101:
                headportrait = returnData;
                Glide.with(this)
                        .load(headportrait)
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.mipmap.photo)
                        .error(R.mipmap.photo)
                        .into(ivUploadHead);
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
                    public void onClick(View view, String message) {
                        createGroupNextList.remove(position);
                        adapter.setData(createGroupNextList);
                    }
                })
                .setCancelOnClickListener(new DialogUtil.OnClickListener() {

                    @Override
                    public void onClick(View view, String message) {

                    }
                })
                .showDefaultDialog();

    }
}
