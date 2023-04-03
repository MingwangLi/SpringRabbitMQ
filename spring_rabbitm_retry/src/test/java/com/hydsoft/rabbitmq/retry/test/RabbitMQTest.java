package com.hydsoft.rabbitmq.retry.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
     * @Description: 测试消息消费异常重试
     * 1、spring.rabbitmq.listener.simple.retry.max-attempts=5 指的是消费次数 值-1等于实际意义上的重试次数
     * 2、spring.rabbitmq.listener.simple.retry.max-interval 如果计算下次间隔时间超过了最大重试间隔 会取最大重试间隔
     * 3、达到最大重试次数之后 如果依然失败 会将消息从队列中移除（消息丢失）
     * 注意：
     * auto模式下 配置重试 可能会造成消息丢失
     * spring.rabbitmq.listener.simple.retry.enabled=true情况下 达到最大重试次数之后 即使配置spring.rabbitmq.listener.simple.default-requeue-rejected=true
     * 消息依然会从队列中移除（消息丢失）
     * @Author Jane
     * @Date: 2022/7/6 15:43
     * @Version 1.0
     */
    @Test
    public void test01() {
        rabbitTemplate.convertAndSend("rabbit.test.exchange.direct.exception.retry", "retry", "this is a test message for consume exception retry");
        try {
            Thread.sleep(60000);
            //如果未达到最大重试次数 消息依然会在队列中
            //Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
