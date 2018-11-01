package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.model.GroupInfoListModel;
import com.xcd.www.internet.view.CircleImageView;

import java.util.List;


/**
 * @author: xp
 * @date: 2018/10/14
 */

public class GroupinfoUpdataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<GroupInfoListModel.DataBean> mData;
    private Context mContext;
    private List<GroupInfoListModel.DataBean> listType;
    private String groupUserid;
    private String userId;

    public GroupinfoUpdataListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<GroupInfoListModel.DataBean> data, String groupUserid) {
        this.mData = data;
        this.listType = data;
        this.groupUserid = groupUserid;
        userId = String.valueOf(BaseApplication.getInstance().getId());
        notifyDataSetChanged();
    }

    public void addData(List<GroupInfoListModel.DataBean> list, String groupUserid) {
        this.listType = list;
        this.groupUserid = groupUserid;
        if (this.mData != null) {
            this.mData.addAll(list);
        } else {
            this.mData = list;
        }
        notifyDataSetChanged();
    }

    public List<GroupInfoListModel.DataBean> getList() {
        return mData;
    }
//    private Map viewHolderMap = new HashMap<>();
//
//    private Map getViewHolderMap() {
//        return viewHolderMap;
//    }
//
//    public void upFootText() {
//        Map viewHolderMap = getViewHolderMap();
//        FootViewHolder holder = (FootViewHolder) viewHolderMap.get("holder");
//        holder.footView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
//        switch (viewType) {
//            case TYPE_ITEM:
        view = mInflater.inflate(R.layout.item_groupinfo_updata, parent, false);
        viewHolder = new ViewHolder(view);

//                break;
//            case TYPE_FOOTER:
//                view = mInflater.inflate(R.layout.item_foot, parent, false);
//                viewHolder = new FootViewHolder(view);
//                viewHolderMap.put("holder", viewHolder);
//                break;
//        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        switch (getItemViewType(position)) {
//            case TYPE_ITEM:
        final ViewHolder holderItem = (ViewHolder) holder;
        GroupInfoListModel.DataBean dataBean = mData.get(position);

        String logo = dataBean.getH();

        Glide.with(mContext.getApplicationContext())
                .load(logo)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.launcher_login)
                .error(R.mipmap.launcher_login)
                .into(holderItem.ivGroupInfologo);
        int id = dataBean.getId();
        if (String.valueOf(id).equals(groupUserid)) {
            holderItem.tvGroupStatus.setText("群主");
            holderItem.llDeleteRed.setVisibility(View.GONE);
        } else {
            holderItem.tvGroupStatus.setText("");
            holderItem.llDeleteRed.setVisibility(View.VISIBLE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }
        String name = dataBean.getN();

        holderItem.tvName.setText(name + "" + position);

        holderItem.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            }
        });

//                break;
//            case TYPE_FOOTER:
//                FootViewHolder footviewholder = (FootViewHolder) holder;
//                if (mData == null || mData.size() == 0) {
//                    footviewholder.footView.setVisibility(View.GONE);
//                } else {
//                    footviewholder.footView.setVisibility(View.VISIBLE);
//                    if (listType == null || listType.size() == 0) {
//                        footviewholder.footView.setVisibility(View.GONE);
//                    } else {
//                        if (listType.size() == 10) {
//                            footviewholder.footText.setText(context.getResources().getString(R.string.pullup_to_load));
//                        } else {
//                            footviewholder.footText.setText(context.getResources().getString(R.string.unpullup_to_load));
//                        }
//                    }
//                }
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : (mData.size());
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemDeleteClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvGroupInfologo;
        CircleImageView ivGroupInfologo;
        TextView tvGroupStatus;
        LinearLayout llDeleteRed;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivGroupInfologo = itemView.findViewById(R.id.iv_GroupInfologo);
            tvGroupInfologo = itemView.findViewById(R.id.tv_GroupInfologo);
            tvGroupStatus = itemView.findViewById(R.id.tv_GroupStatus);
            llDeleteRed = itemView.findViewById(R.id.ll_DeleteRed);
            llDeleteRed.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_DeleteRed:
                    mOnItemClickListener.onItemDeleteClick(getLayoutPosition());
                    break;
            }
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout footView;
        public ProgressBar progressBar;
        public TextView footText;

        public FootViewHolder(View view) {
            super(view);
            footView = itemView.findViewById(R.id.footview);
//        footView =  itemView.findViewById(R.id.footView);
//        progressBar = itemView.findViewById(R.id.progressBar);
            footText = itemView.findViewById(R.id.footText);
        }

    }

    /**
     * 提供给Activity刷新数据
     *
     * @param list
     */
    public void updateList(List<GroupInfoListModel.DataBean> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

}
