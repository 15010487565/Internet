package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.xcd.www.internet.R;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * 关于传智
 *
 */
public class MeAboutFunc extends BaseFunc {

	public MeAboutFunc(Activity activity) {
		super(activity);
	}

	@Override
	public int getFuncId() {
		return R.id.me_about;
	}

	@Override
	public int getFuncIcon() {
		return R.mipmap.me_about;
	}

	@Override
	public int getFuncName() {
		return R.string.me_about;
	}

	@Override
	public void onclick() {
//		getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
	}
	@Override
	public View initFuncView(boolean isSeparator, Object... params) {
		View funcView = super.initFuncView(false, params);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		params2.topMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin);
		params2.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin_20);
		params2.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin_20);
		funcView.setLayoutParams(params2);
		return funcView;
	}
}
