package com.example.peterwu.aichat.model;

import android.content.Context;

import com.example.peterwu.aichat.model.bean.UserInfo;
import com.example.peterwu.aichat.model.dao.UserAccountDao;
import com.example.peterwu.aichat.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//数据模型层全局类
public class Model {
    private Context mContext;
    private UserAccountDao userAccountDao;
    private ExecutorService executors = Executors.newCachedThreadPool();
    private DBManager dbManager;
    //创建对象
    private static Model model = new Model();

    //私有化构造
    private Model(){

    }

    //获取单例对象
    public static Model getInstance(){
        return  model;
    }

    //初始化的方法
    public void init(Context context){
        mContext = context;
        //创建用户账号数据库操作类的对象
        userAccountDao = new UserAccountDao(mContext);
        
        //开启全局监听
        AllEventListener eventListener = new AllEventListener(mContext);
    }

    //获取全局线程池
    public ExecutorService getAllThread(){
        return executors;
    }

    //用户登录成功后的处理方法
    public void loginSuccess(UserInfo account) {
        //校验账户
        if(account == null){
            return;
        }
        if(dbManager != null){
            dbManager.close();
        }
        dbManager = new DBManager(mContext, account.getName());
    }
    public DBManager getDbManager(){
        return dbManager;
    }
    //获取用户账号数据库的操作类
    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }
}
