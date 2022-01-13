package com.wlm.wxdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuliming
 * @date 2022/1/11 15:40
 */
@Slf4j
@Configuration
public class MqttConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    @Bean
    public Queue felixQueueA(){
        return new Queue("felix_queue_A");
    }

    @Bean
    public Queue felixQueueB(){
        return new Queue("felix_queue_B");
    }

    /**
     *  声明一个Topic类型的交换机
     * @return org.springframework.amqp.core.DirectExchange
     **/
    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange("felix_topic_exchange");
    }

    /**
     * 将上面的2个队列绑定到Topic交换机
     * @return org.springframework.amqp.core.Binding
     **/
    @Bean
    Binding bindExchangeA(Queue felixQueueA, TopicExchange topicExchange){
        return BindingBuilder.bind(felixQueueA).to(topicExchange).with("*.hello.#");
    }

    @Bean
    Binding bindExchangeB(Queue felixQueueB,TopicExchange topicExchange){
        return BindingBuilder.bind(felixQueueB).to(topicExchange).with("*.hi");
    }
}
