package com.xcd.www.internet.fragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xcd.www.internet.R;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.func.MeLeftTopBtnFunc;
import com.xcd.www.internet.func.MeRankingFunc;
import com.xcd.www.internet.func.MeFriendFunc;
import com.xcd.www.internet.func.MeHelpFunc;
import com.xcd.www.internet.func.MeAboutFunc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * Created by gs on 2018/10/16.
 */

public class MeFragment extends SimpleTopbarFragment {

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
        systemFuncView = view.findViewById(R.id.me_system_func_view);
        systemFuncList = view.findViewById(R.id.me_system_func_list);
        customFuncView = view.findViewById(R.id.me_custom_func_view);
        customFuncList = view.findViewById(R.id.me_custom_func_view);
        //饼状图
        initPieChart(view);
        // 初始化自定义功能
        initCustomFunc();
        // 初始化系统功能
        initSystemFunc();
        //模拟数据
        getData();
    }

    private void getData() {
        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(50, "LIT"));
        entries.add(new PieEntry(18, "LTC"));
        entries.add(new PieEntry(15, "BTC"));
        entries.add(new PieEntry(10, "ETH"));
        entries.add(new PieEntry(7, "USDT"));

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
        mPieChart = (PieChart) view.findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText("$123456780");

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
//            case R.id.me_account_setting:
//                startActivity(new Intent(getFragmentActivity(), AccountActivity.class));
//                break;
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
