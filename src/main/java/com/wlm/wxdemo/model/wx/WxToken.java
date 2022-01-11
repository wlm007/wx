package com.wlm.wxdemo.model.wx;

import lombok.Data;

import java.io.Serializable;

/**
 * wx_token
 * @author wuliming
 */
@Data
public class WxToken implements Serializable {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 微信access_token
     */
    private String accessToken;

    /**
     * 最新获取时间
     */
    private Long updateTime;

    private static final long serialVersionUID = 1L;
}