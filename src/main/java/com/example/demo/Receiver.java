package com.example.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @RabbitListener(queues = "myQueue")
    public void processMessage(Message message) {
        byte[] body = message.getBody();
        System.out.println("收到消息: '" + new String(body) + "'");
    }

}