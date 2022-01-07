package com.wlm.wxdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信事件类型
 * @author wuliming
 * @date 2022/1/6 10:49
 */
@Getter
@AllArgsConstructor
public enum WxEventEnums {

    /**
     * 微信事件类型
     */
    WX_EVENT_TYPE_SUBSCRIBE("subscribe", "订阅"),
    WX_EVENT_TYPE_UNSUBSCRIBE("unsubscribe", "取消订阅"),
    WX_EVENT_TYPE_SCAN("SCAN", "用户已关注时扫二维码事件"),
    WX_EVENT_TYPE_LOCATION("LOCATION", "上报地理位置"),
    WX_EVENT_TYPE_CLICK("CLICK", "点击菜单拉取消息"),
    WX_EVENT_TYPE_VIEW("CLICK", "点击菜单跳转链接"),
    ;

    private final String eventType;

    private final String explain;
}
