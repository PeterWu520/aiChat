package com.example.peterwu.aichat.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.peterwu.aichat.R;
import com.example.peterwu.aichat.model.bean.InvitationInfo;
import com.example.peterwu.aichat.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

//邀请信息适配器

public class InviteAdapter extends BaseAdapter {
    private Context mContext;//接收上下文
    private onInviteListener monInviteListener;
    private List<InvitationInfo> mInvitationInfos = new ArrayList<>();//邀请信息集合
    private InvitationInfo invitationInfo;

    public InviteAdapter(Context context,onInviteListener onInviteListener) {
        mContext = context;

        monInviteListener = onInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvitationInfo> invitationInfos){
        if(invitationInfos != null && invitationInfos.size() >= 0){

            mInvitationInfos.clear();
            mInvitationInfos.addAll(invitationInfos);
            //通知刷新页面
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取或创建viewHolder
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            //获取布局
            convertView = View.inflate(mContext, R.layout.item_invite,null);
            holder.name = (TextView) convertView.findViewById(R.id.invite_name);
            holder.reason = (TextView) convertView.findViewById(R.id.invite_reason);
            holder.accept = (Button) convertView.findViewById(R.id.bt_accept);
            holder.reject = (Button) convertView.findViewById(R.id.bt_reject);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取当前item数据
        invitationInfo = mInvitationInfos.get(position);
        //显示当前item数据
        UserInfo user = invitationInfo.getUser();//判断当前用户是群组还是联系人
        if (user != null){//当前用户为联系人
            //名称的显示
            holder.name.setText(invitationInfo.getUser().getName());

            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            //显示原因
            if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE){//新邀请

                if (invitationInfo.getReason() == null){
                    holder.reason.setText("添加好友");
                }else {
                    holder.reason.setText(invitationInfo.getReason());
                }

                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);

            }else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT){//接受邀请
                if (invitationInfo.getReason() == null){
                    holder.reason.setText("接受邀请");
                }else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            }else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){//邀请被接受
                if (invitationInfo.getReason() == null){
                    holder.reason.setText("邀请被接受");
                }else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            }

            //按钮的处理
            //同意按钮事件
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monInviteListener.onAccept(invitationInfo);
                }
            });

            //拒绝按钮事件
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monInviteListener.onReject(invitationInfo);
                }
            });

        }else {//群组邀请信息

        }
        //返回view
        return convertView;
    }

    private class ViewHolder{
        //存放控件实例
        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;
    }

    //定义一个接口，方便调用
    public interface onInviteListener{
        //联系人接受按钮的点击事件
        void onAccept(InvitationInfo invitationInfo);

        //联系人拒绝按钮的点击事件
        void onReject(InvitationInfo invitationInfo);
    }
}
