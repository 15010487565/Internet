package com.xcd.www.internet.func;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.CreateGroupNextActivity;
import com.xcd.www.internet.activity.MainActivity;

import www.xcd.com.mylibrary.action.QuickAction;
import www.xcd.com.mylibrary.entity.BaseActionItem;
import www.xcd.com.mylibrary.func.BaseTopImageBtnFunc;


public class HomeRightTopBtnFunc extends BaseTopImageBtnFunc {

    public HomeRightTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.home_right;
    }

    @Override
    public int getFuncIcon() {
        return R.mipmap.home_right;
    }

    @Override
    public void onclick(View v) {
        showCreateMultiChatActionBar(v);
    }

    /**
     * 创建群聊/会议的响应事件
     *
     * @param view
     */
    public void showCreateMultiChatActionBar(View view) {
        QuickAction quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);
        quickAction.addActionItem(new BaseActionItem(0, getActivity().getString(R.string.home_create),
                ContextCompat.getDrawable(getActivity(), R.mipmap.home_create)));
        quickAction.addActionItem(new BaseActionItem(1, getActivity().getString(R.string.home_share),
                ContextCompat.getDrawable(getActivity(), R.mipmap.home_share)));

        quickAction.addActionItem(new BaseActionItem(2, getActivity().getString(R.string.home_chat),
                ContextCompat.getDrawable(getActivity(), R.mipmap.home_chat)));
        quickAction.addActionItem(new BaseActionItem(3, getActivity().getString(R.string.home_scanqrcode),
                ContextCompat.getDrawable(getActivity(), R.mipmap.home_scanqrcode)));
        ;

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                Intent intent = null;
                switch (actionId) {
                    case 0://创建群组
                        intent = new Intent(getActivity(), CreateGroupNextActivity.class);
                        getActivity().startActivity(intent);
                        break;

                    case 1://邀请朋友
                        ((MainActivity)getActivity()).onClickShare();
                        break;

                    case 2://加密私聊

                        break;

                    case 3://扫一扫
                        ((MainActivity)getActivity()).scanAQRCode();
                        break;

                    default:
                        break;
                }
            }
        });
        quickAction.show(view);
        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
        final Window window = getActivity().getWindow();
        final WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha=0.3f;
        //此行代码主要是解决在华为手机上半透明效果无效的bug
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        window.setAttributes(attributes);
        quickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                attributes.alpha=1.0f;
                window.setAttributes(attributes);
            }
        });
    }
}
