package com.sunshine.springbootinit.bizmq;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

// 使用@Component注解标记该类为一个组件，让Spring框架能够扫描并将其纳入管理
@Component
public class BiMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message    消息内容，要发送的具体消息
     */
    public void sendMessage(String message){
        // 使用rabbitTemplate的convertAndSend方法将消息发送到指定的交换机和路由键
        rabbitTemplate.convertAndSend(BiMqConstant.BI_EXCHANGE_NAME,BiMqConstant.BI_ROUTING_KEY,message);
    }

}
