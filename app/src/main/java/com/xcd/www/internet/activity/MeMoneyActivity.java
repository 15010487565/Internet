package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.MeMoneyAdapter;
import com.xcd.www.internet.func.MeMoneyRightTopBtnFunc;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class MeMoneyActivity extends SimpleTopbarActivity {

    private RecyclerView rcMeMoney;
    private LinearLayoutManager linearLayoutManager;
    private MeMoneyAdapter adapter;
    private TextView tvBag,tvCash;

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
        Intent intent = getIntent();
        String usdt = intent.getStringExtra("usdt");
        tvBag.setText(usdt);
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
                this, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.black_ee));
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
