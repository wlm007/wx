package com.wlm.wxdemo.model.wx;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图文消息
 * @author wuliming
 * @date 2022/1/10 14:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticlesMessage extends BaseMessage{

    private Integer ArticleCount;

    private Articles Articles;
}
