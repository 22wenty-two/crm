package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * service：
 * @author arthur
 * @date 2021/1/22 21:10
 */
@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 登录参数校验
     *      1.参数校验
     *          用户名  非空
     *          密码    非空
     *      2.根据用户名  查询用户记录
     *      3.用户存在性校验
     *          不存在   -->记录不存在  方法结束
     *      4.用户存在
     *          校验密码
     *          密码错误 -->密码不正确   方法结束
     *      5.密码正确
     *          用户登录成功  返回用户信息
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel login(String userName,String userPwd){
        // 1.参数校验
        checkParams(userName,userPwd);
        // 2. 查询用户记录
        User user = userMapper.queryUserByUserName(userName);
        // 3.用户存在性校验
        AssertUtil.isTrue(user == null,"用户不存在");
        // 4.用户存在  5.密码正确
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"用户名或密码不正确");
        return buildUserModelInfo(user);
    }

    /**
     * 获取需要部分属性的洪湖模型
     * @return
     */
    public UserModel buildUserModelInfo(User user){
        UserModel userModel = new UserModel();
        // 将用户的id进行加密，存放在cookie中
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }

    /**
     * 修改用户的密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        // 参数校验
        checkParams(userId,oldPassword,newPassword,confirmPassword);
        // 更新密码,得到该更新的用户
        User user = userMapper.selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户密码更新失败！");

    }

    /**
     * 修改用户密码业务
     *      参数校验
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User temp =userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null== userId || null==temp,"用户未登录或不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码!");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码!");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码!");
        AssertUtil.isTrue(!(temp.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确!");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致!");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码与原始密码不能相同!");
    }

    /**
     * 查询指派人
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 客户管理
     *      多条件查询
     * @param userQuery
     * @return
     */
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        // 根据条件查询信息，如果为空，在查询所有
        PageInfo<User> pageInfo = new PageInfo<User>(selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }

    /**
     * 1.参数校验
     *     用户名 非空 值唯一
     *     email  非空  格式合法
     *     手机号非空  格式合法
     * 2.默认参数设置
     *     isValid  1
     *     createDate  系统时间
     *     updateDate 系统时间
     *     默认密码设置   123456
     * 3.执行添加
     * @param user
     */
    public void saveUser(User user){
        // 参数校验
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        AssertUtil.isTrue(null != userMapper.queryUserByUserName(user.getUserName()),"用户名不能重复");
        // 设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(insertSelective(user)<1,"添加失败");
        // 用户角色管理（t_user_role） user_id role_id
        // 获取添加的用户id主键
        Integer userId = userMapper.queryUserByUserName(user.getUserName()).getId();
        // 获取添加的角色id
        String roleIds = user.getRoleIds();
        /**
         * 批量添加用户角色记录到用户角色表
         */
        relationUserRoles(userId,roleIds);
        
    }

    /**
     * 批量添加角色记录到用户角色表
     * @param userId
     * @param roleIds
     */
    private void relationUserRoles(Integer userId, String roleIds) {
        // 获取该用户的角色数量
        int total = userRoleMapper.countUserRoleByUserId(userId);
        if (total>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=total,"用户角色关联失败");
        }
        if (StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoles = new ArrayList<>();
            for (String s: roleIds.split(",")){
                UserRole userRole = new UserRole();
                userRole.setCreateDate(new Date());
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setUpdateDate(new Date());
                userRole.setUserId(userId);
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles)!=userRoles.size(),"用户角色添加失败");
        }
    }

    /**
     * 参数校验
     *      添加用户
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"请输入用户名");
        AssertUtil.isTrue(StringUtils.isBlank(email),"请输入邮箱");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入电话号码");
    }

    /**
     * 更新用户
     * 1.参数校验
     *     id 记录存在
     *     用户名 非空 值唯一
     *     email  非空  格式合法
     *     手机号非空  格式合法
     * 2.默认参数设置
     *     updateDate 系统时间
     * 3.执行更新
     * @param user
     */
    public void updateUser(User user){
        User temp = selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(null == temp,"待更新的用户记录不存在");
        // 参数校验
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        temp = userMapper.queryUserByUserName(user.getUserName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(user.getId())),"该用户已存在");
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户记录更新失败");
        // 批量添加用户角色记录到用户角色表
        relationUserRoles(user.getId(),user.getRoleIds());
    }

    /**
     * 删除用户
     * @param ids
     */
    public void deleteUserByIds(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0,"请选择待删除的记录");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length,"用户记录删除失败");
    }

    public List<Map<String, Object>> queryAllCustomerManager() {
        return userMapper.queryAllCustomerManager();
    }
}
