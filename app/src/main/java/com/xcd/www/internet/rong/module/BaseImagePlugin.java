package com.xcd.www.internet.rong.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.xcd.www.internet.R;

import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation.ConversationType;

/**
 * Created by gs on 2018/10/20.
 */

public class BaseImagePlugin extends ImagePlugin {
    ConversationType conversationType;
    String targetId;

    public BaseImagePlugin() {
    }

    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.album_title);
    }

    public String obtainTitle(Context context) {
        return context.getString(R.string.album_title);
    }

//    public void onClick(Fragment currentFragment, RongExtension extension) {
//        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
//        if(PermissionCheckUtil.requestPermissions(currentFragment, permissions)) {
//            this.conversationType = extension.getConversationType();
//            this.targetId = extension.getTargetId();
////            Intent intent = new Intent(currentFragment.getActivity(), PictureSelectorActivity.class);
////            extension.startActivityForPluginResult(intent, 23, this);
//            ToastUtil.showToast("点击相册");
//        }
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
