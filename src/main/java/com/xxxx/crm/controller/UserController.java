package com.xxxx.crm.controller;

import com.xxxx.base.BaseController;
import com.xxxx.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 
 * @author arthur
 * @date 2021/1/23 12:06
 */
@RestController
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @return
     */
    @GetMapping("user/login")
    public ResultInfo login(String userName,String userPwd){
        ResultInfo resultInfo=new ResultInfo();
        try {
            UserModel userModel = userService.login(userName,userPwd);
            resultInfo.setResult(userModel);
        } catch (ParamsException e) {
            e.printStackTrace();
            resultInfo.setMsg(e.getMsg());
            resultInfo.setCode(e.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultInfo.setMsg("failed");
            resultInfo.setCode(500);
        }
        return resultInfo;
    }
}
