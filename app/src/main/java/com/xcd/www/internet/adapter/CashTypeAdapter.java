package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.model.CashTypeModel;

import java.util.List;


/**
 * @author: xp
 * @date: 2017/7/19
 */

public class CashTypeAdapter extends RecyclerView.Adapter<CashTypeAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<CashTypeModel> list;
    private Context mContext;

    public CashTypeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    public void setData(List<CashTypeModel> data){
        list = data;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cashtype, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName =  view.findViewById(R.id.tvName);
        viewHolder.ivSelectBlue = view.findViewById(R.id.iv_SelectBlue);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CashTypeModel cashTypeModel = list.get(position);

        String name = cashTypeModel.getName();
        holder.tvName.setText(name);

        boolean select = cashTypeModel.isSelect();
        if (select){
            holder.ivSelectBlue.setBackgroundResource(R.mipmap.select_blue);
        }else {
            holder.ivSelectBlue.setBackgroundResource(R.mipmap.select_white);
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }

//
//        holder.tvMeBagType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, list.get(position).getName(),Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list == null?0: list.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivSelectBlue;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
