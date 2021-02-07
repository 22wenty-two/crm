package com.xxxx.crm.query;

import com.xxxx.base.BaseQuery;

/**
 * 角色管理搜索
 * @author arthur
 * @date 2021/2/3 11:46
 */
public class RoleQuery extends BaseQuery {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
