package com.xcd.www.internet.func;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.xcd.www.internet.activity.RedPkgDetailsActivity;

import www.xcd.com.mylibrary.R;
import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


/**
 * Created by Android on 2017/5/15.
 */
public class RedPkgRecordTopBtnFunc extends BaseTopTextViewFunc {


    public RedPkgRecordTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.redpkg_record;
    }
    /** 功能文本 */
    protected String getFuncText() {
        return "红包记录";
    }

    protected int getFuncTextRes() {
        return R.string.redpkg_record;
    }

    @Override
    public View initFuncView(LayoutInflater inflater) {
        View view = super.initFuncView(inflater);
        getTextView().setTextColor(ContextCompat.getColor(getActivity(), R.color.orange_red));
        return view;
    }
    @Override
    public void onclick(View v) {
        ((RedPkgDetailsActivity)getActivity()).startRecord();
    }
}
