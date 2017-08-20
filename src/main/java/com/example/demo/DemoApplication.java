package com.example.demo;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
//        SpringApplication.run(DemoApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        int i =0 ;
        try {
            while (true){
                Sender sender = context.getBean("sender", Sender.class);
                sender.sendMsg("测试Spring AMQP发送消息"+i);
                Thread.sleep(1000);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.close();
    }


    @Bean
    CachingConnectionFactory myConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setHost("192.168.28.133");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    Exchange myExchange() {
        return  ExchangeBuilder.topicExchange("test.topic").durable().build();
    }

    @Bean
    Queue myQueue() {
        return QueueBuilder.durable("myQueue").build();
    }

    @Bean
    public Binding myExchangeBinding(@Qualifier("myExchange") Exchange topicExchange,
                                     @Qualifier("myQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with("test.#").noargs();
    }

    @Bean
    public RabbitTemplate myExchangeTemplate(CachingConnectionFactory myConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(myConnectionFactory);
        rabbitTemplate.setExchange("test.topic");
        rabbitTemplate.setRoutingKey("test.abc.123");
        return rabbitTemplate;
    }
}