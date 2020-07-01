package com.mail.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2019/5/30/030 16:33
 * Description：
 */
@Data
@Component
@ConfigurationProperties(prefix="mail")
public class MailProperties {
    private String protocol;
    private String host;
    private String port;
    private String username;
    private String password;
}
