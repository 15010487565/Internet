package com.xcd.www.internet.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.func.AddFriendTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.util.EventBusMsg;
import com.xcd.www.internet.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;

import static com.xcd.www.internet.R.id.et_AddFriendPhone;

public class AddFriendActivity extends PhotoActivity implements TextWatcher{

    private CircleImageView ivAddFriendTopHead;
    private EditText etAddFriendName, etAddFriendName1, etAddFriendPhone;
    private TextView tvAddFriendlogo;
    private List<ContactModel> friendList;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.DISABLE_KEYGUARD,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.READ_CONTACTS,
    };
    private static final int PERMISSON_REQUESTCODE = 10001;

    private static Class<?> rightFuncArray[] = {AddFriendTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "添加联系人";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
//        ivAddFriendTopHead = findViewById(R.id.iv_AddFriendTopHead);
        etAddFriendName = findViewById(R.id.et_AddFriendName);
        etAddFriendName.addTextChangedListener(this);
        etAddFriendName1 = findViewById(R.id.et_AddFriendName1);
        etAddFriendPhone = findViewById(et_AddFriendPhone);

        tvAddFriendlogo = findViewById(R.id.tv_AddFriendlogo);

    }

    public void addFriend() {
        if (mChecker.lacksPermissions(needPermissions)) {
            // 请求权限
            PermissionsActivity.startActivityForResult(this, PERMISSON_REQUESTCODE, needPermissions);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
        } else {
            // 全部权限都已获取
            String name = etAddFriendName.getText().toString().trim();
            String name1 = etAddFriendName1.getText().toString().trim();
            String phone = etAddFriendPhone.getText().toString().trim();
            if ((!TextUtils.isEmpty(name)||!TextUtils.isEmpty(name1)) && !TextUtils.isEmpty(phone)) {
                addContact(name+name1, phone);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSON_REQUESTCODE:
                if (hasAllPermissionsGranted(grantResults)){
                    getChoiceDialog().show();
                }
                break;
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

    public void addContact(String name, String phoneNum) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        getContentResolver().insert(Data.CONTENT_URI, values);
        values.clear();

        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(Phone.NUMBER, phoneNum);
        // 电话类型
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        getContentResolver().insert(Data.CONTENT_URI, values);
        values.clear();

        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
        // 联系人的Email地址
        values.put(Email.DATA, "zhangphil@xxx.com");
        // 电子邮件的类型
        values.put(Email.TYPE, Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        getContentResolver().insert(Data.CONTENT_URI, values);
        EventBusMsg msg = new EventBusMsg("RefreshContact");
        EventBus.getDefault().post(msg);
        finish();

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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String name = etAddFriendName.getText().toString().trim();
        String name1 = etAddFriendName1.getText().toString().trim();
        if (!TextUtils.isEmpty(name)&&name.length()>=1){
            String sortString = name.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            tvAddFriendlogo.setText(sortString);
        }else  if (!TextUtils.isEmpty(name1)&&name1.length()>=1){
            String sortString = name1.substring(0, 1).toUpperCase();
            tvAddFriendlogo.setText(sortString);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
