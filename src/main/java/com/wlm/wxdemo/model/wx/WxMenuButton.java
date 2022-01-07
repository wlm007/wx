package com.wlm.wxdemo.model.wx;

import lombok.Data;

/**
 * 微信按钮实体类
 * @author wuliming
 * @date 2021/12/16 11:58
 */
@Data
public class WxMenuButton {

    private String type;
    private String name;
    private String key;
    private String url;
    private String appid;
    private String pagepath;
    private WxMenuButton[] sub_button;
}
