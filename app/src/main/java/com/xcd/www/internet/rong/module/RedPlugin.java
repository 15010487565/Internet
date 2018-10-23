package com.xcd.www.internet.rong.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.xcd.www.internet.R;
import com.xcd.www.internet.activity.RedPkgSendActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.model.Conversation.ConversationType;

/**
 * Created by gs on 2018/10/20.
 */

public class RedPlugin implements IPluginModule {
    ConversationType conversationType;
    String targetId;

    public RedPlugin() {
    }

    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.red_packet_black);
    }

    public String obtainTitle(Context context) {
        return context.getString(R.string.red_packet);
    }

    public void onClick(Fragment currentFragment, RongExtension extension) {
        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
        if(PermissionCheckUtil.requestPermissions(currentFragment, permissions)) {
            /**
             * conversationType 会话类型
             * 根据不同的 conversationType，可能是用户 Id、群组 Id 或聊天室 Id。
             */
            this.conversationType = extension.getConversationType();
            this.targetId = extension.getTargetId();
            Intent intent = new Intent(currentFragment.getActivity(), RedPkgSendActivity.class);
            intent.putExtra("targetId",targetId);
            extension.startActivityForPluginResult(intent, 110, this);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG_红包","requestCode====="+requestCode);
        switch (requestCode){
            case 110://红包
                break;
        }
    }
}
