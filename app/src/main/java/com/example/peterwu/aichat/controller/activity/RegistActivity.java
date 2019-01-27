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
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class RegistActivity extends Activity {
    private EditText registName;
    private EditText registPassword;
    private EditText confirm_password;
    private Button bt_regist;
    private Button bt_regist_back;
    private long mExitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
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
        //注册按钮事件
        bt_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册按钮事件处理
                //获取输入框的用户名和密码
                final String registName = RegistActivity.this.registName.getText().toString();
                final String registPwd = registPassword.getText().toString();
                final String confirmPwd = confirm_password.getText().toString();
                //判断输入框是否为空
                if(TextUtils.isEmpty(registName)){
                    Toast.makeText(RegistActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    RegistActivity.this.registName.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(registPwd)){
                    Toast.makeText(RegistActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    registPassword.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(confirmPwd)){
                    Toast.makeText(RegistActivity.this,"确认密码不能为空",Toast.LENGTH_SHORT).show();
                    confirm_password.requestFocus();
                    return;
                }else if(!registPwd.equals(confirmPwd)){
                    Toast.makeText(RegistActivity.this,"两次输入的密码不同，请重新输入"
                    ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(registName) && (!TextUtils.isEmpty(registPwd))){
                    //去服务器注册账号
                    Model.getInstance().getAllThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(registName,registPwd);
                                //更新页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegistActivity.this,"注册成功",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegistActivity.this,"注册失败"
                                         + e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        //注册页面标题栏返回键事件处理
        bt_regist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //初始化控件
    private void initView() {
        registName = (EditText)findViewById(R.id.search_username);
        registPassword = (EditText)findViewById(R.id.registPassword);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        bt_regist = (Button)findViewById(R.id.bt_regist);
        bt_regist_back = (Button)findViewById(R.id.bt_regist_back);
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
