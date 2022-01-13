package com.wlm.wxdemo.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wuliming
 * @date 2022/1/12 10:27
 */
@Component
public class MqttSender {

    private AmqpTemplate amqpTemplate;

    @Autowired
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void send() {
        String context = "hello " + System.currentTimeMillis();
        System.out.println("Sender : " + context);
        this.amqpTemplate.convertAndSend("hello", context);
    }

    /**
     * 生产者将消息发送到交换机
     **/
    public void sendMessage() {
        String message1 = "主题模式-message-routingKey-my.hello.rabbit.kafka";
        String message2 = "主题模式-message-routingKey-my.hi";
        String message3 = "主题模式-message-routingKey-my.hello.hi";

        System.out.println("发送message1 : " + message1);
        amqpTemplate.convertAndSend("felix_topic_exchange","my.hello.rabbit.kafka", message1);

        System.out.println("发送message2 : " + message2);
        amqpTemplate.convertAndSend("felix_topic_exchange","rabbit.hi", message2);

        System.out.println("发送message2 : " + message3);
        amqpTemplate.convertAndSend("felix_topic_exchange", "my.hello.hi", message3);
    }

}
