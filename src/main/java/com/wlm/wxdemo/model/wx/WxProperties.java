package com.wlm.wxdemo.model.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wuliming
 * @date 2021/12/16 10:07
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxProperties {

    private String appId;

    private String appSecret;
}
