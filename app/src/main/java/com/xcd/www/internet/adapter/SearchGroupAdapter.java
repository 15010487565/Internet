package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.SearchModel;
import com.xcd.www.internet.view.CircleImageView;

import java.util.List;

import static com.xcd.www.internet.common.Config.TYPE_FRIEND;
import static com.xcd.www.internet.common.Config.TYPE_GROUP;
import static com.xcd.www.internet.common.Config.TYPE_GROUPFRIEND;
import static com.xcd.www.internet.common.Config.TYPE_TITLE;


/**
 * @author: xp
 * @date: 2017/7/19
 */

public class SearchGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SearchModel> list;
    private Context mContext;
    private String srarchMsg;
    public SearchGroupAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<SearchModel> data,String srarchMsg){
        this.srarchMsg = srarchMsg;
        list = data;
        notifyDataSetChanged();
    }

    public List<SearchModel> getDataList(){
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_TITLE:
                view = mInflater.inflate(R.layout.item_titlegroup, parent, false);
                holder = new TitleViewHolder(view);
                break;
            case TYPE_FRIEND:
                view = mInflater.inflate(R.layout.item_searchgroup, parent,false);
                holder = new ViewHolder(view);

                break;

            case TYPE_GROUP:
                view = mInflater.inflate(R.layout.item_searchgroup, parent,false);
                holder = new ViewHolder(view);

                break;
            case TYPE_GROUPFRIEND:
                view = mInflater.inflate(R.layout.item_searchgroup, parent,false);
                holder = new ViewHolder(view);

                break;

        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        SearchModel searchModel = list.get(position);
        int type = searchModel.getMseeageType();
        if (type == 0){
            return TYPE_TITLE;
        }else if (type == 1){
            return TYPE_FRIEND;
        }else if (type == 2){
            return TYPE_GROUP;
        }else {
            return TYPE_GROUPFRIEND;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SearchModel searchModel = list.get(position);
        String logo = searchModel.getHeadUrl();
        String name = searchModel.getNick();
        String messageCon = searchModel.getMessageCon();
        switch (getItemViewType(position)) {
            case TYPE_TITLE:
                TitleViewHolder holderTitle = (TitleViewHolder) holder;
                holderTitle.tvSearchTitle.setText(name);
                break;
            case TYPE_FRIEND:
                ViewHolder viewHolder = (ViewHolder) holder;
                if (TextUtils.isEmpty(name)){
                    viewHolder.tvName.setText("");
                }else {
                    viewHolder.tvName.setText(name);
                }

                if (TextUtils.isEmpty(logo)){
                    viewHolder.tvSortlogo.setText(TextUtils.isEmpty(name)?"":name.substring(0,1));
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
                        if (!TextUtils.isEmpty(name)){
                            viewHolder.tvSortlogo.setText(name.substring(0,1));
                        }
                        viewHolder.ivSortlogo.setImageResource(R.drawable.shape_round_head);
                    }
                }
                int indexOf = messageCon.indexOf(this.srarchMsg);
                SpannableStringBuilder span = new SpannableStringBuilder(messageCon);
                span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blue)), indexOf, indexOf+srarchMsg.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                viewHolder.tvMessage.setText(span);
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onGroupItemClick(holder.itemView, position);
                        }
                    });

                }
                break;
            case TYPE_GROUP:
                ViewHolder viewHolder1 = (ViewHolder) holder;

                if (TextUtils.isEmpty(name)){
                    viewHolder1.tvName.setText("");
                }else {
                    viewHolder1.tvName.setText(name);
                }
                if (TextUtils.isEmpty(logo)){
                    viewHolder1.tvSortlogo.setText(TextUtils.isEmpty(name)?"":name.substring(0,1));
                }else {
                    if (logo.indexOf("http")!=-1){
                        Glide.with(mContext.getApplicationContext())
                                .load(logo)
                                .fitCenter()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into( viewHolder1.ivSortlogo);
                        viewHolder1.tvSortlogo.setText("");
                    }else {
                        if (!TextUtils.isEmpty(name)){
                            viewHolder1.tvSortlogo.setText(name.substring(0,1));
                        }
                        viewHolder1.ivSortlogo.setImageResource(R.drawable.shape_round_head);

                    }
                }
                int messageNum = searchModel.getMessageNum();
                viewHolder1.tvMessage.setText(messageNum+"条相关的聊天记录");
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onGroupItemClick(holder.itemView, position);
                        }
                    });

                }
                break;
            case TYPE_GROUPFRIEND:
                ViewHolder holderGroupFriend = (ViewHolder) holder;
                if (TextUtils.isEmpty(name)){
                    holderGroupFriend.tvName.setText("");
                }else {
                    holderGroupFriend.tvName.setText(name);
                }

                if (TextUtils.isEmpty(logo)){
                    holderGroupFriend.tvSortlogo.setText(TextUtils.isEmpty(name)?"":name.substring(0,1));
                }else {
                    if (logo.indexOf("http")!=-1){
                        Glide.with(mContext.getApplicationContext())
                                .load(logo)
                                .fitCenter()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into( holderGroupFriend.ivSortlogo);
                        holderGroupFriend.tvSortlogo.setText("");
                    }else {
                        if (!TextUtils.isEmpty(name)){
                            holderGroupFriend.tvSortlogo.setText(name.substring(0,1));
                        }
                        holderGroupFriend.ivSortlogo.setImageResource(R.drawable.shape_round_head);
                    }
                }
                int indexOfGroupFriend = messageCon.indexOf(this.srarchMsg);
                SpannableStringBuilder spanGroupFriend = new SpannableStringBuilder(messageCon);
                spanGroupFriend.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blue)), indexOfGroupFriend, indexOfGroupFriend+srarchMsg.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holderGroupFriend.tvMessage.setText(spanGroupFriend);
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onGroupItemClick(holder.itemView, position);
                        }
                    });

                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list == null?0: list.size();
    }

    //**********************itemClick************************
    public interface OnGroupItemClickListener {
        void onGroupItemClick(View view, int position);
    }

    public OnGroupItemClickListener mOnItemClickListener;

    public void setOnGroupItemClickListener(OnGroupItemClickListener mOnItemClickListener) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvSortlogo,tvMessage;
        CircleImageView ivSortlogo;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName =  itemView.findViewById(R.id.tvName);
            ivSortlogo =  itemView.findViewById(R.id.iv_Sortlogo);
            tvSortlogo = itemView.findViewById(R.id.tv_Sortlogo);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
