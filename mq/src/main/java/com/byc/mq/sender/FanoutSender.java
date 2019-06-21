package com.byc.mq.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2019/5/23/023 15:44
 * Description：广播模式/订阅模式
 */
@Component
public class FanoutSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hi, fanout msg ";
        System.out.println("FanoutSender: " + context);
        this.rabbitTemplate.convertAndSend("fanoutExchange","", context);
    }
}
