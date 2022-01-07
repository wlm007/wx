package com.wlm.wxdemo.model.wx;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信图片消息
 * @author wuliming
 * @date 2022/1/6 14:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImageMessage extends BaseMessage{

    private Image Image;
}
