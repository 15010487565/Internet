package com.xcd.www.internet.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.base.SimpleTopbarFragment;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gs on 2018/10/16.
 */

public class FindFragment extends SimpleTopbarFragment {
    @Override
    protected Object getTopbarTitle() {
        return R.string.find;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.find;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        WebView wb_Find = view.findViewById(R.id.wb_Find);
        wb_Find.loadUrl("http://www.quantusd.com/");
    }
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

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
