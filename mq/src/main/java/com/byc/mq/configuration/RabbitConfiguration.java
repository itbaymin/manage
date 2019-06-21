package com.byc.mq.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by baiyc
 * 2019/5/23/023 14:47
 * Description：
 */
@Configuration
public class RabbitConfiguration {

    @Bean
    public Queue Queue() {
        return new Queue("hello");
    }

}
