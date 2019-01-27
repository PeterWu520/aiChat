package com.example.peterwu.aichat.controller.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.controller.fragment.ChatFragment;
import com.example.peterwu.aichat.controller.fragment.ContactListFragment;
import com.example.peterwu.aichat.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置导航栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.GRAY);
        }
        //初始化页面view
        initView();
        //初始化数据
        initData();
        //初始化监听
        initListener();
    }

    private void initListener() {
        //RadioGroup的选择事件
        main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId){
                    //会话列表界面
                    case R.id.main_conversation:
                        fragment = chatFragment;
                        break;
                        //通讯录列表界面
                    case R.id.main_contact:
                        fragment = contactListFragment;
                        break;
                        //设置页面
                    case R.id.main_setting:
                        fragment = settingFragment;
                        break;
                }
                //切换fragment的方法
                switchFragment(fragment);
            }
        });
        //默认选择会话列表界面
        main.check(R.id.main_conversation);
    }
    //切换fragment的方法
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    private void initData() {
        //创建三个fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }

    private void initView() {
        main = (RadioGroup)findViewById(R.id.main);
    }

    //实现双击返回键提示退出程序
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
