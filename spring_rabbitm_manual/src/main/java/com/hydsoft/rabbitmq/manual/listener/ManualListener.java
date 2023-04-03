package com.hydsoft.rabbitmq.manual.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @title: ManualListener
 * @Description:
 * @Author Jane
 * @Date: 2022/7/7 9:24
 * @Version 1.0
 */
@Component
public class ManualListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = "rabbit.test.queue.direct.manual")
    public void receiveManualMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = "";
        try {
            msg = new String(body, "UTF-8");
            logger.info("rabbit.test.queue.direct.manual receive message:{}", msg);
            //do business
            //模拟业务处理异常
            int i = 1/0;
            // 确认消息。
            // 第2个参数如果设为true，则表示批量确认当前通道中所有deliveryTag小于当前消息的所有消息。
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            logger.error("consume message :{} error:{}", msg, e.getMessage());
            // 消费失败记录入库
            // insertLog
            try {
                // 拒绝消息。
                // 第2个参数如果设为true，则表示批量拒绝当前通道中所有deliveryTag小于当前消息的所有消息。
                // 第3个参数如果设为true，则表示当前消息再次回到队列中等待被再次消费。
                // 对于拒绝消息并且重回队列使用时需要谨慎，避免使用不当会导致一些每次都被你重入列的消息一直消费-入列-消费-入列这样循环，会导致消息积压。
                // 方式一：消费失败记录入库 手工处理
                // 方式二：将消费失败的消息转到死信队列
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                logger.error("basicNack message :{} error:{}", msg, ex.getMessage());
            }finally {
                //do business rollback
            }
        }
    }

    @RabbitListener(queues = "rabbit.test.queue.delay")
    public void receiveDelayMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = "";
        try {
            msg = new String(body, "UTF-8");
            logger.info("rabbit.test.queue.delay receive message:{}", msg);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = sdf.format(new Date());
            logger.info("收到订单超时消息，当前时间:{}", now);
            //订单超时
            //cancelOrder
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            logger.error("consume message :{} error:{}", msg, e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                logger.error("basicNack message :{} error:{}", msg, ex.getMessage());
            }finally {

            }
        }
    }

    // @RabbitListener(queues = "rabbit.test.queue.timeout")
    // public void receiveTimeoutMessage(Message message, Channel channel) {
    //     byte[] body = message.getBody();
    //     long deliveryTag = message.getMessageProperties().getDeliveryTag();
    //     String msg = "";
    //     try {
    //         msg = new String(body, "UTF-8");
    //         logger.info("rabbit.test.queue.timeout receive message:{}", msg);
    //         channel.basicAck(deliveryTag, false);
    //     } catch (Exception e) {
    //         logger.error("consume message :{} error:{}", msg, e.getMessage());
    //         try {
    //             channel.basicNack(deliveryTag, false, false);
    //         } catch (IOException ex) {
    //             logger.error("basicNack message :{} error:{}", msg, ex.getMessage());
    //         }finally {
    //
    //         }
    //     }
    // }

    @RabbitListener(queues = "rabbit.test.queue.dead")
    public void receiveDeadMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = "";
        try {
            msg = new String(body, "UTF-8");
            logger.info("rabbit.test.queue.dead receive message:{}", msg);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = sdf.format(new Date());
            logger.info("收到订单超时消息，当前时间:{}", now);
            //订单超时
            //cancelOrder
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            logger.error("consume message :{} error:{}", msg, e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                logger.error("basicNack message :{} error:{}", msg, ex.getMessage());
            }finally {

            }
        }
    }

    @RabbitListener(queues = "rabbit.test.queue.business")
    public void receiveBusinessMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = "";
        try {
            msg = new String(body, "UTF-8");
            logger.info("rabbit.test.queue.business receive message:{}", msg);
            //模式业务处理异常
            int i = 1/0;
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            logger.error("consume message :{} error:{}", msg, e.getMessage());
            try {
                //配置死信队列情况下 basicNack 消息不重新加入队列情况下 消息会从本队列移除并进入死信队列
                channel.basicNack(deliveryTag, false, false);
                logger.warn("rabbit.test.queue.business message:{} prepare to go to dead queue", msg);
            } catch (IOException ex) {
                logger.error("basicNack message :{} error:{}", msg, ex.getMessage());
            }finally {

            }
        }
    }

    @RabbitListener(queues = "rabbit.test.queue.business.dead")
    public void receiveBusinessDeadMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = "";
        try {
            msg = new String(body, "UTF-8");
            logger.info("rabbit.test.queue.business.dead receive message:{}", msg);
            channel.basicAck(deliveryTag, false);
            logger.info("死信队列正常处理完消息，实现故障转移");
        } catch (Exception e) {
            logger.error("consume message :{} error:{}", msg, e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                logger.error("basicNack message :{} error:{}", msg, ex.getMessage());
            }finally {

            }
        }
    }
}
