package com.hydsoft.rabbitmq.manual.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: ManualTestController
 * @Description:
 * @Author Jane
 * @Date: 2022/7/7 10:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/manual")
public class ManualTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/test")
    public String test() {
        rabbitTemplate.convertAndSend("rabbit.test.exchange.direct.manual", "manual", "This is four rabbitmq message to test manual");
        return "SUCCESS";
    }
}
