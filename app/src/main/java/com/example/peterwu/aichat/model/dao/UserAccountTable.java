package com.example.peterwu.aichat.model.dao;

public class UserAccountTable {
    public static final String USER_ACCOUNT = "tab_account";
    public static final String NAME = "name";
    public static final String HXID = "hxid";
    public static final String NICKNAME = "nickname";
    public static final String PHOTO = "photo";

    public static final String CREATE_USERACCOUNT = "create table "
            + USER_ACCOUNT + " ("
            + HXID + " text primary key,"
            + NAME + " text,"
            + NICKNAME + " text,"
            + PHOTO + " text);";
}
