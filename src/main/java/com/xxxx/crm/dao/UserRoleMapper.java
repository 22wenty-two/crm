package com.xxxx.crm.dao;

import com.xxxx.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    /**
     * 用户角色数量
     * @param userId
     * @return
     */
    int countUserRoleByUserId(Integer userId);

    /**
     * 删除用户所有的角色
     * @param userId
     * @return
     */
    int  deleteUserRoleByUserId(Integer userId);;

    /**
     * 查询关联该角色的用户数
     * @param roleId
     * @return
     */
    int countUserRoleByRoleId(Integer roleId);


    int deleteUserRoleByRoleId(Integer roleId);
}