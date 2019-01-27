package com.example.peterwu.aichat.controller.fragment;

import android.content.Intent;
import com.example.peterwu.aichat.controller.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;
//会话列表页面碎片

public class ChatFragment extends EaseConversationListFragment {


    @Override
    protected void initView() {
        super.initView();

        //跳转到会话页面
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                startActivity(intent);
            }
        });

        //conversationList.clear();

        //监听会话消息
        EMClient.getInstance().chatManager().addMessageListener(aiMessageListener);
    }

    private EMMessageListener aiMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //设置数据
            EaseUI.getInstance().getNotifier().notify(list);
            //刷新页面
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
}