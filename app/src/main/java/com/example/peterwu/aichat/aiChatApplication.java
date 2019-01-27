package com.example.peterwu.aichat;

import android.app.Application;
import android.content.Context;

import com.example.peterwu.aichat.model.Model;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

public class aiChatApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoAcceptGroupInvitation(false);
        EaseUI.getInstance().init(this,options);

        //初始化数据模型层类
        Model.getInstance().init(this);

        //初始化全局上下文对象
        mContext = this;
    }

    //获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
