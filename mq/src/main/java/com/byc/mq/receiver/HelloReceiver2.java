package com.byc.mq.receiver;

import com.byc.mq.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2019/5/23/023 14:50
 * Description：
 */
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver2 {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver2: " + hello);
    }

    @RabbitHandler
    public void process(Message msg) {
        System.out.println("Receiver2: " + msg);
    }
}
