package com.xcd.www.internet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xcd.www.internet.R;
import com.xcd.www.internet.adapter.SearchFriendAdapter;
import com.xcd.www.internet.adapter.SearchGroupAdapter;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.base.BaseInternetActivity;
import com.xcd.www.internet.common.Config;
import com.xcd.www.internet.model.ContactModel;
import com.xcd.www.internet.model.SearchModel;
import com.xcd.www.internet.sq.BlackNumBean;
import com.xcd.www.internet.ui.RecyclerViewDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.SearchConversationResult;
import www.xcd.com.mylibrary.utils.SharePrefHelper;

public class SearchAllActivity extends BaseInternetActivity implements TextWatcher,
        SearchGroupAdapter.OnGroupItemClickListener,
        SearchFriendAdapter.OnFriendItemClickListener {

    private EditText etSearch;
    List<ContactModel> contactSearch;//模糊搜索集合
    List<ContactModel> contactInfo;//数据总集合
    private RecyclerView rcSearch;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchFriendAdapter adapterFriend;
    private LinearLayout llBack;
    private ImageView ivClean;

    private RecyclerView rcGroupSearch;
    private LinearLayoutManager groupLinearLayoutManager;
    private SearchGroupAdapter adapterGroup;
    Conversation.ConversationType[] conversationTypes;
    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchall);
        contactInfo = new ArrayList<>();
        Intent intent = getIntent();
        int searchType = intent.getIntExtra("searchType", 0);
        if (searchType == 0){//0是全部；1是个人,2是群组
            conversationTypes = new Conversation.ConversationType[]{Conversation.ConversationType.GROUP
                    , Conversation.ConversationType.PRIVATE};
        }else {
            conversationTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE};
        }
        BaseApplication instance = BaseApplication.getInstance();
        List<ContactModel> friendList = instance.getFriendList();
        contactInfo.addAll(friendList);
