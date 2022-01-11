package com.wlm.wxdemo.service;

import com.wlm.wxdemo.dao.UserDao;
import com.wlm.wxdemo.model.user.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuliming
 * @date 2022/1/5 11:02
 */
@Service
public class UserServiceImpl {

    private UserDao userDao;

    @Autowired
    public void setUserMapper(UserDao userDao) {
        this.userDao = userDao;
    }

    public SysUser getUserById(Integer id) {
        return userDao.getUserById(id);
    }
}
