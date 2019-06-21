package com.byc.result;

/**
 * Created by baiyc
 * 2019/5/20/020 16:43
 * Description：
 */
public enum ResultCode {
    NOT_FOUND(404),ERROR_SYSTEM(500),SUCCESS(200);

    private int code;

    ResultCode(int code) {
        this.code = code;
    }
}
