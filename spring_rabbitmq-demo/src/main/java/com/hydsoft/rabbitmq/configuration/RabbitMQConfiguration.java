package com.hydsoft.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: RabbitMQConfiguration
 * @Description:
 * @Author Jane
 * @Date: 2022/7/4 14:31
 * @Version 1.0
 */
@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue directQueue() {
        return new Queue("rabbit.test.queue.direct", true);
    }

    @Bean
    public Exchange directExchange() {
        return new DirectExchange("rabbit.test.exchange.direct", true, false);
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("direct").noargs();
    }

    @Bean
    public Queue fanoutQueue1() {
        Queue queue = new Queue("rabbit.test.queue.fanout1", true);
        return queue;
    }

    @Bean
    public Queue fanoutQueue2() {
        Queue queue = new Queue("rabbit.test.queue.fanout2", true);
        return queue;
    }

    @Bean
    public Exchange fanoutExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange("rabbit.test.exchange.fanout", true, false);
        return fanoutExchange;
    }

    @Bean
    public Binding fanoutBind1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange()).with("unUseful").noargs();
    }

    @Bean
    public Binding fanoutBind2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange()).with("unUseful").noargs();
    }

    @Bean
    public Queue topicQueue1() {
        return new Queue("rabbit.test.queue.topic1", true);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("rabbit.test.queue.topic2", true);
    }

    @Bean
    public Exchange topicExchange() {
        return new TopicExchange("rabbit.test.exchange.topic", true, false);
    }

    @Bean
    public Binding topicBind1() {
        return BindingBuilder.bind(topicQueue1() ).to(topicExchange()).with(".123").noargs();
    }

    @Bean
    public Binding topicBind2() {
        return BindingBuilder.bind(topicQueue2() ).to(topicExchange()).with("#.123").noargs();
    }

    @Bean
    public Queue directQueueException() {
        return new Queue("rabbit.test.queue.direct.exception", true);
    }

    @Bean
    public Exchange directExchangeException() {
        return new DirectExchange("rabbit.test.exchange.direct.exception", true, false);
    }

    @Bean
    public Binding directBindingException() {
        return BindingBuilder.bind(directQueueException()).to(directExchangeException()).with("exception").noargs();
    }

}
