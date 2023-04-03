package com.hydsoft.rabbitmq.retry.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title: RabbitMQListener
 * @Description: 测试消息重试
 * @Author Jane
 * @Date: 2022/7/4 15:00
 * @Version 1.0
 */
@Component
public class RabbitMQListener {

    public static AtomicInteger atomicInteger = new AtomicInteger();

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RabbitListener(queues = "rabbit.test.queue.direct.exception.retry")
    public void receiveDirectQueueMessageExceptionRetry(String message) {
        logger.info("rabbit.test.queue.direct.exception.retry receive message:{}", message);
        int retryTime = atomicInteger.getAndIncrement();
        logger.info("rabbit.test.queue.direct.exception.retry retryTime:{}", retryTime);
        throw new RuntimeException();
    }
}
