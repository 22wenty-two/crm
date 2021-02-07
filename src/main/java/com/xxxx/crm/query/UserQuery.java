package com.xxxx.crm.query;

import com.xxxx.base.BaseQuery;

/**
 * 权限管理
 * @author arthur
 * @date 2021/1/28 16:57
 */
public class UserQuery extends BaseQuery {
    private String userName;
    private String email;
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
