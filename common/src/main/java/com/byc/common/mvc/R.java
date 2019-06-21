package com.byc.common.mvc;

import lombok.Data;

/**
 * Created by baiyc
 * 2019/6/3/003 19:36
 * Description：
 */
@Data
public class R {
    private int code;
    private String msg;
    private Object data;

    public R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static R succ(){
        return new R(200,"成功",null);
    }

    public static R succ(Object o){
        return new R(200,"成功",o);
    }

    public static R fail(){
        return new R(500,"失败",null);
    }


}
