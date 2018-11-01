package com.xcd.www.internet.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.AccountActivity;
import com.xcd.www.internet.activity.AccountCodeActivity;
import com.xcd.www.internet.activity.MeMoneyActivity;
import com.xcd.www.internet.activity.MeRedPkgActivity;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.func.MeAboutFunc;
import com.xcd.www.internet.func.MeFriendFunc;
import com.xcd.www.internet.func.MeHelpFunc;
import com.xcd.www.internet.func.MeLeftTopBtnFunc;
import com.xcd.www.internet.func.MeRankingFunc;
import com.xcd.www.internet.model.MeBagModel;
import com.xcd.www.internet.util.EventBusMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import www.xcd.com.mylibrary.entity.GlobalParam;
import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.view.MultiSwipeRefreshLayout;

import static com.xcd.www.internet.R.id.swipe_layout;

/**
 * Created by gs on 2018/10/16.
 */

public class MeFragment extends SimpleTopbarFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static Class<?>[] systemFuncArray = {
            MeRankingFunc.class,
            MeFriendFunc.class,
            MeHelpFunc.class,
            MeAboutFunc.class
    };
    /**
     * 功能对象
     */
    private Hashtable<Integer, BaseFunc> htFunc = new Hashtable<>();
    private PieChart mPieChart;
    /**
     * 系统功能View
     */
    private LinearLayout systemFuncView;
    private LinearLayout systemFuncList;
    private LinearLayout customFuncView;
    private LinearLayout customFuncList;

    private ImageView ivMeTopHead, ivMeCode;
    private TextView tvMeTopName, tvMeUsdtNum, tvMeUsdtMoney;
    private TextView tvMeTopPhone, tvMeTopHead;
    //我的资产
    private LinearLayout llMeMoney, llMeTop, llMeRedPkg;
    String sign;
    private MultiSwipeRefreshLayout meSwLayout;
    /**
     * 获得系统功能列表
     */
    protected Class<?>[] getSystemFuncArray() {
        return systemFuncArray;
    }

    /**
     * 获得自定义功能列表
     *
     * @return
     */
    protected Class<?>[] getCustomFuncArray() {
        return null;
    }

    @Override
    protected Object getTopbarTitle() {
        return R.string.me;
    }

    @Override
    protected Class<?> getTopbarLeftFunc() {
        return MeLeftTopBtnFunc.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        sign = BaseApplication.getInstance().getSign();
        initSwipeRefreshLayout(view);
        systemFuncView = view.findViewById(R.id.me_system_func_view);
        systemFuncList = view.findViewById(R.id.me_system_func_list);
        customFuncView = view.findViewById(R.id.me_custom_func_view);
        customFuncList = view.findViewById(R.id.me_custom_func_view);
        ivMeCode = view.findViewById(R.id.iv_MeCode);
        ivMeCode.setOnClickListener(this);
        //顶部个人资料
        initTopMember(view);
        //我的资产
        llMeMoney = view.findViewById(R.id.ll_MeMoney);
        llMeMoney.setOnClickListener(this);
        tvMeUsdtNum = view.findViewById(R.id.tv_MeUsdtNum);
        tvMeUsdtMoney = view.findViewById(R.id.tv_MeUsdtMoney);
        //红包
        llMeRedPkg = view.findViewById(R.id.ll_MeRedPkg);
        llMeRedPkg.setOnClickListener(this);
        //饼状图
        initPieChart(view);
        // 初始化自定义功能
        initCustomFunc();
        // 初始化系统功能
        initSystemFunc();
        initData();
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("sign", sign);
        okHttpPostBody(100, GlobalParam.MEBAG, params);
        getData(0);
    }

    private void initSwipeRefreshLayout(View view) {
        meSwLayout = view.findViewById(swipe_layout);
//        meSwLayout.setOnRefreshListener(this);
        //禁止下拉
//        meSwLayout.setEnabled(false);
        //下拉刷新监听
        meSwLayout.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        meSwLayout.setProgressViewOffset(true, -20, 100);
        meSwLayout.setColorSchemeResources(R.color.red, R.color.orange, R.color.blue, R.color.black);

    }
    private void initTopMember(View view) {
        llMeTop = view.findViewById(R.id.ll_MeTop);
        llMeTop.setOnClickListener(this);
        //顶部个人资料
        ivMeTopHead = view.findViewById(R.id.iv_MeTopHead);
        tvMeTopName = view.findViewById(R.id.tv_MeTopName);
        tvMeTopHead = view.findViewById(R.id.tv_MeTopHead);
        String name = BaseApplication.getInstance().getName();
        String nick = BaseApplication.getInstance().getNick();
        tvMeTopPhone = view.findViewById(R.id.tv_MeTopPhone);
        String account = BaseApplication.getInstance().getAccount();
        String country = BaseApplication.getInstance().getCountry();
        tvMeTopPhone.setText("+" + country + " " + account);
        String headportrait = BaseApplication.getInstance().getHeadportrait();

        if (TextUtils.isEmpty(nick)) {
            if (TextUtils.isEmpty(name)) {
                tvMeTopName.setText("");
            } else {
                tvMeTopName.setText(name);
            }
        } else {
            tvMeTopName.setText(nick);
        }
        if (TextUtils.isEmpty(headportrait)){
//            tvMeTopHead.setText(TextUtils.isEmpty(nick)?"":nick.substring(0));
            tvMeTopHead.setText("");
        }else {
            tvMeTopHead.setText("");
            Glide.with(getActivity())
                    .load(headportrait)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.launcher_login)
                    .error(R.mipmap.launcher_login)
                    .into(ivMeTopHead);
        }
    }

    private void getData(double usdtNum) {

        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(0, "LIT"));
        entries.add(new PieEntry(0, "LTC"));
        entries.add(new PieEntry(0, "BTC"));
        entries.add(new PieEntry(0, "ETH"));
