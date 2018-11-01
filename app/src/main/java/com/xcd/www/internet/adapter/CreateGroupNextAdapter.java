package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class CreateGroupNextAdapter extends RecyclerView.Adapter<CreateGroupNextAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ContactModel> mData;
    private Context mContext;

    public CreateGroupNextAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<ContactModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_creategroupnext, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = view.findViewById(R.id.tvName);
        viewHolder.ivSortlogo = view.findViewById(R.id.iv_Sortlogo);
        viewHolder.tvSortlogo = view.findViewById(R.id.tv_Sortlogo);
        viewHolder.ivSelectBlue = view.findViewById(R.id.iv_SelectBlue);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ContactModel contactModel = mData.get(position);

        String logo = contactModel.getLogo();
        if (logo.indexOf("http") != -1) {
            Glide.with(mContext.getApplicationContext())
                    .load(logo)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivSortlogo);
            holder.tvSortlogo.setText("");

        } else {
            holder.tvSortlogo.setText(logo);
            if (position % 3 == 0) {
                holder.ivSortlogo.setImageResource(R.drawable.shape_round_blue);
            } else if (position % 3 == 1) {
                holder.ivSortlogo.setImageResource(R.drawable.shape_round_red);
            } else {
                holder.ivSortlogo.setImageResource(R.drawable.shape_round_orange);
            }

        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }
        String name = contactModel.getName();
        if (TextUtils.isEmpty(name)) {
            String mobile = contactModel.getMobile();
            holder.tvName.setText(mobile);
        } else {
            holder.tvName.setText(name);
        }


        onItemEventClick(holder);
        boolean select = contactModel.isSelect();
        if (select) {
            holder.ivSelectBlue.setBackgroundResource(R.mipmap.select_blue);
        } else {
            holder.ivSelectBlue.setBackgroundResource(R.mipmap.select_white);
        }


//        holder.tvMeBagType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, mData.get(position).getName(),Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void onItemEventClick(RecyclerView.ViewHolder holder) {
        final int position = holder.getLayoutPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mOnItemClickListener.OnItemLongClick(v, position);
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
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
        TextView tvName, tvSortlogo;
        CircleImageView ivSortlogo;
        ImageView ivSelectBlue;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     *
     * @param list
     */
    public void updateList(List<ContactModel> list) {
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
