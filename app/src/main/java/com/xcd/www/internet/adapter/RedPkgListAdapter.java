package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.model.RedPkgDetailsListModel;

import java.util.List;


/**
 * @author: xp
 * @date: 2018/10/14
 */

public class RedPkgListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<RedPkgDetailsListModel.DataBean> mData;
    private Context context;
    private List<RedPkgDetailsListModel.DataBean> listType;

    public RedPkgListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<RedPkgDetailsListModel.DataBean> data) {
        this.mData = data;
        this.listType = data;
        notifyDataSetChanged();
    }

    public void addData(List<RedPkgDetailsListModel.DataBean> list) {
        this.listType = list;
        if (this.mData != null) {
            this.mData.addAll(list);
        } else {
            this.mData = list;
        }
        notifyDataSetChanged();
    }
    public List<RedPkgDetailsListModel.DataBean> getListData(){
        return mData;
    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        view = mInflater.inflate(R.layout.item_redpkg_me_list, parent, false);
        viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        switch (getItemViewType(position)) {
//            case TYPE_ITEM:
        final ViewHolder holderItem = (ViewHolder) holder;
        RedPkgDetailsListModel.DataBean dataBean = mData.get(position);
        String content = dataBean.getContent();
        holderItem.tvRedPkgName.setText(content);
//        String logo = dataBean.get();
//
//        Glide.with(context.getApplicationContext())
//                .load(logo)
//                .fitCenter()
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.launcher_login)
//                .error(R.mipmap.launcher_login)
//                .into(holderItem.ivredPkgListHead);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }
//        String name = dataBean.getNick();
//        holderItem.tvMeBagType.setText(TextUtils.isEmpty(name)?"":name);
        String grabTime = dataBean.getTime();
        holderItem.tvRedPkgListTime.setText(TextUtils.isEmpty(grabTime)?"":grabTime);
        ////0收入1支出
        int direction = dataBean.getDirection();
        double amount = dataBean.getMoney();
        String priceResult = String.format("%.4f", amount);
        if (direction == 0){
            holderItem.tvRedPkgListMoney.setTextColor(ContextCompat.getColor(context,R.color.blue));
            holderItem.tvRedPkgListMoney.setText("+ "+priceResult);
        }else {
            holderItem.tvRedPkgListMoney.setTextColor(ContextCompat.getColor(context,R.color.black_4e));
            holderItem.tvRedPkgListMoney.setText("- "+ priceResult);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : (mData.size());
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRedPkgName, tvRedPkgListTime, tvRedPkgListMoney;


        public ViewHolder(View itemView) {
            super(itemView);
            tvRedPkgName = itemView.findViewById(R.id.tv_RedPkgListName);
            tvRedPkgListTime = itemView.findViewById(R.id.tv_RedPkgListTime);
            tvRedPkgListMoney = itemView.findViewById(R.id.tv_RedPkgListMoney);

        }
    }

}
