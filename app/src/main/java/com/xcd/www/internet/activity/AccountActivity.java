package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.LoginInfoModel;
import com.xcd.www.internet.util.EventBusMsg;
import com.xcd.www.internet.util.ReadImgToBinary;
import com.xcd.www.internet.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

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
import www.xcd.com.mylibrary.utils.key.Base64_;
import www.xcd.com.mylibrary.utils.key.RSAUtils;


public class AccountActivity extends PhotoActivity {

	private CircleImageView ivAccountHead;
	private TextView tvAccountNick,tvAccountPhone, tvAccountName, tvAccountHead;
	private LinearLayout llAccountCard, llAccountBank, llAccountCode;
	String sign;
	String headportrait;
	String nick;
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
		headportrait = instance.getHeadportrait();

		nick = instance.getNick();
		tvAccountNick.setText(TextUtils.isEmpty(nick)?"":nick);
		String account = instance.getAccount();
		tvAccountPhone.setText(TextUtils.isEmpty(account)?"":account);
		String name = instance.getName();
		tvAccountName.setText(TextUtils.isEmpty(name)?"":name);

		if (TextUtils.isEmpty(headportrait)){
			tvAccountHead.setText(TextUtils.isEmpty(nick)?"":nick.substring(0));
		}else {
			tvAccountHead.setText("");
			Glide.with(this)
					.load(headportrait)
					.fitCenter()
					.dontAnimate()
					.transform(new GlideCircleTransform(this))
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.placeholder(R.mipmap.launcher_login)
					.error(R.mipmap.launcher_login)
					.into(ivAccountHead);
		}

	}

	@Override
	protected void afterSetContentView() {
		super.afterSetContentView();
		ivAccountHead = findViewById(R.id.iv_AccountHead);
		ivAccountHead.setOnClickListener(this);
		tvAccountHead = findViewById(R.id.tv_AccountHead);
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
		//
		llAccountCode = findViewById(R.id.ll_AccountCode);
		llAccountCode.setOnClickListener(this);
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

								Map<String, String> params = new HashMap<>();
								params.put("nick", message);
								params.put("sign", sign);
								okHttpPostBody(103, GlobalParam.ACCOUNTUPDATE, params);
							}
						})
						.setCancelOnClickListener(new DialogUtil.OnClickListener(){

							@Override
							public void onClick(View view, String message) {

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
			case R.id.ll_AccountCode:
				Intent intent1 = new Intent(this, AccountCodeActivity.class);
				intent1.putExtra("AccountInfoHead", headportrait);
				intent1.putExtra("AccountInfoName", nick);
				startActivity(intent1);
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
					case R.id.iv_UploadHead://上传营业执照

						if (headUrl != null) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//将图片转化为字符串
									String proveFile = ReadImgToBinary.imgToBase64(headUrl);

									Map<String, String> params = new HashMap<>();
									if (!headUrl.matches("^image/(jpg)$")){
										byte[] en_result = RSAUtils.encryptByPublicKey("jpg".getBytes(), RSAUtils.getPublicKey());
										String keyStr = Base64_.encode(en_result);
										params.put("type", keyStr );
									}else if (!headUrl.matches("^image/(png)$")){
										byte[] en_result = RSAUtils.encryptByPublicKey("png".getBytes(), RSAUtils.getPublicKey());
										String keyStr = Base64_.encode(en_result);
										params.put("type", keyStr );
									}
									params.put("data", proveFile );


									long time=System.currentTimeMillis();//获取系统时间的10位的时间戳
									String date =String.valueOf(time);
									Log.e("TAG_系统时间","date="+date);
									byte[] dateResult = RSAUtils.encryptByPublicKey(date.getBytes(), RSAUtils.getPublicKey());
									String dateStr = Base64_.encode(dateResult);
									params.put("date", dateStr );

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

	@Override
	public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
		if (returnCode == 200){
			switch (requestCode){
				case 100:
					LoginInfoModel loginIndoModel = JSON.parseObject(returnData, LoginInfoModel.class);
					LoginInfoModel.DataBean data = loginIndoModel.getData();
					headportrait = data.getHeadportrait();
					Glide.with(this)
							.load(headportrait)
							.fitCenter()
							.dontAnimate()
							.placeholder(R.mipmap.photo)
							.error(R.mipmap.photo)
							.into(ivAccountHead);
					BaseApplication.getInstance().setHeadportrait(headportrait);
					EventBusMsg msg = new EventBusMsg("RefreshHeadportrait");
					msg.setMsgCon(headportrait);
					EventBus.getDefault().post(msg);
					ToastUtil.showToast(returnMsg);
					break;
				case 101:
//					ImageModel imageModel = JSON.parseObject(returnData, ImageModel.class);
//					String data = imageModel.getData();
					Map<String, String> params = new HashMap<>();
					params.put("Headportrait", returnData);
					params.put("sign", sign);
					okHttpPostBody(100, GlobalParam.ACCOUNTUPDATE, params);
					break;
				case 103:
					LoginInfoModel loginIndo = JSON.parseObject(returnData, LoginInfoModel.class);
					LoginInfoModel.DataBean dataNick = loginIndo.getData();
					String nick = dataNick.getNick();

					BaseApplication.getInstance().setNick(nick);
					tvAccountNick.setText(nick);

					EventBusMsg msgNick = new EventBusMsg("RefreshNick");
					msgNick.setMsgCon(nick);
					EventBus.getDefault().post(msgNick);
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
