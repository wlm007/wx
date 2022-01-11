package com.wlm.wxdemo.dao;

import com.wlm.wxdemo.model.user.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wuliming
 * @date 2022/1/5 11:06
 */
@Mapper
public interface UserDao {

    /**
     * 获取用户信息
     * @param id id
     * @return sysUser
     */
    SysUser getUserById(@Param("id") Integer id);
}
