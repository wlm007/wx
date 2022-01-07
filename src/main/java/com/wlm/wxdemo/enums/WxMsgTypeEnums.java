package com.wlm.wxdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信消息类型
 * @author wuliming
 * @date 2022/1/6 10:49
 */
@Getter
@AllArgsConstructor
public enum WxMsgTypeEnums {

    /**
     * 微信消息类型
     */
    WX_MSG_TYPE_TEXT("text", "文本"),
    WX_MSG_TYPE_IMAGE("image", "图片"),
    WX_MSG_TYPE_VOICE("voice", "语音"),
    WX_MSG_TYPE_VIDEO("video", "视频"),
    WX_MSG_TYPE_MUSIC("music", "音乐"),
    WX_MSG_TYPE_NEWS("mews", "图文"),
    WX_MSG_TYPE_SHORT_VIDEO("shortvideo", "小视频"),
    WX_MSG_TYPE_LOCATION("location", "地理位置"),
    WX_MSG_TYPE_LINK("link", "链接"),
    WX_MSG_TYPE_EVENT("event", "事件"),
    ;

    private final String msgType;

    private final String explain;
}
