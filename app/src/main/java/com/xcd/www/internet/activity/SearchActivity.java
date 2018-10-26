package com.xcd.www.internet.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.SearchAdapter;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.view.RecyclerViewDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.utils.ToastUtil;

public class SearchActivity extends BaseInternetActivity implements TextWatcher,SearchAdapter.OnItemClickListener {

    private EditText etSearch;
    List<ContactModel> contactSearch;//模糊搜索集合
    List<ContactModel> contactInfo;
    private RecyclerView rcSearch;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchAdapter adapter;
    private LinearLayout llBack;
    private ImageView ivClean;
    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        llBack =findViewById(R.id.ll_Back);
        llBack.setOnClickListener(this);
        ivClean =findViewById(R.id.iv_Clean);
        ivClean.setVisibility(View.GONE);
        ivClean.setOnClickListener(this);
        contactInfo = (List<ContactModel>) getIntent().getSerializableExtra("list");
        etSearch = findViewById(R.id.et_Search);
        etSearch.addTextChangedListener(this);
        rcSearch =  findViewById(R.id.rc_Search);
        rcSearch.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcSearch.setLayoutManager(mLinearLayoutManager);
        adapter = new SearchAdapter(this);
        adapter.setOnItemClickListener(this);
        rcSearch.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcSearch.addItemDecoration(recyclerViewDecoration);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_Back:
                finish();
                break;
            case R.id.iv_Clean:
                etSearch.setText("");
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //如果长度为0
        if (charSequence.length() > 0) {
            String trim = etSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)){
                contactSearch = new ArrayList<>();//模糊搜索集合
                for (int j = 0, k = contactInfo.size(); j < k; j++) {
                    ContactModel contactModel = contactInfo.get(j);
                    String name = contactModel.getName();
                    String letters = contactModel.getLetters();
                    if (name.indexOf(trim)!=-1){//匹配文字
                        contactSearch.add(contactModel);
                    }
//                    else {//匹配首字母
//                        String pinyin = PinyinUtils.getPingYin(trim);
//                        String sortString = pinyin.substring(0, 1);
//                        if (letters.equalsIgnoreCase(sortString)){
//                            contactSearch.add(contactModel);
//                        }
//                    }
                }
                ivClean.setVisibility(View.VISIBLE);
                rcSearch.setVisibility(View.VISIBLE);
                adapter.setData(contactSearch);
            }else {
                ivClean.setVisibility(View.GONE);
                rcSearch.setVisibility(View.GONE);
            }
        }else {
            ivClean.setVisibility(View.GONE);
            rcSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    //模糊搜索的adapter
    @Override
    public void onItemClick(View view, int position) {
        ContactModel contactModel = contactSearch.get(position);
        String name = contactModel.getName();
        ToastUtil.showToast(name);
    }
}
