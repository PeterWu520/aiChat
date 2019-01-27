package com.example.peterwu.aichat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.peterwu.aichat.aiChatApplication;
//保存
//获取数据
public class SpUtils {
    public static final String IS_NEW_INVITE = "is_new_invite";//新的邀请标记
    private static SpUtils instance = new SpUtils();
    private static SharedPreferences preferences;

    private SpUtils() {

    }

    //单例
    public static SpUtils getInstance(){
        if(preferences == null){
            preferences = aiChatApplication.getGlobalApplication().getSharedPreferences("aiChat", Context.MODE_PRIVATE);
        }
        return instance;
    }

    //保存
    public void save(String key,Object value){
        if(value instanceof String){
            preferences.edit().putString(key,(String) value).commit();
        }else if(value instanceof Boolean){
            preferences.edit().putBoolean(key, (Boolean) value).commit();
        }else if(value instanceof Integer){
            preferences.edit().putInt(key, (Integer) value).commit();
        }
    }

    //获取数据的方法
    public String getString(String key,String defValue){
        return preferences.getString(key,defValue);
    }

    //获取Boolean数据
    public Boolean getBoolean(String key,boolean defValue){
        return preferences.getBoolean(key,defValue);
    }

    //获取int类型数据
    public int getInt(String key,int defValue){
        return preferences.getInt(key,defValue);
    }
}
