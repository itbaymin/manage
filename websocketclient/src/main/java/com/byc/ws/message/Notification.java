package com.byc.ws.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Created by dongt
 * 2019/1/9/009 19:39
 */
@Data
public abstract class Notification {

    /** 系统通知(sys)、订单通知(trade)、聊天(chat)、ACK */
    @JsonProperty("type")
    private String messageType;
    /** 订单被接受(accept), 订单支付成功(payed), 订单支付失败(payfail) */
    private String subtype;
    private String content;
    /** 额外的参数串 */
    private Map<String, String> param;
    @JsonProperty("t")
    private long timestamp;
    @JsonProperty("msg_id")
    private String msgId;

    public boolean ack() {
        return messageType.equals("ack");
    }

    public boolean chat() {
        return messageType.equals("chat");
    }

}
