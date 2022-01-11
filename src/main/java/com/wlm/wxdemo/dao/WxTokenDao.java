package com.wlm.wxdemo.dao;

import com.wlm.wxdemo.model.wx.WxToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wuliming
 */
@Mapper
public interface WxTokenDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WxToken record);

    int insertSelective(WxToken record);

    WxToken selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(WxToken record);

    int updateByPrimaryKey(WxToken record);
}