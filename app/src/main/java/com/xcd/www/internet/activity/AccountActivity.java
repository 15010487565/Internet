package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.view.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.DialogUtil;
import www.xcd.com.mylibrary.utils.GlideCircleTransform;
import www.xcd.com.mylibrary.utils.ToastUtil;


public class AccountActivity extends PhotoActivity {

	private CircleImageView ivAccountHead;
	private TextView tvAccountNick,tvAccountPhone, tvAccountName;
	private LinearLayout llAccountCard, llAccountBank;
	String sign;
	@Override
	protected Object getTopbarTitle() {
		return "个人信息";
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_account);
		BaseApplication instance = BaseApplication.getInstance();
		sign = instance.getSign();
		String headportrait = instance.getHeadportrait();

		Glide.with(this)
				.load(headportrait)
				.fitCenter()
				.dontAnimate()
				.transform(new GlideCircleTransform(this))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.placeholder(R.mipmap.launcher_login)
				.error(R.mipmap.launcher_login)
				.into(ivAccountHead);
		String nick = instance.getNick();
		tvAccountNick.setText(TextUtils.isEmpty(nick)?"":nick);
		String account = instance.getAccount();
		tvAccountPhone.setText(TextUtils.isEmpty(account)?"":account);
		String name = instance.getName();
		tvAccountName.setText(TextUtils.isEmpty(name)?"":name);
	}

	@Override
	protected void afterSetContentView() {
		super.afterSetContentView();
		ivAccountHead = findViewById(R.id.iv_AccountHead);
//		ivAccountHead.setOnClickListener(this);
		tvAccountNick = findViewById(R.id.tv_AccountNick);
		tvAccountNick.setOnClickListener(this);
		tvAccountPhone = findViewById(R.id.tv_AccountPhone);
		tvAccountName = findViewById(R.id.tv_AccountName);
		//身份认证
		llAccountCard = findViewById(R.id.ll_AccountCard);
		llAccountCard.setOnClickListener(this);
		//银行卡绑定
		llAccountBank = findViewById(R.id.ll_AccountBank);
		llAccountBank.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.tv_AccountNick:
				DialogUtil.getInstance()
						.setContext(this)
						.setCancelable(true)
						.title("修改昵称")
						.hint("请输入昵称")
						.sureText("确定")
						.cancelText("取消")
						.setInputType(InputType.TYPE_CLASS_TEXT)
						.setSureOnClickListener(new DialogUtil.OnClickListener() {
							@Override
							public void onClick(View view, String message) {
								tvAccountNick.setText(message);
								Map<String, String> params = new HashMap<>();
								params.put("nick ", message);
								params.put("sign", sign);
								okHttpPostBody(100, GlobalParam.ACCOUNTUPDATE, params);
							}
						}).showEditDialog();
				break;

			case R.id.iv_AccountHead:
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
			case R.id.ll_AccountCard:
				startActivity(new Intent(this,AuthorImageNextActivity.class));
				break;
			case R.id.ll_AccountBank:
				startActivity(new Intent(this,AuthorBankActivity.class));
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
								.into(ivAccountHead);

						break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
		if (returnCode == 200){
			switch (requestCode){
				case 100:
					ToastUtil.showToast(returnMsg);
					break;
			}
		}else {
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
}
