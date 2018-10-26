package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;

import com.xcd.www.internet.activity.GroupInfoActivity;

import www.xcd.com.mylibrary.R;
import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


/**
 * Created by Android on 2017/5/15.
 */
public class GroupInfoTopBtnFunc extends BaseTopTextViewFunc {


    public GroupInfoTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.editor;
    }
    /** 功能文本 */
    protected String getFuncText() {
        return "编辑";
    }

    protected int getFuncTextRes() {
        return R.string.editor;
    }

    @Override
    public void onclick(View v) {
        ((GroupInfoActivity)getActivity()).getEditorGroupInfo();
    }
}
