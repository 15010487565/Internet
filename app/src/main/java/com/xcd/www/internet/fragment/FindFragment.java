package com.xcd.www.internet.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.xcd.www.internet.OnRcItemClickListener;
import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.WebViewActivity;
import com.xcd.www.internet.adapter.FindAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.model.FindBannerModel;
import com.xcd.www.internet.model.FindListModel;
import com.xcd.www.internet.model.FindRcModel;
import com.xcd.www.internet.ui.RecyclerViewDecoration;
import com.xcd.www.internet.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Created by gs on 2018/10/16.
 */

public class FindFragment extends SimpleTopbarFragment implements
        OnRcItemClickListener, OnBannerListener
        , SwipeRefreshLayout.OnRefreshListener{

    private FindAdapter adapter;
    private Banner bannerFind;
    private RecyclerView rcFind;
    private LinearLayoutManager mLinearLayoutManager;
//    private MultiSwipeRefreshLayout findSwLayout;
    //轮播
    List<FindBannerModel> listBanner;
    //正文列表
    List<FindListModel> listBody;
    String sign;
    int width1;
    @Override
    protected Object getTopbarTitle() {
        return "咨询";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        sign = BaseApplication.getInstance().getSign();
        bannerFind = view.findViewById(R.id.banner_Find);
        //开始轮播
        bannerFind.startAutoPlay();
//        initSwipeRefreshLayout(view);
        initViewPagerImage();
        initRecyclerView(view);
        Map<String, String> map = new HashMap<>();
        map.put("sign", sign);
        okHttpPostBody(100, GlobalParam.FINDLIST, map);
    }
//    private void initSwipeRefreshLayout(View view) {
//        findSwLayout = view.findViewById(R.id.swipe_layout);
////        findSwLayout.setOnRefreshListener(this);
//        //禁止下拉
////        findSwLayout.setEnabled(false);
//        //下拉刷新监听
//        findSwLayout.setOnRefreshListener(this);
//        //设置样式刷新显示的位置
//        findSwLayout.setProgressViewOffset(true, -20, 100);
//        findSwLayout.setColorSchemeResources(R.color.red, R.color.orange, R.color.blue, R.color.black);
//
//    }
    private void initRecyclerView(View view) {
        //初始化tabRecyclerView
        rcFind = view.findViewById(R.id.rc_Find);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcFind.setLayoutManager(mLinearLayoutManager);
        //创建Adapter
        adapter = new FindAdapter(getActivity(),width1);
        adapter.setOnItemClickListener(this);

        rcFind.setAdapter(adapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                getActivity(), LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.black_ee));
        rcFind.addItemDecoration(recyclerViewDecoration);
    }

    private void initViewPagerImage() {
        WindowManager wm = getActivity().getWindowManager();//获取屏幕宽高
        width1 = wm.getDefaultDisplay().getWidth();
//        int height1 = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams para = bannerFind.getLayoutParams();//获取drawerlayout的布局
        para.height = width1 * 294 / 711;//修改宽度
//        para.height = height1;//修改高度
        bannerFind.setLayoutParams(para); //设置修改后的布局。

    }

    //轮播图点击事件
    @Override
    public void OnBannerClick(int position) {
        FindBannerModel findBannerModel = listBanner.get(position);
        String url = findBannerModel.getUrl();
        startWebView(url);
    }

    private void startWebView(String url) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("isShowTopBar",true);
        intent.putExtra("Url",url);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
//        homeConvenientBanner.stopTurning();
//        getActivity().overridePendingTransition(0, 0);
        //结束轮播
        bannerFind.stopAutoPlay();
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        switch (requestCode) {
            case 100:
//                findSwLayout.setRefreshing(false);
                if (returnCode == 200){
                    FindRcModel findRcModel = JSON.parseObject(returnData, FindRcModel.class);
                    List<FindRcModel.DataBean> data = findRcModel.getData();
                    //轮播
                    listBanner = new ArrayList<>();
                    //正文列表
                     listBody = new ArrayList<>();
                    for (int i = 0,j = data.size(); i < j; i++) {
                        FindRcModel.DataBean dataBean = data.get(i);
                        int type = dataBean.getType();
                        if (type == 1){
                            FindBannerModel findBanner = new FindBannerModel();
                            findBanner.setImg(dataBean.getImg());
                            findBanner.setUrl(dataBean.getUrl());
                            listBanner.add(findBanner);
                        }else {
                            FindListModel body = new FindListModel();
                            body.setImg(dataBean.getImg());
                            body.setUrl(dataBean.getUrl());
                            body.setFrom(dataBean.getFrom());
                            body.setTime(dataBean.getTime());
                            body.setTitle(dataBean.getTitle());
                            body.setBrief(dataBean.getBrief());
                            listBody.add(body);
                        }
                    }
                    bannerFind.setImages(listBanner)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(this)
                    .start();
                    adapter.setData(listBody);
                }else {
                    ToastUtil.showToast(returnMsg);
                }
                break;
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
    public void OnItemClick(View view, int position) {
        FindListModel findListModel = listBody.get(position);
        String url = findListModel.getUrl();
        startWebView(url);
    }

    @Override
    public void onRefresh() {
        setDialogShow(false);
        Map<String, String> map = new HashMap<>();
        map.put("sign", sign);
        okHttpPostBody(100, GlobalParam.FINDLIST, map);
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Message msg = handler.obtainMessage();
//                msg.what = 0;
//                msg.obj = getActivity();
//                handler.sendMessage(msg);
////                swipeRefreshLayout.setRefreshing(false);
//            }
//        };
//        new Timer().schedule(timerTask, 2000);
    }
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    findSwLayout.setRefreshing(false);
//                    break;
//            }
//        }
//    };

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
    }
}
