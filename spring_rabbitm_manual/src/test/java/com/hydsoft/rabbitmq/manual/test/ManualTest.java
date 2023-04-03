package com.hydsoft.rabbitmq.manual.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @title: ManualTest
 * @Description:
 * @Author Jane
 * @Date: 2022/7/7 9:22
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ManualTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Description: 测试手动提交Ack
     * 1、手动提交Ack 如果没有提交basicAck 消息不会从队列中删除 状态是Unacked 当有新消息推送施 该消息不会被推送 但是当消费者与RabbitMQ断开后(重启) 消息状态变成Ready状态 会重新推送给消费者
     * 也会造成消息重复消费和消息堆积 与拒绝消费并重新放入队列不同
     * 2、业务处理异常 通过调用basicNack 拒绝消费 为了避免重入列的消息一直消费-入列-消费-入列这样循环，可以设置不重新放入队列。
     * 3、重试机制仅在自动提价Ack模式下使用
     * @Author Jane
     * @Date: 2022/7/7 10:14
     * @Version 1.0
     */
    @Test
    public void test01() {
        rabbitTemplate.convertAndSend("rabbit.test.exchange.direct.manual", "manual", "This is a rabbitmq message to test manual");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {

        }
    }

    /**
     * @Description: 测试延时消息
     * 1、RabbitMQ服务器安装x-delayed-message插件
     * 2、使用CustomExchange 指定类型为x-delayed-message
     * @Author Jane
     * @Date: 2022/7/7 11:52
     * @Version 1.0
     */
    @Test
    public void test02() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        logger.info("开始发送订单超时消息，当前时间:{}", now);
        //发送延迟消息 延时30000ms
        rabbitTemplate.convertAndSend("rabbit.test.exchange.delay", "delay", "This is a delay message to cancel order",msg->{
            msg.getMessageProperties().setDelay(30000);
            return msg;
        });
        try {
            Thread.sleep(35000);
        } catch (InterruptedException e) {

        }
    }


    /**
     * @Description: 测试 死信队列+TTL(time-to-live) 实现延迟队列
     * @Author Jane
     * @Date: 2022/7/7 14:39
     * @Version 1.0
     */
    @Test
    public void test03() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        logger.info("开始发送订单超时消息，当前时间:{}", now);
        //发送到超时队列 超时之后 队列消息将会发送到配置的死信交换机对应的死信队列
        rabbitTemplate.convertAndSend("rabbit.test.exchange.timeout", "timeout", "This is a timeout message to cancel order");
        try {
            Thread.sleep(35000);
        } catch (InterruptedException e) {

        }
    }

    /**
     * @Description: 测试 死信队列
     * 手动提交Ack模式下 通过配置业务队列的死信交换机到死信队列上 当消息消费异常时，设置消息不重新加入队列而是进入死信队列 既不会造成消息的积压 又不会丢失消息
     * @Author Jane
     * @Date: 2022/7/7 15:02
     * @Version 1.0
     */
    @Test
    public void test04() {
        rabbitTemplate.convertAndSend("rabbit.test.exchange.business", "business", "This is a business message for testing Exception to go to dead queue");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }


}
