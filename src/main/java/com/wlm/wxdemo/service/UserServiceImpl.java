package com.wlm.wxdemo.service;

import com.wlm.wxdemo.mapper.UserMapper;
import com.wlm.wxdemo.model.user.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuliming
 * @date 2022/1/5 11:02
 */
@Service
public class UserServiceImpl {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SysUser getUserById(Integer id) {
        return userMapper.getUserById(id);
    }
}
