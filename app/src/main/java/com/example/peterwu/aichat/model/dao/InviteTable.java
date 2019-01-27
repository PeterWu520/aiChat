package com.example.peterwu.aichat.model.dao;

//邀请信息表
public class InviteTable {
    public static final String INVITE = "tab_invite";

    public static final String USER_HXID = "user_hxid";//用户的环信id
    public static final String USER_NAME = "user_name";//用户的名称
    public static final String REASON = "reason";//邀请原因
    public static final String STATUS = "status";//邀请状态

    public static final String CREATE_INVITE = "create table "
            + INVITE + " ("
            + USER_HXID + " text primary key,"
            + USER_NAME + " text,"
            + REASON + " text,"
            + STATUS + " integer);";
}
