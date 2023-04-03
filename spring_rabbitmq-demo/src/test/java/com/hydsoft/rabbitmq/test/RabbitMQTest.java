package com.hydsoft.rabbitmq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @title: RabbitMQTest
 * @Description:
 * @Author Jane
 * @Date: 2022/7/4 11:11
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * @Description: 测试 DirectExchange 根据routingKey路由到队列
     * spring.rabbitmq.publisher-confirm-type=correlated 消息确认模式 消息发送之后回调ConfirmCallback
     * 发送到交换机 ack=true
     * 找不到交换机 ack=false
     * spring.rabbitmq.publisher-returns=true(spring.rabbitmq.template.mandatory=true) 消息发送到交换机之后 路由到队列
     * 当消息路由不到队列时 回调ReturnCallback方法
     * @Author Jane
     * @Date: 2022/7/5 15:31
     * @Version 1.0
     */
    @Test
    public void test01() {
        //do something
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause)->{
            logger.info("ConfirmCallback 相关数据:{}", correlationData);
            logger.info("ConfirmCallback 确认情况:{}", ack);
            logger.info("ConfirmCallback 原因:{}", cause);
            if (!ack) {
                //do something callback
                logger.error("消息发送失败，找不到交换机，请检查配置");
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)->{
            //do something callback
            logger.error("消息发送失败，无法路由到对应队列，请检查配置，exchange：{}，routingKey：{}", exchange, routingKey);
            logger.info("ReturnCallback 消息:{}", message);
            logger.info("ReturnCallback 响应码:{}", replyCode);
            logger.info("ReturnCallback 响应消息:{}", replyText);
            logger.info("ReturnCallback 交换机:{}", exchange);
            logger.info("ReturnCallback 路由key:{}", routingKey);
        });
        rabbitTemplate.convertAndSend("rabbit.test.exchange.direct", "direct", "this is a direct message to rabbit.test.queue.direct");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 测试找不到Exchange
     * @Author Jane
     * @Date: 2022/7/6 10:06
     * @Version 1.0
     */
    @Test
    public void test02() {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause)->{
            logger.info("ConfirmCallback 相关数据:{}", correlationData);
            logger.info("ConfirmCallback 确认情况:{}", ack);
            logger.info("ConfirmCallback 原因:{}", cause);
            if (!ack) {
                logger.error("消息发送失败，找不到交换机，请检查配置");
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)->{
            logger.error("消息发送失败，无法路由到对应队列，请检查配置，exchange：{}，routingKey：{}", exchange, routingKey);
            logger.info("ReturnCallback 消息:{}", message);
            logger.info("ReturnCallback 响应码:{}", replyCode);
            logger.info("ReturnCallback 响应消息:{}", replyText);
            logger.info("ReturnCallback 交换机:{}", exchange);
            logger.info("ReturnCallback 路由key:{}", routingKey);
        });
        rabbitTemplate.convertAndSend("rabbit.test.exchange.noDirect", "direct", "this is a direct message to rabbit.test.queue.direct");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 测试路由不到队列
     * @Author Jane
     * @Date: 2022/7/6 10:09
     * @Version 1.0
     */
    @Test
    public void test03() {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause)->{
            logger.info("ConfirmCallback 相关数据:{}", correlationData);
            logger.info("ConfirmCallback 确认情况:{}", ack);
            logger.info("ConfirmCallback 原因:{}", cause);
            if (!ack) {
                logger.error("消息发送失败，找不到交换机，请检查配置");
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)->{
            logger.error("消息发送失败，无法路由到对应队列，请检查配置，exchange：{}，routingKey：{}", exchange, routingKey);
            logger.info("ReturnCallback 消息:{}", message);
            logger.info("ReturnCallback 响应码:{}", replyCode);
            logger.info("ReturnCallback 响应消息:{}", replyText);
            logger.info("ReturnCallback 交换机:{}", exchange);
            logger.info("ReturnCallback 路由key:{}", routingKey);
        });
        rabbitTemplate.convertAndSend("rabbit.test.exchange.direct", "noDirect", "this is a direct message to rabbit.test.queue.direct");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description: 测试 FanoutExchange  自动ack模式
     * @Author Jane
     * @Date: 2022/7/5 15:10
     * @Version 1.0
     */
    @Test
    public void test04() {
        for(int i = 0 ; i < 10 ; i ++) {
            rabbitTemplate.convertAndSend("rabbit.test.exchange.fanout", "not use routeKey", "This is the " + i + " test message for fanoutExchange");
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 测试TopicExchange
     * topic类型的Exchange会根据通配符对Routing key进行匹配，只要Routing key满足某个通配符的条件，就会被路由到对应的Queue上。通配符的匹配规则如下：
     * Routing key必须是一串字符串，每个单词用“.”分隔；
     * 符号“#”表示匹配一个或多个单词；
     * 符号“*”表示匹配一个单词。
     * @Author Jane
     * @Date: 2022/7/6 10:25
     * @Version 1.0
     */
    @Test
    public void test05() {
        rabbitTemplate.convertAndSend("rabbit.test.exchange.topic", "abc.def.123", "this is a topic message");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 测试自动ack模式下 消费异常
     * 当spring.rabbitmq.listener.simple.acknowledge-mode=auto时
     * 消费未发生异常 会自动提交ack 表示消息已消费 队列将消息移除
     * 如果消费发生异常 消息会再次回到队列中等待被再次消费 相当于手动提交basicNack() 其中是否重新入队取spring.rabbitmq.listener.simple.default-requeue-rejected配置的值（默认true）
     * 对于核心业务 可以通过配置死信队列 将消费异常的消息发到死信队列存储 待查明原因后 通过死信队列消费者消费
     * @Author Jane
     * @Date: 2022/7/6 11:16
     * @Version 1.0
     */
    @Test
    public void test06() {
        rabbitTemplate.convertAndSend("rabbit.test.exchange.direct.exception", "exception", "this is a test message for consume exception");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
