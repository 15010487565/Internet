package com.xcd.www.internet.rong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcd.www.internet.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.fragment.ConversationFragment;


/**
 * Created by Android on 2017/10/20.
 */

public class BaseConversationFragment extends ConversationFragment {
    RongExtension mRongExtension;
    String rongvipshow;
    TextView rongviphint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRongExtension = (RongExtension) view.findViewById(R.id.rc_extension);


        return view;
    }

}
