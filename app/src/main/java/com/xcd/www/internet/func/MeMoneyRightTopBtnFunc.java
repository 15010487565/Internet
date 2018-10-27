package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.MeMoneyActivity;

import www.xcd.com.mylibrary.func.BaseTopImageBtnFunc;


public class MeMoneyRightTopBtnFunc extends BaseTopImageBtnFunc {

    public MeMoneyRightTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.home_right;
    }

    @Override
    public int getFuncIcon() {
        return R.mipmap.home_right;
    }

    @Override
    public void onclick(View v) {
        ( (MeMoneyActivity)getActivity()).addMeMoney();
    }
}
