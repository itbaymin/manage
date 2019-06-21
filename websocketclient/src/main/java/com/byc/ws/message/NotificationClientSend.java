package com.byc.ws.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

/**
 * 客户端发送消息模型，In是相对于服务端接收消息的定义
 * Created by dongt
 * 2019/1/14/014 09:55
 */
@Data
public class NotificationClientSend extends Notification {

    @JsonProperty("to")
    /** userid, broadcast */
    private String toUser;

    public void initialize() {
        if(getMsgId()==null||getMsgId().isEmpty()) {
            setMsgId(UUID.randomUUID().toString());
        }
        if(getTimestamp() == 0) {
            setTimestamp(System.currentTimeMillis());
        }
    }

    public boolean broadCast() {
        return toUser.equals("broadcast");
    }

    public static NotificationClientSend ackNotification(String msgId) {
        NotificationClientSend notificationClientSend = new NotificationClientSend();
        notificationClientSend.setMessageType("ack");
        notificationClientSend.setMsgId(msgId);
        return notificationClientSend;
    }

    public static NotificationClientSend systemNotification(String toUser, String content, boolean broadcast) {
        NotificationClientSend notificationClientSend = new NotificationClientSend();
        notificationClientSend.setMessageType("sys");
        notificationClientSend.setToUser(broadcast ? "broadcast" : toUser);
        notificationClientSend.setContent(content);
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend tradeRefundSuccessNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("refund");
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend tradePaySuccessNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("payed");
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend tradeCanceldNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("canceld");
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend tradeReadyNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("ready");
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend tradePayFailNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("payfail");
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend tradeAcceptNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("accept");
        notificationClientSend.initialize();
        return notificationClientSend;
    }
    public static NotificationClientSend tradeStartNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("start");
        notificationClientSend.initialize();
        return notificationClientSend;
    }
    public static NotificationClientSend tradeFinishNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = tradeNotification(toUser, content, params);
        notificationClientSend.setSubtype("finish");
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    protected static NotificationClientSend tradeNotification(String toUser, String content, Map<String, String> params) {
        NotificationClientSend notificationClientSend = new NotificationClientSend();
        notificationClientSend.setMessageType("trade");
        notificationClientSend.setToUser(toUser);
        notificationClientSend.setContent(content);
        notificationClientSend.setParam(params);
        notificationClientSend.initialize();
        return notificationClientSend;
    }

    public static NotificationClientSend chatNotification(String toUser,String content) {
        NotificationClientSend notificationClientSend = new NotificationClientSend();
        notificationClientSend.setMessageType("chat");
        notificationClientSend.setToUser(toUser);
        notificationClientSend.setContent(content);
        notificationClientSend.initialize();
        return notificationClientSend;
    }
}
