package com.example.peterwu.aichat.controller.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.model.Model;
import com.example.peterwu.aichat.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

//添加联系人页面
public class AddContactActivity extends Activity {
    private TextView add_find;
    private EditText search_username;
    private RelativeLayout rl_add;
    private TextView add_username;
    private Button bt_add;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        //设置导航栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.GRAY);
        }
        //初始化页面
        initView();
        initListener();
    }

    private void initListener() {
        //查找按钮的点击事件
        add_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });

        //添加按钮的点击事件
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    //查找按钮处理
    private void find() {
        //获取输入的用户名称
        final String name = search_username.getText().toString();
        //校验输入的名称
        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this,"输入的用户名称不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //去服务器判断当前用户是否存在
        Model.getInstance().getAllThread().execute(new Runnable() {
            @Override
            public void run() {
                //去本地服务器判断当前用户是否存在
                userInfo = new UserInfo(name);
                //更新UI显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_add.setVisibility(View.VISIBLE);
                        add_username.setText(userInfo.getName());
                    }
                });
            }
        });
    }

    //添加按钮处理
    private void add() {
        Model.getInstance().getAllThread().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送邀请成功"
                            ,Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送邀请失败"
                             + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        add_find = (TextView)findViewById(R.id.add_find);
        search_username = (EditText)findViewById(R.id.search_username);
        rl_add = (RelativeLayout)findViewById(R.id.rl_add);
        add_username = (TextView)findViewById(R.id.add_username);
        bt_add = (Button)findViewById(R.id.bt_add);
    }
}
