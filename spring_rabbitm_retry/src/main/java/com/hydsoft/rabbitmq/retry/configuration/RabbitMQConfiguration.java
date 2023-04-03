package com.hydsoft.rabbitmq.retry.configuration;

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
    public Queue directQueueExceptionRetry() {
        return new Queue("rabbit.test.queue.direct.exception.retry", true);
    }

    @Bean
    public Exchange directExchangeExceptionRetry() {
        return new DirectExchange("rabbit.test.exchange.direct.exception.retry", true, false);
    }

    @Bean
    public Binding directBindingExceptionRetry() {
        return BindingBuilder.bind(directQueueExceptionRetry()).to(directExchangeExceptionRetry()).with("retry").noargs();
    }

}
