package com.xcd.www.internet.func;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.AddFriendActivity;

import www.xcd.com.mylibrary.action.QuickAction;
import www.xcd.com.mylibrary.entity.BaseActionItem;
import www.xcd.com.mylibrary.func.BaseTopImageBtnFunc;


public class ContactRightTopBtnFunc extends BaseTopImageBtnFunc {

    public ContactRightTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.contact_right;
    }

    @Override
    public int getFuncIcon() {
        return R.mipmap.home_right;
    }

    @Override
    public void onclick(View v) {

//        showCreateMultiChatActionBar(v);
        getActivity().startActivity(new Intent(getActivity(),AddFriendActivity.class));
    }

    /**
     * 创建群聊/会议的响应事件
     *
     * @param view
     */
    public void showCreateMultiChatActionBar(View view) {
        QuickAction quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);
        quickAction.addActionItem(new BaseActionItem(0, getActivity().getString(R.string.addfriend),
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

                switch (actionId) {
                    case 0://创建群组
//                        intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("CURRENTITEM", 0);
//                        getActivity().startActivity(intent);
//                        getActivity().finish();
                        break;

                    case 1://邀请朋友
//                        intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("CURRENTITEM", 1);
//                        getActivity().startActivity(intent);
//                        getActivity().finish();
                        break;

                    case 2://加密私聊

                        break;

                    case 3://扫一扫

                        break;

                    default:
                        break;
                }
            }
        });
        quickAction.show(view);
        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
    }
}
