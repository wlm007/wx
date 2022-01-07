package com.wlm.wxdemo.model.wx;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信回复文本类型消息
 *
 * @author wuliming
 * @date 2022/1/5 17:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TextMessage extends BaseMessage {

    private String Content;
}
