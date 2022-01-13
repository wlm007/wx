package com.wlm.wxdemo.config;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wuliming
 * @date 2022/1/12 10:29
 */
@Component
public class MqttReceiver {

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver : " + hello);
    }

    @RabbitListener(queues = "felix_queue_A")
    @RabbitHandler
    public void processA(String message) {
        System.out.println("消息路由到了队列A : " + message);
    }

    @RabbitListener(queues = "felix_queue_B")
    @RabbitHandler
    public void processB(String message) {
        System.out.println("消息路由到了队列B : " + message);
    }
}

