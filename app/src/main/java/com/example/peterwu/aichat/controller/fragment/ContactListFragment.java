package com.example.peterwu.aichat.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.controller.activity.AddContactActivity;
import com.example.peterwu.aichat.controller.activity.ChatActivity;
import com.example.peterwu.aichat.controller.activity.InviteActivity;
import com.example.peterwu.aichat.model.Model;
import com.example.peterwu.aichat.utils.Constant;
import com.example.peterwu.aichat.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//联系人列表碎片

public class ContactListFragment extends EaseContactListFragment {
    //联系人变化广播
    private BroadcastReceiver ContactChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新页面
            refreshContacts();

        }
    };


    private ImageView contact_red;
    private LocalBroadcastManager localBroadcastManager;
    private LinearLayout contact_invite;
    private String userHxid;//当前联系人条目的环信id
    private BroadcastReceiver ContactInviteChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };

    @Override
    protected void initView() {
        super.initView();
        //布局显示加号
        titleBar.setRightImageResource(R.drawable.em_add);
        //添加头布局
        View headerView = View.inflate(getActivity(), R.layout.header_fragment_contact, null);
        listView.addHeaderView(headerView);

        //获取红点对象
        contact_red = (ImageView) headerView.findViewById(R.id.contact_red);

        //获取邀请信息条目的对象
        contact_invite = (LinearLayout )headerView.findViewById(R.id.contact_invite);

        //设置listview条目点击事件
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        //添加按钮的点击事件处理
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });
        //初始化红点显示
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        contact_red.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);

        //邀请信息条目的点击事件
        contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //红点处理
                contact_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,false);
                //跳转到邀请信息列表页面
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });


        //注册广播
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(ContactInviteChangeReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        localBroadcastManager.registerReceiver(ContactChangeReceiver,new IntentFilter(Constant.CONTACT_CHANGED));

        //从环信服务器获取所有的联系人信息
        getContactsList();

        //绑定listview和contextmenu
        registerForContextMenu(listView);

    }

    //添加长按联系人条目弹出删除的布局
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取当前位置的联系人数据
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
        userHxid = easeUser.getUsername();
        //添加布局
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }

    //长按联系人执行的操作
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.contact_delete){
            //删除选中的联系人
            deleteContact();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    //删除联系人
    private void deleteContact() {
        Model.getInstance().getAllThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(userHxid);
                    //更新数据库
                    Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(userHxid);

                    //切换到主线程更新页面
                    if (getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"联系人"+userHxid+"已删除",Toast.LENGTH_SHORT).show();
                            //刷新页面
                            refreshContacts();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    if (getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"联系人"+userHxid+"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getContactsList() {
        Model.getInstance().getAllThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Map<String, EaseUser> map = new HashMap<>();
                    List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (String userId : userNames){
                        map.put(userId,new EaseUser(userId));
                    }
                    if (getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //刷新页面
                            setContactsMap(map);
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void refreshContacts() {
        getContactsList();
    }

    //关闭广播
    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(ContactInviteChangeReceiver);
        localBroadcastManager.unregisterReceiver(ContactChangeReceiver);
    }
}
