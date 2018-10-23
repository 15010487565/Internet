package com.xcd.www.internet.rong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.RedPkgDetailsActivity;

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
        holder.tvRedPkgName =  view.findViewById(R.id.tv_RedPkgSendName);
        holder.tvRedPkgRemark =  view.findViewById(R.id.tv_RedPkgRemark);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, RedPackageMessage redPackageMessage, UIMessage message) {

        //根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();
//        int messageId = message.getMessageId();
//        Log.e("TAG_接受","messageId="+messageId);
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        //AndroidEmoji.ensure((Spannable) holder.message.getText());//显示消息中的 Emoji 表情。
        holder.tvRedPkgRemark.setText(redPackageMessage.getRemark());
        String sendName = redPackageMessage.getSendName();
        String sendRedType = redPackageMessage.getSendRedType();
        String minNumberString = String.format("来自%s的%s红包", sendName, sendRedType);
        holder.tvRedPkgName.setText(minNumberString);
    }
    //列表页消息显示内容
    @Override
    public Spannable getContentSummary(RedPackageMessage redPackageMessage) {
        String sendRedType = redPackageMessage.getSendRedType();
        if ("USDT".equals(sendRedType)){
            return new SpannableString("【红包】");
        }else {
            String desc1 = redPackageMessage.getSendName();
             return new SpannableString(desc1==null?"未知":desc1);
        }
    }

    @Override
    public void onItemClick(View view, int i, RedPackageMessage redPackageMessage, UIMessage uiMessage) {
        showOpenRedRkgDialog();
    }

    @Override
    public void onItemLongClick(View view, int i, RedPackageMessage redPackageMessage, UIMessage uiMessage) {
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

    private void showOpenRedRkgDialog() {
        if (openRedPkgDialog !=null && openRedPkgDialog.isShowing()){
            return;
        }
        LayoutInflater factor = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View serviceView = factor.inflate(R.layout.dialog_openredrkg, null);

        ImageView tvRedPkgClose = serviceView.findViewById(R.id.tv_OpenRedPkgClose);
        tvRedPkgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRedPkgDialog.dismiss();

            }
        });
        TextView refuse =  serviceView.findViewById(R.id.iv_OpenRedPkgBtn);
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRedPkgDialog.dismiss();
                Intent intent = new Intent(context, RedPkgDetailsActivity.class);
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
        openRedPkgDialog.show();
        openRedPkgDialog.setContentView(serviceView);
//        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.WRAP_CONTENT);
        //layout.setMargins(WallspaceUtil.dip2px(this, 10), 0, FeatureFunction.dip2px(this, 10), 0);
//        serviceView.setLayoutParams(layout);
    }
}
