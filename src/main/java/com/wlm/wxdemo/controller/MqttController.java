package com.wlm.wxdemo.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wlm.wxdemo.config.MqttSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuliming
 * @date 2022/1/12 10:31
 */
@Api(tags = "mqtt测试")
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    private MqttSender sender;

    @Autowired
    public void setSender(MqttSender sender) {
        this.sender = sender;
    }

    @ApiOperation(value = "mqtt发送测试")
    @ApiOperationSupport(author = "wlm", order = 1)
    @GetMapping("/send")
    public String mqttSend() {
        sender.send();
        return "SUCCESS";
    }

    @ApiOperation(value = "mqtt发送测试2")
    @ApiOperationSupport(author = "wlm", order = 2)
    @GetMapping("/send2")
    public String mqttSend2() {
        sender.sendMessage();
        return "SUCCESS";
    }
}
