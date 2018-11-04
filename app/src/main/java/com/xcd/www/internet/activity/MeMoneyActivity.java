package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.MeMoneyAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.func.MeMoneyRightTopBtnFunc;
import com.xcd.www.internet.model.MeBagModel;
import com.xcd.www.internet.model.MeBagTypeModel;
import com.xcd.www.internet.ui.RecyclerViewDecoration;
import com.xcd.www.internet.util.EventBusMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;

public class MeMoneyActivity extends SimpleTopbarActivity {

    private RecyclerView rcMeMoney;
    private LinearLayoutManager linearLayoutManager;
    private MeMoneyAdapter adapter;
    private TextView tvBag,tvCash;
    String sign;
    private static Class<?> rightFuncArray[] = {MeMoneyRightTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "资产";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_money);
        EventBus.getDefault().register(this);
        sign = BaseApplication.getInstance().getSign();
        Map<String, String> params = new HashMap<>();
        params.put("sign", sign);
        okHttpPostBody(100, GlobalParam.MEBAG, params);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        //提现按钮
        initRecyclerView();
        tvCash = findViewById(R.id.tv_Cash);
        tvCash.setOnClickListener(this);
        //总资产
        tvBag = findViewById(R.id.tv_Bag);
    }
    private void initRecyclerView() {
        //初始化tabRecyclerView
        rcMeMoney = findViewById(R.id.rc_MeMoney);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcMeMoney.setLayoutManager(linearLayoutManager);
        //创建Adapter
        adapter = new MeMoneyAdapter(this);
//        adapter.setOnItemClickListener(this);
//        rcMeMoney.setLoadingMoreEnabled(false);
        //rc线
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.black_ee));
        rcMeMoney.addItemDecoration(recyclerViewDecoration);
        rcMeMoney.setAdapter(adapter);

    }
    //添加资产
    public void addMeMoney(){

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_Cash:
                startActivity(new Intent(this,CashTypeActivity.class));
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
                case 100:
                    try {
                        MeBagModel mebagModel = JSON.parseObject(returnData, MeBagModel.class);
                        MeBagModel.DataBean data = mebagModel.getData();
                        double usdt = data.getUsdt();
                        BaseApplication instance = BaseApplication.getInstance();
                        instance.setUsdt(String.valueOf(usdt));
                        tvBag.setText("$"+String.valueOf(usdt));
                        List<MeBagTypeModel> list = new ArrayList<>();
                        MeBagTypeModel meBag = new MeBagTypeModel();
                        meBag.setName("usdt");
                        meBag.setNum(String.valueOf(usdt));
                        String usdt_dollar = BaseApplication.getInstance().getUsdt_dollar();
                        meBag.setPrice(usdt_dollar);
                        meBag.setAllPrice("≈$"+String.valueOf(usdt));
                        list.add(meBag);
                        adapter.setData(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusMsg event) {
        String msg = event.getMsg();
        if ("RefreshBag".equals(msg)) {
            Map<String, String> params = new HashMap<>();
            params.put("sign", sign);
            okHttpPostBody(100, GlobalParam.MEBAG, params);
        }
    }
}
