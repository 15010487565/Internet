package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.MeBagTypeModel;

import java.util.List;


/**
 * @author: xp
 * @date: 2018/10/14
 */

public class MeMoneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<MeBagTypeModel> list;
    private Context mContext;
    private List<MeBagTypeModel> listType;

    public MeMoneyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<MeBagTypeModel> data) {
        this.list = data;
        this.listType = data;
        notifyDataSetChanged();
    }

    public void addData(List<MeBagTypeModel> list) {
        this.listType = list;
        if (this.list != null) {
            this.list.addAll(list);
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        view = mInflater.inflate(R.layout.item_memoney, parent, false);
        viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder holderItem = (ViewHolder) holder;
        MeBagTypeModel meBagTypeModel = list.get(position);
        String name = meBagTypeModel.getName();
        holderItem.tvMeBagType.setText(name);
        String allPrice = meBagTypeModel.getAllPrice();
        holderItem.tvMeBagAllMoney.setText(allPrice);
        holderItem.tvMeMoneyRightTop.setText(meBagTypeModel.getNum());
        holderItem.tvMeMoneyRightBottom.setText(meBagTypeModel.getPrice());
//        List<String> list = stringListMap.get("line");
//        LineChart lineChart = LineChartUtil.initChart(holderItem.lineChart);
//        LineChartUtil.showLineChart(lineChart , list, "测试", R.color.blue);
//        if (mOnItemClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.itemView, position);
//                }
//            });
//
//        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : (list.size());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeBagType, tvMeBagAllMoney, tvMeMoneyRightTop, tvMeMoneyRightBottom;
        LineChart lineChart;// 声明图表控件
        public ViewHolder(View itemView) {
            super(itemView);
            tvMeBagType = itemView.findViewById(R.id.tv_MeBagType);
            tvMeBagAllMoney = itemView.findViewById(R.id.tv_MeAllMoney);
            tvMeMoneyRightTop = itemView.findViewById(R.id.tv_MeMoneyRightTop);
            tvMeMoneyRightBottom = itemView.findViewById(R.id.tv_MeMoneyRightBottom);
            lineChart = itemView.findViewById(R.id.lineChart);//绑定控件
            lineChart.setVisibility(View.GONE);
        }
    }

}
