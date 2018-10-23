package com.xcd.www.internet.func;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.SettingActivity;

import www.xcd.com.mylibrary.func.BaseTopImageBtnFunc;


public class MeLeftTopBtnFunc extends BaseTopImageBtnFunc {

	public MeLeftTopBtnFunc(Activity activity) {
		super(activity);
	}

	@Override
	public int getFuncId() {
		return R.id.me_left;
	}

	@Override
	public int getFuncIcon() {
		return R.mipmap.setting;
	}

	@Override
	public void onclick(View v) {
		Log.e("TAG_我的","设置");
		getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
	}
}
