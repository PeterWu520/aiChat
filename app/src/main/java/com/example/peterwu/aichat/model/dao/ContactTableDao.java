package com.example.peterwu.aichat.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.peterwu.aichat.model.bean.UserInfo;
import com.example.peterwu.aichat.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

//联系人表的操作类
public class ContactTableDao {
    private DBHelper mHelper;
    public ContactTableDao(DBHelper helper) {
        mHelper = helper;

    }
    //获取所有联系人
    public List<UserInfo> getContacts(){
        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + ContactTable.CONTACT + " where " +
                ContactTable.IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfo> users = new ArrayList<>();

        while (cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.NAME)));
            userInfo.setNickName(cursor.getString(cursor.getColumnIndex(ContactTable.NICK_NAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.PHOTO)));
            //每一次遍历查找的联系人都封装到集合里
            users.add(userInfo);
        }
        //关闭资源
        cursor.close();
        
        //返回数据
        return users;
    }

    //通过环信id获取联系人单个信息
    public UserInfo getContactByHx(String hxId){
        if(hxId == null){
            return null;
        }
        //获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询
        String sql = "select * from " + ContactTable.CONTACT + " where " + ContactTable.HXID
                + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        UserInfo userInfo = null;
        if(cursor.moveToNext()){
            userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.NAME)));
            userInfo.setNickName(cursor.getString(cursor.getColumnIndex(ContactTable.NICK_NAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.PHOTO)));
        }
        //关闭资源
        cursor.close();
        //返回数据
        return userInfo;
    }

    //通过环信id获取用户联系人信息
    public List<UserInfo> getContactsByHx(List<String> hxIds){
        if(hxIds == null || hxIds.size() <= 0){
            return null;
        }

        List<UserInfo> contacts = new ArrayList<>();
        //遍历hxIds来查找
        for(String hxid : hxIds){
            UserInfo contact = getContactByHx(hxid);
            contacts.add(contact);
        }
        //返回数据
        return contacts;
    }

    //保存单个联系人
    public void saveContact(UserInfo user,boolean isMycontact){
        if(user == null){
            return;
        }
        //获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行保存语句
        ContentValues values = new ContentValues();
        values.put(ContactTable.HXID,user.getHxid());
        values.put(ContactTable.NAME,user.getName());
        values.put(ContactTable.NICK_NAME,user.getNickName());
        values.put(ContactTable.PHOTO,user.getPhoto());
        values.put(ContactTable.IS_CONTACT,isMycontact ? 1 : 0);
        db.replace(ContactTable.CONTACT,null,values);
    }

    //保存联系人信息
    public void saveContacts(List<UserInfo> contacts,boolean isMycontact){
        if(contacts == null || contacts.size() <= 0){
            return;
        }
        for(UserInfo contact : contacts){
            saveContact(contact,isMycontact);
        }
    }

    //删除联系人信息
    public void deleteContactByHxId(String hxId){
        if(hxId == null){
            return;
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ContactTable.CONTACT,ContactTable.HXID + "=?",
                new String[]{hxId});
    }

}
