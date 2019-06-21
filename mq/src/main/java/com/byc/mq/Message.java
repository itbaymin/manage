package com.byc.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by baiyc
 * 2019/5/23/023 15:29
 * Description：
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {

    private String content;

    private Date time;
}
