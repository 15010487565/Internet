package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;

import com.xcd.www.internet.activity.InviteFriendBitActivity;

import www.xcd.com.mylibrary.R;
import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


/**
 * Created by Android on 2017/5/15.
 */
public class InviteFriendBitTopBtnFunc extends BaseTopTextViewFunc {


    public InviteFriendBitTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.check_all;
    }
    /** 功能文本 */
    protected String getFuncText() {
        return "全选";
    }

    protected int getFuncTextRes() {
        return R.string.check_all;
    }

    @Override
    public void onclick(View v) {
        ((InviteFriendBitActivity)getActivity()).getInviteFriend();
    }
}
