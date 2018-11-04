package com.xcd.www.internet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.OnRcItemClickListener;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.FindListModel;

import java.util.List;

/**
 * Created by gs on 2017/12/26.
 */

public class FindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FindListModel> contentList;

    private LayoutInflater inflater;
    int width1;
    public FindAdapter(Context context,int width1) {
        this.context = context;
        this.width1 = width1;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<FindListModel> contentList) {

        this.contentList = contentList;
        notifyDataSetChanged();
    }

    public OnRcItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnRcItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_find, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ViewHolder findHolder = (ViewHolder) holder;
        FindListModel findRcModel = contentList.get(position);
        String title = findRcModel.getTitle();
        findHolder.tvFindTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        String url = findRcModel.getImg();
        Log.e("TAG_发现","列表="+url);
        if (!TextUtils.isEmpty(url)) {
            findHolder.ivFindImage.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext())
                    .load(url)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(findHolder.ivFindImage);
        } else {
            findHolder.ivFindImage.setVisibility(View.GONE);
        }
        String brief = findRcModel.getBrief();
        findHolder.tvFindBrief.setText(TextUtils.isEmpty(brief) ? "" : brief);
        String from = findRcModel.getFrom();
        findHolder.tvFindFrom.setText(TextUtils.isEmpty(from) ? "" : from);
        String time = findRcModel.getTime();
        findHolder.tvFindTime.setText(TextUtils.isEmpty(time) ? "" : time);
        onItemEventClick(findHolder);
    }

    @Override
    public int getItemCount() {
        return contentList == null ? 0 : contentList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFindImage;
        TextView tvFindTitle, tvFindBrief;
        TextView tvFindFrom, tvFindTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFindTitle = itemView.findViewById(R.id.tv_FindTitle);

            ivFindImage = itemView.findViewById(R.id.iv_FindImage);

            tvFindBrief = itemView.findViewById(R.id.tv_FindBrief);

            tvFindFrom = itemView.findViewById(R.id.tv_FindFrom);
            tvFindTime = itemView.findViewById(R.id.tv_FindTime);

            ViewGroup.LayoutParams para = ivFindImage.getLayoutParams();//获取drawerlayout的布局
            para.height = width1 * 236 / 695;//修改宽度
//        para.height = height1;//修改高度
            ivFindImage.setLayoutParams(para); //设置修改后的布局。
        }
    }
    private void onItemEventClick(RecyclerView.ViewHolder holder) {
        final int position = holder.getLayoutPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v, position);
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
}

