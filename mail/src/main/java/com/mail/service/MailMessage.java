package com.mail.service;

import lombok.Data;

@Data
public class MailMessage {

    private String messageCode;

    private String messageStatus;

    private String cause;

}