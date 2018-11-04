package com.xcd.www.internet.rong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.RedPkgDetailsActivity;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.view.CircleImageView;

import org.json.JSONObject;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by gs on 2018/10/21.
 */

@ProviderTag(
        messageContent = RedPackageMessage.class,//（这里是你自定义的消息实体）
        showReadState = true
)
public class RedPackageItemProvider extends IContainerItemProvider.MessageProvider<RedPackageMessage> {
    Context context;

    public RedPackageItemProvider() {
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        //这就是展示在会话界面的自定义的消息的布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_redpackage_message, null);
        ViewHolder holder = new ViewHolder();
        holder.tvRedPkgName = view.findViewById(R.id.tv_RedPkgSendName);
        holder.tvRedPkgRemark = view.findViewById(R.id.tv_RedPkgRemark);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, RedPackageMessage redPackageMessage, UIMessage message) {
        Log.e("TAG_紅包接收=============", "RedPackageMessage=" + redPackageMessage.toString());
        Log.e("TAG_紅包接收============", "UIMessage=" + message.getContent().toString());
        //根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();
//        int messageId = message.getMessageId();
//        Log.e("TAG_接受","messageId="+messageId);
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        try {
            String extra = redPackageMessage.getExtra();
            if (extra != null){
                JSONObject jsonObject = new JSONObject(extra);
                String content = jsonObject.optString("content");
                holder.tvRedPkgRemark.setText(TextUtils.isEmpty(content) ? "恭喜发财，大吉大利" : content);

                final String sendName = jsonObject.optString("sendName");
                String sendRedType = jsonObject.optString("coin");
                String minNumberString = String.format("来自%s的%s红包", sendName, sendRedType);
                holder.tvRedPkgName.setText(minNumberString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //列表页消息显示内容
    @Override
    public Spannable getContentSummary(RedPackageMessage redPackageMessage) {
        try {
            String extra = redPackageMessage.getExtra();
            if (extra !=null){
                JSONObject jsonObject = new JSONObject(extra);

                String sendRedType = jsonObject.optString("coin");
                if ("USDT".equals(sendRedType)) {
                    return new SpannableString("【红包】");
                } else {
                    String desc1 = redPackageMessage.getSendName();
                    return new SpannableString(desc1 == null ? "未知" : desc1);
                }

            }
            return new SpannableString("");
        } catch (Exception e) {
            e.printStackTrace();
            return new SpannableString("未知");
        }
    }

    @Override
    public void onItemClick(View view, int i, RedPackageMessage redPackageMessage, UIMessage uiMessage) {
        String groupId = uiMessage.getTargetId();
        Log.e("TAG_点击", "groupId=" + groupId);
        Log.e("TAG_点击", "context=" + (context == null));
        long id = BaseApplication.getInstance().getId();

        if (context != null)
            showOpenRedRkgDialog(redPackageMessage, groupId);
    }

    @Override
    public void onItemLongClick(View view, int i, RedPackageMessage redPackageMessage, UIMessage uiMessage) {
        String groupId = uiMessage.getTargetId();
        Log.e("TAG_点击", "groupId=" + groupId);
        //实现长按删除等功能，咱们直接复制融云其他provider的实现
//        String[] items1;//复制，删除
//        items1 = new String[]{view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_copy), view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_delete)};
//
//        OptionsPopupDialog.newInstance(view.getContext(), items1).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
//            public void onOptionsItemClicked(int which) {
//                ToastUtil.showToast("点击了红包");
////                if (which == 0) {
////                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
////                    clipboard.setText(content.getContent());//这里是自定义消息的消息属性
////                } else if (which == 1) {
////                    RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
////                }
//            }
//        }).show();

    }

    private static class ViewHolder {
        TextView tvRedPkgName, tvRedPkgRemark;
    }

    //打開紅包弹窗
    protected AlertDialog openRedPkgDialog;

    private void showOpenRedRkgDialog(final RedPackageMessage redPackageMessage, final String groupId) {
        try {

            String extra = redPackageMessage.getExtra();
            JSONObject jsonObject = new JSONObject(extra);
            final String headUrl = jsonObject.optString("headUrl");

            final String sendName = jsonObject.optString("sendName");
            final String contentStr = jsonObject.optString("content");

            final String redPkgId = jsonObject.optString("redPacketId");
            final String total = jsonObject.optString("total");
            final String amout = jsonObject.optString("amout");

            String sendID = jsonObject.optString("sendID");
            long id = BaseApplication.getInstance().getId();
//            if (String.valueOf(id).equals(sendID)){
//                Intent intent = new Intent(context, RedPkgDetailsActivity.class);
//                intent.putExtra("redPkgId", TextUtils.isEmpty(redPkgId) ? "" : redPkgId);
//                intent.putExtra("headUrl", TextUtils.isEmpty(headUrl) ? "" : headUrl);
//                intent.putExtra("sendName", TextUtils.isEmpty(sendName) ? "" : sendName);
//                intent.putExtra("content", TextUtils.isEmpty(contentStr) ? "" : contentStr);
//                intent.putExtra("total", total);
//                intent.putExtra("amout", amout);
//                intent.putExtra("groupId", groupId);
//                context.startActivity(intent);
//                return;
//            }
            if (openRedPkgDialog != null && openRedPkgDialog.isShowing()) {
                return;
            }
            LayoutInflater factor = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View serviceView = factor.inflate(R.layout.dialog_openredrkg, null);
            //头像
            CircleImageView ivOpenRedPkgHead = serviceView.findViewById(R.id.iv_OpenRedPkgHead);
            //关闭
            ImageView tvRedPkgClose = serviceView.findViewById(R.id.tv_OpenRedPkgClose);
            //昵称
            TextView tvOpenRedPkgName = serviceView.findViewById(R.id.tv_OpenRedPkgName);
            //备注
            TextView tvOpenRedPkgRemark = serviceView.findViewById(R.id.tv_OpenRedPkgRemark);
            TextView refuse = serviceView.findViewById(R.id.iv_OpenRedPkgBtn);


            tvOpenRedPkgName.setText(TextUtils.isEmpty(sendName) ? "" : sendName);


            tvOpenRedPkgRemark.setText(TextUtils.isEmpty(contentStr) ? "" : contentStr);

            Glide.with(context.getApplicationContext())
                    .load(headUrl)
                    .fitCenter()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivOpenRedPkgHead);

            tvRedPkgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRedPkgDialog.dismiss();

                }
            });

            refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRedPkgDialog.dismiss();
                    Intent intent = new Intent(context, RedPkgDetailsActivity.class);
                    intent.putExtra("redPkgId", TextUtils.isEmpty(redPkgId) ? "" : redPkgId);
                    intent.putExtra("headUrl", TextUtils.isEmpty(headUrl) ? "" : headUrl);
                    intent.putExtra("sendName", TextUtils.isEmpty(sendName) ? "" : sendName);
                    intent.putExtra("content", TextUtils.isEmpty(contentStr) ? "" : contentStr);
                    intent.putExtra("total", total);
                    intent.putExtra("amout", amout);
                    intent.putExtra("groupId", groupId);
                    context.startActivity(intent);
                }
            });


            Activity activity = (Activity) context;
            while (activity.getParent() != null) {
                activity = activity.getParent();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            openRedPkgDialog = builder.create();
            openRedPkgDialog.setCancelable(false);
            openRedPkgDialog.setCanceledOnTouchOutside(false);
            openRedPkgDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
            openRedPkgDialog.show();
            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
            android.view.WindowManager.LayoutParams p = openRedPkgDialog.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的0.3
            p.width = (int) (d.getWidth() * 0.72);    //宽度设置为屏幕的0.7
            openRedPkgDialog.getWindow().setAttributes(p);     //设置生效
            openRedPkgDialog.setContentView(serviceView);
//        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.WRAP_CONTENT);
            //layout.setMargins(WallspaceUtil.dip2px(this, 10), 0, FeatureFunction.dip2px(this, 10), 0);
//        serviceView.setLayoutParams(layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
