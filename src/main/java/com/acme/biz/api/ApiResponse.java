package com.acme.biz.api;

/**
 * @author qi.li
 * @email liq@hzgjgc.com
 * @since 2022/10/12
 */
public class ApiResponse<T> {
    private int code;

    private String message;

    private T body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
