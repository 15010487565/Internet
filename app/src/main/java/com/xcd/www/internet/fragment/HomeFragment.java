package com.xcd.www.internet.fragment;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.xcd.www.internet.R;
import com.xcd.www.internet.base.SimpleTopbarFragment;
import com.xcd.www.internet.func.HomeLeftTopBtnFunc;
import com.xcd.www.internet.func.HomeRightTopBtnFunc;
import com.xcd.www.internet.rong.BaseExtensionModule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by gs on 2018/10/16.
 */

public class HomeFragment extends SimpleTopbarFragment {

    private static Class<?> rightFuncArray[] = {HomeRightTopBtnFunc.class};

    @Override
    protected Object getTopbarTitle() {
        return R.string.home;
    }

    @Override
    protected Class<?> getTopbarLeftFunc() {
        return HomeLeftTopBtnFunc.class;
    }

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {
        //会话列表
        ConversationListFragment conversationListFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//设置系统会话，该会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .build();
        conversationListFragment.setUri(uri);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.rong_container, conversationListFragment);
        transaction.commit();
        resetExtensionPlugin();
    }

    //重新设置会话界面的ExtensionModule
    private void resetExtensionPlugin() {
        List<IExtensionModule> extensionModules = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultExtensionModule = null;
        for (int i = 0; i < extensionModules.size(); i++) {
            IExtensionModule module = extensionModules.get(i);
            //取出默认的模块
            if (module instanceof DefaultExtensionModule) {
                defaultExtensionModule = module;
            }
        }
        if (defaultExtensionModule != null) {
            //删除默认展示的模块
            RongExtensionManager.getInstance().unregisterExtensionModule(defaultExtensionModule);
        }

        //注册自定义的模块
        RongExtensionManager.getInstance().registerExtensionModule(new BaseExtensionModule());
    }


    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

    }

    @Override
    public void onCancelResult() {

    }

    @Override
    public void onErrorResult(int errorCode, IOException errorExcep) {

    }

    @Override
    public void onParseErrorResult(int errorCode) {

    }

    @Override
    public void onFinishResult() {

    }

}
