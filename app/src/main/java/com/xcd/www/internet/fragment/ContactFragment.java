package com.xcd.www.internet.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.SearchActivity;
import com.xcd.www.internet.adapter.SortAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.func.ContactRightTopBtnFunc;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.util.ContactUtil;
import com.xcd.www.internet.util.PinyinComparator;
import com.xcd.www.internet.view.RecyclerViewDecoration;
import com.xcd.www.internet.view.TitleItemDecoration;
import com.xcd.www.internet.view.WaveSideBar;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.activity.PermissionsChecker;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Created by gs on 2018/10/16.
 */

public class ContactFragment extends SimpleTopbarFragment implements SortAdapter.OnItemClickListener{

    private static final String[] AUTHORIMAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.WRITE_CONTACTS
            ,Manifest.permission.READ_CONTACTS
    };
    private PermissionsChecker mChecker ;

    private RecyclerView rcContact;
    private LinearLayoutManager mLinearLayoutManager;
    private WaveSideBar mSideBar;
    private PinyinComparator mComparator;
    private SortAdapter mAdapter;
   private RelativeLayout reSearch;
    List<ContactModel> contactInfo;//联系人数据

    private static Class<?> rightFuncArray[] = {ContactRightTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        initRecyclerView(view);
        reSearch = view.findViewById(R.id.re_Search);
        reSearch.setOnClickListener(this);
        try {
            mChecker = new PermissionsChecker(getActivity());
            if (mChecker.lacksPermissions(AUTHORIMAGE)) {
                // 请求权限
                PermissionsActivity.startActivityForResult(getActivity(),11000,AUTHORIMAGE);
//                    ActivityCompat.requestPermissions(this, BaseActivity.WRITEREADPERMISSIONS, 11000);
            } else {
                // 全部权限都已获取
                initContactData();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initRecyclerView(View view) {

        rcContact =  view.findViewById(R.id.rc_Contact);
        mComparator = new PinyinComparator();
        mSideBar =  view.findViewById(R.id.sideBar);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcContact.setLayoutManager(mLinearLayoutManager);
        mAdapter = new SortAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        rcContact.setAdapter(mAdapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                getActivity(), LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcContact.addItemDecoration(recyclerViewDecoration);
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.re_Search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("list", (Serializable)contactInfo);
                startActivity(intent);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 11000:
                if (hasAllPermissionsGranted(grantResults)){
                    initContactData();
                }else {
                    ToastUtil.showToast("请在应用管理中打开“相机”访问权限！");
                }
                break;
        }
    }
    
    private void initContactData() {
        try {
            ContactUtil contactUtil = new ContactUtil(getActivity());
            List<ContactModel> contactInfo = contactUtil.getContactInfo();
            Iterator it=contactInfo.iterator();
            while(it.hasNext()){
                ContactModel contactModel = (ContactModel) it.next();
                String mobile = contactModel.getMobile();
                if(TextUtils.isEmpty(mobile)||"null".equals(mobile)){
                    it.remove();
                }
            }
            this.contactInfo = contactInfo;
            // 根据a-z进行排序源数据
            Collections.sort(contactInfo, mComparator);
            mAdapter.setData(contactInfo);
            //如果add两个，那么按照先后顺序，依次渲染。
            TitleItemDecoration mDecoration = new TitleItemDecoration(getActivity(), contactInfo);
            rcContact.addItemDecoration(mDecoration);
            rcContact.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            //保存到Application中

            BaseApplication.getInstance().setListApp(contactInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
//        for (int i = 0; i < mDateList.size(); i++) {
//            SortModel sortModel = mDateList.get(i);
//            if (i == position) {
//                carTypeId = sortModel.getCarTypeId();
//                sortModel.setChecked(true);
//                cartype = sortModel.getName();
//                sortModel.getLetters();
//                seclectName.setText(cartype);
//            } else {
//                sortModel.setChecked(false);
//            }
//        }
//        mAdapter.notifyDataSetChanged();
    }

}
