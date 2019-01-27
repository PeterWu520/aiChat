package com.example.peterwu.aichat.model.dao;

//联系人表的建表语句

public class ContactTable {
    public static final String CONTACT = "tab_contact";

    public static final String HXID = "hxid";
    public static final String NAME = "name";
    public static final String NICK_NAME = "nickName";
    public static final String PHOTO = "photo";
    public static final String IS_CONTACT = "is_contact"; //是否是联系人

    public static final String CREATE_CONTACT = "create table "
            + CONTACT + " ("
            + HXID + " text primary key,"
            + NAME + " text,"
            + NICK_NAME + "text,"
            + PHOTO + " text,"
            + IS_CONTACT + " integer);";

}
