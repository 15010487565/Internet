package com.xcd.www.internet.func;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.WebViewActivity;
import com.xcd.www.internet.application.BaseApplication;

import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.utils.key.AESUtils;

/**
 * 排行榜
 *
 */
public class MeRankingFunc extends BaseFunc {

	public MeRankingFunc(Activity activity) {
		super(activity);
	}

	@Override
	public int getFuncId() {
		return R.id.me_ranking;
	}

	@Override
	public int getFuncIcon() {
		return R.mipmap.me_ranking;
	}

	@Override
	public int getFuncName() {
		return R.string.me_ranking;
	}

	@Override
	public void onclick() {
		try {
			long id = BaseApplication.getInstance().getId();
			String encrypt = AESUtils.getInstance().encrypt(String.valueOf(id));
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra("Url", GlobalParam.RANKING+encrypt);
			getActivity().startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public View initFuncView(boolean isSeparator, Object... params) {
		View funcView = super.initFuncView(false, params);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		params2.topMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin_15);
		params2.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin_20);
		params2.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.me_vertical_margin_20);
		funcView.setLayoutParams(params2);
		return funcView;
	}
}