//        List<ContactModel> phoneList = instance.getPhoneList();
//        Iterator it = phoneList.iterator();
//        if (friendList == null || friendList.size()==0){
//            contactInfo.addAll(phoneList);
//        }else {
//            for (int i = 0, j = friendList.size(); i < j; i++) {
//                ContactModel contactModel1 = friendList.get(i);
//                String p = contactModel1.getMobile();
//                while (it.hasNext()) {
//                    ContactModel contactModel = (ContactModel) it.next();
//                    String mobile = contactModel.getMobile();
//                    if (p.equals(mobile)) {
//                        it.remove();
//                    }
//                }
//            }
//            contactInfo.addAll(friendList);
//            contactInfo.addAll(phoneList);
//        }
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        llBack = findViewById(R.id.ll_Back);
        llBack.setOnClickListener(this);
        ivClean = findViewById(R.id.iv_Clean);
        ivClean.setVisibility(View.GONE);
        ivClean.setOnClickListener(this);

        etSearch = findViewById(R.id.et_Search);
        etSearch.addTextChangedListener(this);
        showSoftInputFromWindow(etSearch);
        //好友列表
        initFriend();
        //群组列表
        initGroupList();
    }

    private void initGroupList() {

        rcGroupSearch = findViewById(R.id.rc_GroupSearch);
        rcGroupSearch.setVisibility(View.VISIBLE);
        groupLinearLayoutManager = new LinearLayoutManager(this);
        groupLinearLayoutManager.setAutoMeasureEnabled(true);
        rcGroupSearch.setLayoutManager(groupLinearLayoutManager);
        adapterGroup = new SearchGroupAdapter(this);
        adapterGroup.setOnGroupItemClickListener(this);
        rcGroupSearch.setAdapter(adapterGroup);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcGroupSearch.addItemDecoration(recyclerViewDecoration);
    }

    private void initFriend() {
        rcSearch = findViewById(R.id.rc_Search);
        rcSearch.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        rcSearch.setLayoutManager(mLinearLayoutManager);
        adapterFriend = new SearchFriendAdapter(this);
        adapterFriend.setOnFriendItemClickListener(this);
        rcSearch.setAdapter(adapterFriend);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line_c3));
        rcSearch.addItemDecoration(recyclerViewDecoration);
    }

    private void showSoftInputFromWindow(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                imm.showSoftInput(editText, 0);
                editText.setSelection(editText.getText().length());
            }
        }, 200);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_Back:
                finish();
                break;
            case R.id.iv_Clean:
                etSearch.setText("");
                break;
        }
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, final int i, int i1, int i2) {
        //如果长度为0
        if (contactInfo != null && contactInfo.size() > 0) {
            int friendNum = 0;
            int contactNum = 0;
            if (charSequence.length() > 0) {
                final String trim = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    contactSearch = new ArrayList<>();//模糊搜索集合
                    for (int j = 0, k = contactInfo.size(); j < k; j++) {
                        ContactModel contactModel = contactInfo.get(j);
                        String name = contactModel.getName();
                        String letters = contactModel.getLetters();
                        if (name.indexOf(trim) != -1) {//匹配文字
                            String userId = contactModel.getUserId();
                            if (!TextUtils.isEmpty(userId) && !"null".equals(userId)) {
                                if (friendNum == 0) {
                                    ContactModel contactType = new ContactModel();
                                    contactType.setName("好友");
                                    contactType.setType(Config.TYPE_TITLE);
                                    contactSearch.add(contactType);
                                    friendNum++;
                                }
                                contactSearch.add(contactModel);

                            } else {
                                if (contactNum == 0) {
                                    ContactModel contactType = new ContactModel();
                                    contactType.setName("通讯录联系人");
                                    contactType.setType(Config.TYPE_TITLE);
                                    contactSearch.add(contactType);
                                    contactNum++;
                                }
                                contactSearch.add(contactModel);
                            }
                        }
//                    else {//匹配首字母
//                        String pinyin = PinyinUtils.getPingYin(trim);
//                        String sortString = pinyin.substring(0, 1);
//                        if (letters.equalsIgnoreCase(sortString)){
//                            contactSearch.add(contactModel);
//                        }
//                    }
                    }
                    adapterFriend.setData(contactSearch);
                    /**
                     * 搜索本地历史消息。
                     * 此接口可快速返回匹配的会话列表,并且会话中包含已匹配的消息数量。通过 {SearchConversationResult#getMatchCount()} 得到。
                     * 如果需要自定义消息也能被搜索到,需要在自定义消息中实现 {@link MessageContent#getSearchableWord()} 方法;
                     *
                     * @param keyword           搜索的关键字。
                     * @param conversationTypes 搜索的会话类型。
                     * @param objectNames       搜索的消息类型,例如:RC:TxtMsg。
                     * @param resultCallback    搜索结果回调。
                     */
                    RongIMClient.getInstance().searchConversations(trim,conversationTypes
                           ,
                            new String[]{"RC:TxtMsg"},
                            new RongIMClient.ResultCallback<List<SearchConversationResult>>() {
                                @Override
                                public void onSuccess(List<SearchConversationResult> searchConversationResults) {
                                    Log.e("TAG_消息模糊搜索", "publicServiceData=" + searchConversationResults.size());
                                    List<SearchModel> searchList = new ArrayList<SearchModel>();

                                    for (int j = 0; j < searchConversationResults.size(); j++) {
                                        SearchConversationResult searchConversationResult = searchConversationResults.get(j);
                                        Conversation conversation = searchConversationResult.getConversation();
                                        Conversation.ConversationType conversationType = conversation.getConversationType();
                                        String nameType = conversationType.getName();
                                        if ("private".equals(nameType)) {
                                            if (j == 0) {
                                                SearchModel searchModel = new SearchModel();
                                                searchModel.setMseeageType(0);
                                                searchModel.setNick("平台好友");
                                                searchList.add(searchModel);
                                            }
                                            SearchModel searchModel = new SearchModel();
                                            String targetId = conversation.getTargetId();
                                            searchModel.setTargetId(targetId);
                                            Log.e("TAG_消息模糊搜索private", "targetId=" + targetId);
                                            BlackNumBean blackNumBean = blackDao.rawQuery(targetId);
                                            String headUrl = blackNumBean.getHeadUrl();
                                            searchModel.setHeadUrl(headUrl);
                                            Log.e("TAG_消息模糊搜索private", "headUrl=" + headUrl);
                                            String nick = blackNumBean.getNick();
                                            searchModel.setNick(nick);
                                            Log.e("TAG_消息模糊搜索private", "nick=" + nick);
                                            MessageContent latestMessage = conversation.getLatestMessage();
                                            List<String> searchableWord = latestMessage.getSearchableWord();
                                            int latestMessageId = conversation.getLatestMessageId();
                                            searchModel.setLatestMessageId(latestMessageId);
                                            if (searchableWord != null && searchableWord.size() > 0) {
                                                String messageCon = searchableWord.get(searchableWord.size() - 1);
                                                Log.e("TAG_消息模糊搜索private", "messageCon=" + messageCon);
                                                searchModel.setMessageCon(messageCon);
                                            }
                                            searchModel.setMseeageType(1);
                                            searchList.add(searchModel);
                                        }
                                    }
                                    for (int j = 0; j < searchConversationResults.size(); j++) {
                                        SearchConversationResult searchConversationResult = searchConversationResults.get(j);
                                        Conversation conversation = searchConversationResult.getConversation();
                                        Conversation.ConversationType conversationType = conversation.getConversationType();
                                        String nameType = conversationType.getName();
                                        if ("group".equals(nameType)) {
                                            if (j == 0) {
                                                SearchModel searchModel = new SearchModel();
                                                searchModel.setMseeageType(0);
                                                searchModel.setNick("群组聊天记录");
                                                searchList.add(searchModel);
                                            }
                                            SearchModel searchModel = new SearchModel();
                                            String targetId = conversation.getTargetId();
                                            searchModel.setTargetId(targetId);
                                            Log.e("TAG_消息模糊搜索group", "targetId=" + targetId);
                                            BlackNumBean blackNumBean = blackDao.rawQuery(targetId);
                                            String headUrl = blackNumBean.getHeadUrl();
                                            searchModel.setHeadUrl(headUrl);
                                            Log.e("TAG_消息模糊搜索group", "headUrl=" + headUrl);
                                            String nick = blackNumBean.getNick();
                                            searchModel.setNick(nick);
                                            Log.e("TAG_消息模糊搜索group", "nick=" + nick);
                                            int matchCount = searchConversationResult.getMatchCount();
                                            Log.e("TAG_消息模糊搜索group", "matchCount=" + matchCount);
                                            searchModel.setMessageNum(matchCount);
                                            searchModel.setMseeageType(2);
                                            searchList.add(searchModel);
                                        }
                                    }
                                    adapterGroup.setData(searchList, trim);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                    ivClean.setVisibility(View.VISIBLE);
                    rcSearch.setVisibility(View.VISIBLE);
                    rcGroupSearch.setVisibility(View.VISIBLE);

                } else {
                    ivClean.setVisibility(View.GONE);
                    rcSearch.setVisibility(View.GONE);
                    rcGroupSearch.setVisibility(View.GONE);
                }
            } else {
                ivClean.setVisibility(View.GONE);
                rcSearch.setVisibility(View.GONE);
                rcGroupSearch.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFriendItemClick(View view, int position) {
        List<ContactModel> dataList = adapterFriend.getDataList();
        ContactModel contactModel = dataList.get(position);
        String userId = contactModel.getUserId();
        Intent intent = new Intent(this, FriendInfoActivity.class);
        intent.putExtra("targetId", Integer.valueOf(userId));
        startActivity(intent);
        finish();
    }

    @Override
    public void onGroupItemClick(View view, int position) {
        List<SearchModel> dataList = adapterGroup.getDataList();
        SearchModel searchModel = dataList.get(position);
        int mseeageType = searchModel.getMseeageType();
        String targetId = searchModel.getTargetId();
        if (mseeageType == 1) {//1单聊，2为群聊
            Intent intent = new Intent(this, FriendInfoActivity.class);
            intent.putExtra("targetId", Integer.valueOf(targetId));
            startActivity(intent);
            finish();
        } else  if (mseeageType == 2){
            final String trim = etSearch.getText().toString().trim();
            RongIMClient.getInstance().searchMessages(Conversation.ConversationType.GROUP, targetId, trim, 0, 0,
                    new RongIMClient.ResultCallback<List<Message>>() {
                        @Override
                        public void onSuccess(List<Message> searchConversationResults) {
                            Log.e("TAG_消息模糊搜索", "publicServiceData=" + searchConversationResults.size());
                            List<SearchModel> searchList = new ArrayList<SearchModel>();

                            for (int j = 0; j < searchConversationResults.size(); j++) {
                                Message mesage = searchConversationResults.get(j);

                                SearchModel searchModel = new SearchModel();
                                String targetId = mesage.getTargetId();
                                searchModel.setTargetId(targetId);
                                Log.e("TAG_消息模糊搜索group", "targetId=" + targetId);
                                BlackNumBean blackNumBean = blackDao.rawQuery(targetId);
                                String headUrl = blackNumBean.getHeadUrl();
                                searchModel.setHeadUrl(headUrl);
                                Log.e("TAG_消息模糊搜索group", "headUrl=" + headUrl);
                                String nick = blackNumBean.getNick();
                                searchModel.setNick(nick);
                                Log.e("TAG_消息模糊搜索group", "nick=" + nick);
                                MessageContent content = mesage.getContent();
                                int messageId = mesage.getMessageId();
                                searchModel.setLatestMessageId(messageId);
                                List<String> searchableWord = content.getSearchableWord();
                                if (searchableWord != null && searchableWord.size() > 0) {
                                    String messageCon = searchableWord.get(searchableWord.size() - 1);
                                    Log.e("TAG_消息模糊搜索private", "messageCon=" + messageCon);
                                    searchModel.setMessageCon(messageCon);
                                }
                                if (j == 0) {
                                    SearchModel searchModelTitle = new SearchModel();
                                    searchModelTitle.setMseeageType(0);
                                    searchModelTitle.setNick(nick+"的记录");
                                    searchList.add(searchModelTitle);
                                }
                                searchModel.setMseeageType(5);
                                searchList.add(searchModel);

                            }
                            adapterGroup.setData(searchList, trim);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
        }else  if (mseeageType == 5){
            SharePrefHelper.getInstance(this).putSpBoolean("isHistoryMessages", true);
            String targetId1 = searchModel.getTargetId();
            int latestMessageId = searchModel.getLatestMessageId();
            SharePrefHelper.getInstance(this).putSpInt("latestMessageId", latestMessageId);
            RongIM.getInstance().startGroupChat(this, targetId1, "");
        }
    }
}
