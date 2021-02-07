package com.xxxx.crm.model;

/**
 * 用户数据模型
 * @author arthur
 * @date 2021/1/23 9:53
 */
public class UserModel {
    /**
     * userIdStr：用户id
     * userName：用户名称
     * trueName：用户真实名称
     */
    private String userIdStr;
    private String userName;
    private String trueName;


    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
