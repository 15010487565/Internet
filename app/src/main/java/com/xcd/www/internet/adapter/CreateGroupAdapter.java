package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.view.CircleImageView;

import java.util.List;


/**
 * @author: xp
 * @date: 2017/7/19
 */

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ContactModel> mData;
    private Context mContext;

    public CreateGroupAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    public void setData(List<ContactModel> data){
        mData = data;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_creategroup, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName =  view.findViewById(R.id.tvName);
        viewHolder.ivSortlogo =  view.findViewById(R.id.iv_Sortlogo);
        viewHolder.tvSortlogo = view.findViewById(R.id.tv_Sortlogo);
        viewHolder.ivDeleteRed = view.findViewById(R.id.iv_DeleteRed);
        viewHolder.llDeleteRed = view.findViewById(R.id.ll_DeleteRed);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ContactModel contactModel = mData.get(position);

        String logo = contactModel.getLogo();
        if (!TextUtils.isEmpty(logo)&&logo.indexOf("http")!=-1){
            Glide.with(mContext.getApplicationContext())
                    .load(logo)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.ivSortlogo);
            holder.tvSortlogo.setText("");

        }else {
            String name = contactModel.getName();
            if (!TextUtils.isEmpty(name)){
                holder.tvSortlogo.setText(name.substring(0,1));
            }
            holder.ivSortlogo.setImageResource(R.drawable.shape_round_head);

        }

        if (mOnItemClickListener != null) {
            holder.llDeleteRed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemDeleteClick(holder.itemView, position);
                }
            });

        }
        String name = contactModel.getName();
        if (TextUtils.isEmpty(name)){
            String mobile = contactModel.getMobile();
            holder.tvName.setText(mobile);
        }else {
            holder.tvName.setText(name);
        }
//        boolean enable = contactModel.isEnable();
//        if (enable){//是都可邀請
//            holder.ivDeleteRed.setVisibility(View.VISIBLE);
//            boolean select = contactModel.isSelect();
//            if (select){
//                holder.ivDeleteRed.setBackgroundResource(R.mipmap.select_blue);
//            }else {
//                holder.ivDeleteRed.setBackgroundResource(R.mipmap.select_white);
//            }
//        }else {
//            holder.ivDeleteRed.setVisibility(View.GONE);
//        }

    }
//    private void onItemEventClick(RecyclerView.ViewHolder holder) {
//        final int position = holder.getLayoutPosition();
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemDeleteClick(v, position);
//            }
//        });
//    }
    @Override
    public int getItemCount() {
        return mData == null?0:mData.size();
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemDeleteClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvSortlogo;
        CircleImageView ivSortlogo;
        ImageView ivDeleteRed;
        LinearLayout llDeleteRed;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<ContactModel> list){
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