//        entries.add(new PieEntry(5, "USDT"));

        entries.add(new PieEntry((float) usdtNum, "USDT"));

        //设置数据
        setData(entries);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    private void initPieChart(View view) {
        mPieChart = view.findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
//        mPieChart.setCenterText("");

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(false);
        mPieChart.setHighlightPerTapEnabled(false);

        //变化监听
//        mPieChart.setOnChartValueSelectedListener(this);
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    /**
     * 初始化系统功能
     */
    protected void initSystemFunc() {
        Class<?>[] systemFuncs = getSystemFuncArray();
        // 功能列表为空,隐藏区域
        if (systemFuncs == null || systemFuncs.length == 0) {
            systemFuncView.setVisibility(View.GONE);
            return;
        }
        // 初始化功能
        boolean isSeparator = false;
        for (Class<?> systemFunc : systemFuncs) {
            // view
            View funcView = getFuncView(systemFunc, isSeparator);
            if (funcView != null) {
                // 点击事件
                funcView.setOnClickListener(this);
                // 加入页面
                systemFuncList.addView(funcView);
                isSeparator = true;
            }
        }
        // 设置列表显示
        systemFuncList.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化自定义功能
     */
    protected void initCustomFunc() {
        Class<?>[] customFuncs = getCustomFuncArray();
        // 功能列表为空,隐藏区域
        if (customFuncs == null || customFuncs.length == 0) {
            customFuncView.setVisibility(View.GONE);
            return;
        }
        // 初始化功能
        boolean isSeparator = false;
        for (Class<?> customFunc : customFuncs) {
            // view
            View funcView = getFuncView(customFunc, isSeparator);
            if (funcView != null) {
                // 点击事件
                funcView.setOnClickListener(this);
                // 加入页面
                customFuncList.addView(funcView);
                isSeparator = true;
            }
        }
        // 设置列表显示
        customFuncList.setVisibility(View.VISIBLE);
    }

    /**
     * 获得功能View
     *
     * @return
     */
    private View getFuncView(Class<?> funcClazz, boolean isSeparator) {
        BaseFunc func = BaseFunc.newInstance(funcClazz, getFragmentActivity());
        if (func == null) {
            return null;
        }
        htFunc.put(func.getFuncId(), func);
        // view
        return func.initFuncView(isSeparator);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_MeMoney://我的总资产
                double usdt = data.getUsdt();
                double ltc = data.getLtc();
                Intent intent = new Intent(getFragmentActivity(), MeMoneyActivity.class);
                intent.putExtra("usdt", String.valueOf(usdt));
                startActivity(intent);
                break;
            case R.id.ll_MeTop:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.ll_MeRedPkg:
                startActivity(new Intent(getActivity(), MeRedPkgActivity.class));
                break;
            case R.id.iv_MeCode:
                startActivity(new Intent(getActivity(), AccountCodeActivity.class));
                break;
            default:
                // func
                BaseFunc func = htFunc.get(v.getId());
                // 处理点击事件
                if (func != null) {
                    func.onclick();
                }
                break;
        }
    }

    MeBagModel.DataBean data;

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    MeBagModel mebagModel = JSON.parseObject(returnData, MeBagModel.class);
                    data = mebagModel.getData();
                    double usdt = data.getUsdt();
                    BaseApplication instance = BaseApplication.getInstance();
                    instance.setUsdt(String.valueOf(usdt));
                    tvMeUsdtNum.setText(String.valueOf(usdt));
                    String usdt_dollar = BaseApplication.getInstance().getUsdt_dollar();
                    try {
                        String priceResult = String.format("%.4f", String.valueOf(usdt*Double.valueOf(usdt_dollar)));
                        tvMeUsdtMoney.setText(priceResult);
                    } catch (Exception e) {
                        tvMeUsdtMoney.setText(String.valueOf(usdt));
                        e.printStackTrace();
                    }
                    // 设置中间 文件
//                    mPieChart.setCenterText(String.valueOf(usdt));
                    mPieChart.setCenterText("");
                    Log.e("TAG_usdt", "usdt=" + usdt);
                    //银行卡
                    String cardNum = data.getCardNum();
                    if (!TextUtils.isEmpty(cardNum)) {
                        instance.setCardNum(cardNum);
                    }
                    getData(usdt);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusMsg event) {
        String msg = event.getMsg();
        Log.e("TAG_Main", "Contact=" + msg);
        if ("RefreshHeadportrait".equals(msg)) {
            // 更新主线程ＵＩ
            String headportrait = BaseApplication.getInstance().getHeadportrait();
            Glide.with(getActivity())
                    .load(headportrait)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.launcher_login)
                    .error(R.mipmap.launcher_login)
                    .into(ivMeTopHead);
        }else if ("RefreshNick".equals(msg)){
            String nick = BaseApplication.getInstance().getNick();
            tvMeTopName.setText(TextUtils.isEmpty(nick)?"":nick);
        }
    }

    @Override
    public void onRefresh() {
        setDialogShow(false);
        initData();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = getActivity();
                handler.sendMessage(msg);
//                swipeRefreshLayout.setRefreshing(false);
            }
        };
        new Timer().schedule(timerTask, 2000);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    meSwLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
