package com.byc.mq.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2019/5/23/023 15:38
 * Description：
 */
@Component
public class TopicSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    /**
     * Description:topic
     * params:[]
     * return:void
     */
    public void sendTopic() {
        String context = "hi, i am message 1";
        System.out.println("TopicSender: " + context);
        this.rabbitTemplate.convertAndSend("exchange", "topic.message", context);
    }

    public void sendTopic2() {
        String context = "hi, i am messages 2";
        System.out.println("TopicSender: " + context);
        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", context);
    }
}
