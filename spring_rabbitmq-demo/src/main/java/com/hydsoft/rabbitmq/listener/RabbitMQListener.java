package com.hydsoft.rabbitmq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @title: RabbitMQListener
 * @Description:
 * @Author Jane
 * @Date: 2022/7/4 15:00
 * @Version 1.0
 */
@Component
public class RabbitMQListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = "rabbit.test.queue.fanout1")
    public void receiveFanoutQueue1Message(String message){
        logger.info("rabbit.test.queue.fanout1 receive message:{}", message);
    }

    @RabbitListener(queues = "rabbit.test.queue.fanout2")
    public void receiveFanoutQueue2Message(String message) {
        logger.info("rabbit.test.queue.fanout2 receive message:{}", message);
    }

    @RabbitListener(queues = "rabbit.test.queue.direct")
    public void receiveDirectQueueMessage(String message) {
        logger.info("rabbit.test.queue.direct receive message:{}", message);
    }

    @RabbitListener(queues = "rabbit.test.queue.topic1")
    public void receiveTopicQueue1Message(String message){
        logger.info("rabbit.test.queue.topic1 receive message:{}", message);
    }

    @RabbitListener(queues = "rabbit.test.queue.topic2")
    public void receiveTopicQueue2Message(String message) {
        logger.info("rabbit.test.queue.topic2 receive message:{}", message);
    }

    @RabbitListener(queues = "rabbit.test.queue.direct.exception")
    public void receiveDirectQueueMessageException(String message) {
        logger.info("rabbit.test.queue.direct.exception receive message:{}", message);
        throw new RuntimeException();
    }
}
