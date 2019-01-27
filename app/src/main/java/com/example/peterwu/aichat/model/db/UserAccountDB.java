package com.example.peterwu.aichat.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.peterwu.aichat.model.dao.UserAccountTable;

public class UserAccountDB extends SQLiteOpenHelper {

    public UserAccountDB(Context context) {
        super(context, "account.db", null, 1);
    }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserAccountTable.CREATE_USERACCOUNT);

    }

    //更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
