package com.byc.mq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2019/5/23/023 15:46
 * Description：
 */
@Component
@RabbitListener(queues = "fanout.B")
public class FanoutReceiver2 {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("FanoutReceiver2: " + hello);
    }
}
