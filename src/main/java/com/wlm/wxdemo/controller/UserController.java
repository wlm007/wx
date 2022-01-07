package com.wlm.wxdemo.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wlm.wxdemo.model.user.SysUser;
import com.wlm.wxdemo.service.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuliming
 * @date 2022/1/5 10:58
 */
@Api(tags = "系统用户")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }


    @ApiOperation(value = "获取用户信息")
    @ApiOperationSupport(author = "wlm", order = 1)
    @GetMapping("/get_user/{id}")
    public SysUser getToken(@PathVariable Integer id) {
        return userService.getUserById(id);
    }
}
