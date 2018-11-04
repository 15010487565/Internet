package com.xcd.www.internet.func;

import android.app.Activity;
import android.view.View;

import com.xcd.www.internet.R;

import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import www.xcd.com.mylibrary.func.BaseTopImageBtnFunc;


public class HomeLeftTopBtnFunc extends BaseTopImageBtnFunc {

	public HomeLeftTopBtnFunc(Activity activity) {
		super(activity);
	}

	@Override
	public int getFuncId() {
		return R.id.home_left;
	}

	@Override
	public int getFuncIcon() {
		return R.mipmap.home_left;
	}

	@Override
	public void onclick(View v) {
		HashMap<String, Boolean> hashMap = new HashMap<>();
		//会话类型 以及是否聚合显示
//		hashMap.put(Conversation.ConversationType.PRIVATE.getName(),false);
//		hashMap.put(Conversation.ConversationType.PUSH_SERVICE.getName(),true);
		hashMap.put(Conversation.ConversationType.SYSTEM.getName(),false);//系统
		hashMap.put(Conversation.ConversationType.PUSH_SERVICE.getName(),false);//push推送
		RongIM.getInstance().startConversationList(getActivity(),hashMap);

//		getActivity().startActivity(new Intent(getActivity(), SystemActivity.class));
	}
}
