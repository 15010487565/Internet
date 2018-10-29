package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;

import com.xcd.www.internet.activity.SettingUpdataPayCodeActivity;

import www.xcd.com.mylibrary.R;
import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


/**
 * Created by Android on 2017/5/15.
 */
public class SettingUpdataPayNextTopBtnFunc extends BaseTopTextViewFunc {


    public SettingUpdataPayNextTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.next;
    }
    /** 功能文本 */
    protected String getFuncText() {
        return "下一步";
    }

    protected int getFuncTextRes() {
        return R.string.next;
    }

    @Override
    public void onclick(View v) {
        ((SettingUpdataPayCodeActivity)getActivity()).updataPay();

    }
}
