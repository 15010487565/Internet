package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;

import com.xcd.www.internet.activity.GroupCreateActivity;

import www.xcd.com.mylibrary.R;
import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


/**
 * Created by Android on 2017/5/15.
 */
public class CreateGroupTopBtnFunc extends BaseTopTextViewFunc {


    public CreateGroupTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.confirm;
    }
    /** 功能文本 */
    protected String getFuncText() {
        return "确定";
    }

    protected int getFuncTextRes() {
        return R.string.confirm;
    }

    @Override
    public void onclick(View v) {
        ((GroupCreateActivity)getActivity()).createGroup();
    }
}
