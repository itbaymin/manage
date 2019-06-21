package com.byc.mq.sender;

import com.byc.mq.Message;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by baiyc
 * 2019/5/23/023 14:49
 * Description：
 */
@Component
public class HelloSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    
    /**
     * Description:发送字符串
     * params:[i]
     * return:void
     */
    public void send(int i) {
        String context = "hello " + new Date();
        System.out.println("Sender: " + context);
        this.rabbitTemplate.convertAndSend("hello", context+"****"+i);
    }

    /**
     * Description:发送对象
     * params:[i]
     * return:void
     */
    public void sendObj(int i) {
        Message msg = new Message("测试",new Date());
        System.out.println("Sender: " + msg);
        this.rabbitTemplate.convertAndSend("hello", msg);
    }

}
