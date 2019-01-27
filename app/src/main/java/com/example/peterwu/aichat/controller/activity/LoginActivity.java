package com.example.peterwu.aichat.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.model.Model;
import com.example.peterwu.aichat.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginActivity extends Activity {
    private EditText login_name;
    private EditText login_password;
    private Button bt_login;
    private TextView tv_toregist;
    private long mExitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置导航栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.GRAY);
        }
        //初始化控件
        initView();
        //初始化监听
        initListener();
    }

    private void initListener() {
        //跳转到注册页面
        tv_toregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //登录按钮监听事件
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    //登录业务
    private void login() {
        //1.获取输入的用户名和密码
        final String loginName = login_name.getText().toString();
        final String loginPwd = login_password.getText().toString();
        //2.校验用户名和密码
        if(TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPwd)){
            Toast.makeText(LoginActivity.this,"输入的用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //3.登录处理
        Model.getInstance().getAllThread().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器登录
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    //登录成功
                    @Override
                    public void onSuccess() {
                        Model.getInstance().loginSuccess(new UserInfo(loginName));
                        //保存用户账号信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示登录成功
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                //跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                    //登录失败
                    @Override
                    public void onError(int i, final String s) {
                        //提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"登录失败"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //登录过程处理
                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }
    private void initView(){//初始化登录界面视图
        login_name = (EditText)findViewById(R.id.login_name);
        login_password = (EditText)findViewById(R.id.login_pwd);
        bt_login = (Button)findViewById(R.id.bt_login);
        tv_toregist = (TextView)findViewById(R.id.tv_toregist);
    }


    //再按一次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if (keyCode == event.KEYCODE_BACK){

            if ((System.currentTimeMillis() - mExitTime) > 2000){
                Toast.makeText(this, "再次点击退出爱聊", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
