package com.example.peterwu.aichat.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.peterwu.aichat.model.bean.InvitationInfo;
import com.example.peterwu.aichat.model.bean.UserInfo;
import com.example.peterwu.aichat.utils.Constant;
import com.example.peterwu.aichat.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

//全局事件监听类
public class AllEventListener {
    private Context mContext;
    private final LocalBroadcastManager localBroadcastManager;

    public AllEventListener(Context context) {
        mContext = context;

        //创建一个发送广播的管理者对象
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
    }

    //注册一个联系人变化的监听
    private final EMContactListener emContactListener = new EMContactListener() {
        //联系人增加后执行的方法
        @Override
        public void onContactAdded(String hxId) {
            //更新数据库
            Model.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo(hxId),true);
            //发送联系人变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxId) {
            //更新数据库
            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxId);
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxId);
            //发送联系人变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //接收到联系人的新邀请
        @Override
        public void onContactInvited(String hxId, String reason) {
            //数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxId));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);//新邀请
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送联系人变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人同意了你的好友邀请
        @Override
        public void onFriendRequestAccepted(String hxId) {
            //更新数据库
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxId));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);//别人同意了你的邀请
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送联系人变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人拒绝了你的好友邀请
        @Override
        public void onFriendRequestDeclined(String s) {
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送联系人变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
