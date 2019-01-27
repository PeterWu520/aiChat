package com.example.peterwu.aichat.controller.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.controller.activity.LoginActivity;
import com.example.peterwu.aichat.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
//设置页面碎片

public class SettingFragment extends Fragment {
    private Button bt_exit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        bt_exit = (Button)view.findViewById(R.id.bt_exit);
        
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        //在退出按钮显示当前用户名
        bt_exit.setText("退出登录(" + EMClient.getInstance().getCurrentUser() +")");
        //退出登录并返回登录界面
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //弹出是否退出对话框
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("退出登录")
                        .setMessage("确定要退出吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logOut();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    private void logOut() {
        Model.getInstance().getAllThread().execute(new Runnable() {
            @Override
            public void run() {
                //登录环信服务器退出登录
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //关闭DBhelper
                        Model.getInstance().getDbManager().close();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI显示
                                Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                                //回到登录页面
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, final String s) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"退出失败" + s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
}
