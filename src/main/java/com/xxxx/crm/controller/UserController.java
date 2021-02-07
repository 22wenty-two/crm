package com.xxxx.crm.controller;

import com.xxxx.base.BaseController;
import com.xxxx.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 权限管理
 * @author arthur
 * @date 2021/1/23 12:06
 */
@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @return
     */
    @GetMapping("user/login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){
        ResultInfo resultInfo=new ResultInfo();
        UserModel userModel = userService.login(userName,userPwd);
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    /**
     * 密码修改
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @PostMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo=new ResultInfo();
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request),oldPassword,newPassword,confirmPassword);
        return resultInfo;
    }

    /**
     * 查询指派人
     * @return
     */
    @RequestMapping("user/sales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    /**
     * 权限管理
     *     用户管理：多条件查询
     * @param userQuery
     * @return
     */
    @RequestMapping("user/list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 用户管理模块
     * @return
     */
    @RequestMapping("user/index")
    public String index(){
        return "user/user";
    }

    /**
     * 用户管理
     *      用户添加
     * @param user
     * @return
     */
    @RequestMapping("user/save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("用户记录添加成功");
    }

    /**
     * 用户管理
     *      编辑
     * @param user
     * @return
     */
    @RequestMapping("user/update")
    @ResponseBody
    public ResultInfo updateSave(User user){
        userService.updateUser(user);
        return success("用户记录更新成功");
    }

    /**
     * 用户更新视图
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("user/addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id, Model model){
        model.addAttribute("user",userService.selectByPrimaryKey(id));
        return "user/add_update";
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @RequestMapping("user/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUserByIds(ids);
        return success("用户记录删除成功");
    }

    /**
     * 查询客户经理
     * @return
     */
    @RequestMapping("user/queryAllCustomerManager")
    @ResponseBody
    public List<Map<String,Object>> queryAllCustomerManager(){
        return userService.queryAllCustomerManager();
    }

}
