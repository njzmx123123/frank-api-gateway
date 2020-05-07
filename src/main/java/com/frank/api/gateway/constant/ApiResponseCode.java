package com.frank.api.gateway.constant;

/**
 * @author frank
 */

public enum ApiResponseCode {

    /**
     * 处理成功
     */
    SUCCESS("0"),
    /**
     * Illegal request parameter
     */
    ILLEGAL_REQUEST_PARAMETER("10000"),

    /**
     * app id 为空
     */
    APP_ID_EMPTY("10001"),

    /**
     * 请求处理失败
     */
    PROCESS_REQ_FAIL("20000");

    ApiResponseCode(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return this.code;
    }

}