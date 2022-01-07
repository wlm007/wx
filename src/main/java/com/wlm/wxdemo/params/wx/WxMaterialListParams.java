package com.wlm.wxdemo.params.wx;

import lombok.Data;

/**
 * @author wuliming
 * @date 2022/1/6 16:40
 */
@Data
public class WxMaterialListParams {

    private String type;

    private Integer offset = 0;

    private Integer count = 20;
}
