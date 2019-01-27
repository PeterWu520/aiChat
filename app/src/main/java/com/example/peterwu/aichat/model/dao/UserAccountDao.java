package com.example.peterwu.aichat.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.peterwu.aichat.model.bean.UserInfo;
import com.example.peterwu.aichat.model.db.UserAccountDB;

//账号数据库操作类
public class UserAccountDao {
    private final UserAccountDB mHelper;
    public UserAccountDao(Context context) {
         mHelper = new UserAccountDB(context);
    }

    //添加用戶到数据库
    public void addAccount(UserInfo user){
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.HXID,user.getHxid());
        values.put(UserAccountTable.NAME,user.getName());
        values.put(UserAccountTable.NICKNAME,user.getNickName());
        values.put(UserAccountTable.PHOTO,user.getPhoto());
        db.replace(UserAccountTable.USER_ACCOUNT,null,values);
        //执行添加操作
    }

    //根据环信id获取用户所有信息
    public UserInfo getAccountByHxId(String hxId){
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + UserAccountTable.USER_ACCOUNT + " where "+ UserAccountTable.HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        UserInfo userInfo = null;
        if(cursor.moveToNext()){
            userInfo = new UserInfo();
            //封装对象
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.NAME)));
            userInfo.setNickName(cursor.getString(cursor.getColumnIndex(UserAccountTable.NICKNAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.PHOTO)));
        }
        //关闭资源
        cursor.close();
        //返回数据
        return userInfo;
    }
}
