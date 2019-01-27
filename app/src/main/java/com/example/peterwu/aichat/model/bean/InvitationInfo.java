package com.example.peterwu.aichat.model.bean;

//邀请信息的bean类

public class InvitationInfo {
    private UserInfo user;  //联系人
    private String reason;  //邀请原因

    private InvitationStatus status; //邀请状态

    public InvitationInfo() {
    }

    public InvitationInfo(UserInfo user,  String reason, InvitationStatus status) {
        this.user = user;
        this.reason = reason;
        this.status = status;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public enum InvitationStatus{
        // 联系人邀请信息状态
        NEW_INVITE,// 新邀请
        INVITE_ACCEPT,//接受邀请
        INVITE_ACCEPT_BY_PEER,// 邀请被接受
    }

    @Override
    public String toString() {
        return "InvitationInfo{" +
                "user=" + user +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }
}
