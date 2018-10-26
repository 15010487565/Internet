package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.RedPkgDetailsModel;
import com.xcd.www.internet.view.CircleImageView;

import java.util.List;


/**
 * @author: xp
 * @date: 2018/10/14
 */

public class RedPkgOpenDetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<RedPkgDetailsModel.DataBean> mData;
    private Context mContext;
    private List<RedPkgDetailsModel.DataBean> listType;

    public RedPkgOpenDetAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<RedPkgDetailsModel.DataBean> data) {
        this.mData = data;
        this.listType = data;
        notifyDataSetChanged();
    }

    public void addData(List<RedPkgDetailsModel.DataBean> list) {
        this.listType = list;
        if (this.mData != null) {
            this.mData.addAll(list);
        } else {
            this.mData = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        view = mInflater.inflate(R.layout.item_redpkgopendet, parent, false);
        viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        switch (getItemViewType(position)) {
//            case TYPE_ITEM:
        final ViewHolder holderItem = (ViewHolder) holder;
        RedPkgDetailsModel.DataBean dataBean = mData.get(position);

        String logo = dataBean.getHead();

        Glide.with(mContext.getApplicationContext())
                .load(logo)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.launcher_login)
                .error(R.mipmap.launcher_login)
                .into(holderItem.ivGroupInfologo);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }
        String name = dataBean.getNick();
        holderItem.tvRedPkgName.setText(TextUtils.isEmpty(name)?"":name);
        String grabTime = dataBean.getGrabTime();
        holderItem.tvRedPkgTime.setText(TextUtils.isEmpty(grabTime)?"":grabTime);
        double amount = dataBean.getAmount();
        String priceResult = "￥" + String.format("%.4f", amount);
        holderItem.tvRedPkgMoney.setText(priceResult+" USDT");
        if (position == 0){
            holderItem.tvRedPkgHint.setVisibility(View.VISIBLE);
        }else {
            holderItem.tvRedPkgHint.setVisibility(View.GONE);
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
        TextView tvRedPkgName, tvRedPkgTime, tvRedPkgMoney, tvRedPkgHint, tvGroupInfologo;
        CircleImageView ivGroupInfologo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRedPkgName = itemView.findViewById(R.id.tv_RedPkgName);
            tvRedPkgTime = itemView.findViewById(R.id.tv_RedPkgTime);
            tvRedPkgMoney = itemView.findViewById(R.id.tv_RedPkgMoney);
            tvRedPkgHint = itemView.findViewById(R.id.tv_RedPkgHint);
            ivGroupInfologo = itemView.findViewById(R.id.iv_GroupInfologo);
            tvGroupInfologo = itemView.findViewById(R.id.tv_GroupInfologo);
        }
    }

}
