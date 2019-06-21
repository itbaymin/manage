package com.byc.websocket;

/**
 * Created by baiyc
 * 2019/5/23/023 17:19
 * Description：
 */
public class ResponseMessage {
    private String id;
    private String name;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ResponseMessage(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
