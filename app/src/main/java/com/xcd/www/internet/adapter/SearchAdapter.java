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
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.view.CircleImageView;

import java.util.List;

import static com.xcd.www.internet.common.Config.TYPE_FRIEND;
import static com.xcd.www.internet.common.Config.TYPE_TITLE;


/**
 * @author: xp
 * @date: 2017/7/19
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ContactModel> list;
    private Context mContext;

    public SearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    public void setData(List<ContactModel> data){
        list = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_TITLE:
                view = mInflater.inflate(R.layout.item_title, parent, false);
                holder = new TitleViewHolder(view);
                break;
            case TYPE_FRIEND:
                view = mInflater.inflate(R.layout.item_search, parent,false);
                holder = new ViewHolder(view);

                break;

        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        ContactModel contactModel = list.get(position);
        int type = contactModel.getType();
        if (type == TYPE_TITLE){
            return TYPE_TITLE;
        }else {
            return TYPE_FRIEND;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ContactModel contactModel = list.get(position);
        switch (getItemViewType(position)) {
            case TYPE_TITLE:
                TitleViewHolder holderTitle = (TitleViewHolder) holder;
                String name1 = contactModel.getName();
                holderTitle.tvSearchTitle.setText(name1);
                break;
            case TYPE_FRIEND:
                ViewHolder viewHolder = (ViewHolder) holder;

                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });

                }
                String name = contactModel.getName();
                if (TextUtils.isEmpty(name)){
                    String mobile = contactModel.getMobile();
                    viewHolder.tvName.setText(mobile);
                }else {
                    viewHolder.tvName.setText(name);
                }
                String logo = contactModel.getLogo();
                if (TextUtils.isEmpty(logo)){
                    viewHolder.tvSortlogo.setText(name.substring(0));
                }else {
                    if (logo.indexOf("http")!=-1){
                        Glide.with(mContext.getApplicationContext())
                                .load(logo)
                                .fitCenter()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into( viewHolder.ivSortlogo);
                        viewHolder.tvSortlogo.setText("");
                    }else {
                        viewHolder.tvSortlogo.setText(logo);
                        if (position%3==0){
                            viewHolder.ivSortlogo.setImageResource(R.drawable.shape_round_blue);
                        }else if (position%3==1){
                            viewHolder.ivSortlogo.setImageResource(R.drawable.shape_round_red);
                        }else {
                            viewHolder.ivSortlogo.setImageResource(R.drawable.shape_round_orange);
                        }
                    }
                }
                break;
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
    class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView tvSearchTitle;
        public TitleViewHolder(View itemView) {
            super(itemView);
            tvSearchTitle =  itemView.findViewById(R.id.tv_SearchTitle);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvSortlogo;
        CircleImageView ivSortlogo;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName =  itemView.findViewById(R.id.tvName);
            ivSortlogo =  itemView.findViewById(R.id.iv_Sortlogo);
            tvSortlogo = itemView.findViewById(R.id.tv_Sortlogo);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<ContactModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
