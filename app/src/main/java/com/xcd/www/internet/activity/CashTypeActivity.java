package com.xcd.www.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.CashTypeAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.CashTypeModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class CashTypeActivity extends SimpleTopbarActivity implements CashTypeAdapter.OnItemClickListener{

    private RecyclerView rcCashType;
    private LinearLayoutManager mLinearLayoutManager;
    private CashTypeAdapter adapter;
    private TextView tvCashType;
    private List<CashTypeModel> list;
    String sign;
    String cardNum;
    @Override
    protected Object getTopbarTitle() {
        return "红包提现";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        BaseApplication instance = BaseApplication.getInstance();
        cardNum = instance.getCardNum();
        sign = instance.getSign();
        list = new ArrayList<>();

        CashTypeModel cashTypeModel =  new CashTypeModel();
        cashTypeModel.setName("USDT");
        cashTypeModel.setCoin("usdt");
        cashTypeModel.setSelect(true);
        list.add(cashTypeModel);

        CashTypeModel cashTypeModel2 =  new CashTypeModel();
        cashTypeModel2.setName("美金(USD)");
        cashTypeModel2.setCoin("dollar");
        cashTypeModel2.setSelect(false);
        list.add(cashTypeModel2);
        CashTypeModel cashTypeModel3 =  new CashTypeModel();
        cashTypeModel3.setName("人民币");
        cashTypeModel3.setSelect(false);
        cashTypeModel3.setCoin("rmb");
        list.add(cashTypeModel3);
        CashTypeModel cashTypeModel4 =  new CashTypeModel();
        cashTypeModel4.setName("购买okd送lit");
        cashTypeModel4.setCoin("okd");
        cashTypeModel4.setSelect(false);
        list.add(cashTypeModel4);
        adapter.setData(list);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        tvCashType = findViewById(R.id.tv_CashType);
        tvCashType.setOnClickListener(this);
        rcCashType =  findViewById(R.id.rc_CashType);
        rcCashType.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcCashType.setLayoutManager(mLinearLayoutManager);
        adapter = new CashTypeAdapter(this);
        adapter.setOnItemClickListener(this);
        rcCashType.setAdapter(adapter);
//        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
//                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
//        rcCashType.addItemDecoration(recyclerViewDecoration);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_CashType:
                //提现检测

                Map<String, String> params = new HashMap<>();
                params.put("sign", sign);
                okHttpPostBody(100, GlobalParam.CASHCHECK, params);
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200){
            switch (requestCode){
                case 100:
                    String coin = null;
                    for (int i = 0; i < list.size(); i++) {
                        CashTypeModel cashTypeModel = list.get(i);
                        boolean select = cashTypeModel.isSelect();
                        if (select){
                            coin = cashTypeModel.getCoin();
                            break;
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("coin",coin);
                    if ("usdt".equals(coin)){
                        intent.setClass(this, CashInfoUSDTActivity.class);
                    }else if ("okd".equals(coin)){
                        intent.setClass(this, CashInfoOKDActivity.class);
                    }else if ("dollar".equals(coin)){
                        if (TextUtils.isEmpty(cardNum)){
                            ToastUtil.showToast("请先绑定银行卡！");
                            return;
                        }
                        intent.setClass(this, CashInfoDollarActivity.class);
                    }else if ("rmb".equals(coin)){
                        if (TextUtils.isEmpty(cardNum)){
                            ToastUtil.showToast("请先绑定银行卡！");
                            return;
                        }
                        intent.setClass(this, CashInfoRmbActivity.class);
                    }
                    startActivity(intent);
                    finish();
                    break;
            }
        }else {
            ToastUtil.showToast(returnMsg);
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
    public void onItemClick(View view, int position) {

        for (int i = 0; i < list.size(); i++) {
            CashTypeModel cashTypeModel = list.get(i);
            if (position == i){
                cashTypeModel.setSelect(true);
            }else {
                cashTypeModel.setSelect(false);
            }
        }
        adapter.setData(list);
    }
}
