package com.hydsoft.rabbitmq.manual.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: ManualConfiguration
 * @Description:
 * @Author Jane
 * @Date: 2022/7/7 9:24
 * @Version 1.0
 */
@Configuration
public class ManualConfiguration {

    @Bean
    public Queue directQueueManual() {
        return new Queue("rabbit.test.queue.direct.manual", true);
    }

    @Bean
    public Exchange directExchangeManual() {
        return new DirectExchange("rabbit.test.exchange.direct.manual", true, false);
    }

    @Bean
    public Binding directBindingManual() {
        return BindingBuilder.bind(directQueueManual()).to(directExchangeManual()).with("manual").noargs();
    }

    @Bean
    public Queue delayQueue() {
        return new Queue("rabbit.test.queue.delay", true);
    }

    @Bean
    public Exchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("rabbit.test.exchange.delay", "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with("delay").noargs();
    }

    /**
     * @Description: 死信队列
     * @Author Jane
     * @Date: 2022/7/7 14:15
     * @Version 1.0
     */
    @Bean
    public Queue deadQueue() {
        return new Queue("rabbit.test.queue.dead", true);
    }

    @Bean
    public Exchange deadExchange() {
        return new DirectExchange("rabbit.test.exchange.dead", true, false);
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("dead").noargs();
    }

    /**
     * @Description: 业务队列绑定死信交换机
     * @Author Jane
     * @Date: 2022/7/7 14:22
     * @Version 1.0
     */
    @Bean
    public Queue timeoutQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "rabbit.test.exchange.dead");
        args.put("x-dead-letter-routing-key", "dead");
        //队列过期时间30000ms
        args.put("x-message-ttl", 30000);
        return new Queue("rabbit.test.queue.timeout", true, false ,false, args);
    }

    @Bean
    public Exchange timeoutExchange() {
        return new DirectExchange("rabbit.test.exchange.timeout", true, false);
    }

    @Bean
    public Binding timeoutBinding() {
        return BindingBuilder.bind(timeoutQueue()).to(timeoutExchange()).with("timeout").noargs();
    }

    @Bean
    public Queue businessDeadQueue() {
        return new Queue("rabbit.test.queue.business.dead", true);
    }

    @Bean
    public Exchange businessDeadExchange() {
        return new DirectExchange("rabbit.test.exchange.business.dead", true, false);
    }

    @Bean
    public Binding businessDeadBinding() {
        return BindingBuilder.bind(businessDeadQueue()).to(businessDeadExchange()).with("business").noargs();
    }

    /**
     * @Description: 业务队列绑定死信业务队列
     * @Author Jane
     * @Date: 2022/7/7 14:44
     * @Version 1.0
     */
    @Bean
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "rabbit.test.exchange.business.dead");
        //不配置x-dead-letter-routing-key 默认使用业务列队的routingKey
        //args.put("x-dead-letter-routing-key", "business");
        return new Queue("rabbit.test.queue.business", true, false ,false, args);
    }

    @Bean
    public Exchange businessExchange() {
        return new DirectExchange("rabbit.test.exchange.business", true, false);
    }

    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue()).to(businessExchange()).with("business").noargs();
    }
}
