package com.xcd.www.internet.base;

import android.os.Bundle;
import android.util.Log;

import com.xcd.www.internet.sq.BlackDao;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

/**
 * Created by gs on 2018/10/16.
 */

public abstract class BaseInternetActivity extends SimpleTopbarActivity {
    protected BlackDao blackDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blackDao = BlackDao.getInstance(this);
    }
    public void connect(String token) {

        Log.e("TAG_融云", "连接="+token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.e("TAG_融云", "--onTokenIncorrect---");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.e("TAG_融云", "--onSuccess---" + userid);

            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("TAG_融云", "--onError" + errorCode.getMessage());

            }
        });
    }
}
