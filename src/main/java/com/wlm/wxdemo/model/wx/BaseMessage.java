package com.wlm.wxdemo.model.wx;

import lombok.Data;

/**
 * 微信回复消息基类
 * @author wuliming
 * @date 2022/1/5 17:29
 */
@Data
public class BaseMessage {

    /**
     * 接收方账号（收到的openId）
     */
    private String ToUserName;

    /**
     * 开发者微信号
     */
    private String FromUserName;

    /**
     * 创建时间
     */
    private Long CreateTime;

    /**
     * 消息类型（text/image/voice/video/music/mews）
     */
    private String MsgType;
}
