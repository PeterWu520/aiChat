package com.example.peterwu.aichat.controller.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

//会话页面
public class ChatActivity extends FragmentActivity {
    private String userHxid;
    private EaseChatFragment easeChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //初始化数据
        initData();
    }


    private void initData() {
        //传入参数
        //userHxid = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));

        //创建一个会话的fragment
        easeChatFragment = new EaseChatFragment();

        //easeChatFragment.setArguments(getIntent().getExtras());
        easeChatFragment.setArguments(args);
        //替换fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_chat_layout,easeChatFragment).commit();

    }
}
