package com.example.peterwu.aichat.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.peterwu.aichat.model.bean.InvitationInfo;
import com.example.peterwu.aichat.model.bean.UserInfo;
import com.example.peterwu.aichat.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

//邀请信息表的操作类
public class InviteTableDao {
    private DBHelper mHelper;
    public InviteTableDao(DBHelper helper) {
        mHelper = helper;
    }

    //添加邀请
    public void addInvitation(InvitationInfo invitationInfo){
        //获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行添加语句
        ContentValues values = new ContentValues();
        values.put(InviteTable.REASON,invitationInfo.getReason());// 原因
        values.put(InviteTable.STATUS,invitationInfo.getStatus().ordinal());//状态
        UserInfo user = invitationInfo.getUser();
        if(user != null){//如果邀请信息里有联系人就是邀请个人
            values.put(InviteTable.USER_HXID,invitationInfo.getUser().getHxid());
            values.put(InviteTable.USER_NAME,invitationInfo.getUser().getName());
        }
//        else {//否则就是群组邀请
//            values.put(InviteTable.GROUP_HXID,invitationInfo.getGroup().getGroupId());
//            values.put(InviteTable.GROUP_NAME,invitationInfo.getGroup().getGroupName());
//            values.put(InviteTable.USER_HXID,invitationInfo.getGroup().getInvitePerson());
//        }
        db.replace(InviteTable.INVITE,null,values);
    }

    //获取所有邀请信息
    public List<InvitationInfo> getInvitations(){
        //获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询
        String sql = "select * from " + InviteTable.INVITE;
        Cursor cursor = db.rawQuery(sql, null);
        List<InvitationInfo> invitationInfos = new ArrayList<>();
        while (cursor.moveToNext()){
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.REASON)));
            invitationInfo.setStatus(intInvitationStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.STATUS))));

            String userId = cursor.getString(cursor.getColumnIndex(InviteTable.USER_HXID));
            if(userId != null){//联系人的邀请信息
                UserInfo userInfo = new UserInfo();
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.USER_NAME)));
                userInfo.setNickName(cursor.getString(cursor.getColumnIndex(InviteTable.USER_NAME)));

                invitationInfo.setUser(userInfo);
            }
            //添加本次循环的邀请信息到集合中
            invitationInfos.add(invitationInfo);
        }
        //关闭资源
        cursor.close();
        //返回数据
        return invitationInfos;
    }

    //将int类型状态转换为邀请的状态
    private InvitationInfo.InvitationStatus intInvitationStatus(int intStatus){
        //比较枚举的序号，返回对应的状态
        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        return null;
    }

    //删除邀请
    public void removeInvitation(String hxId){
        if(hxId == null){
            return;
        }
        //获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行删除语句
        db.delete(InviteTable.INVITE,InviteTable.USER_HXID + "=?",
                new String[]{hxId});
    }

    //更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String hxId){
        if(hxId == null){
            return;
        }
        //获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行更新语句
        ContentValues values = new ContentValues();
        values.put(InviteTable.STATUS,invitationStatus.ordinal());
        db.update(InviteTable.INVITE,values,InviteTable.USER_HXID + "=?",
                new String[]{hxId});
    }
}
