package com.f139.shop.admin.common.exception;

public enum  Errors {

    UNKNOWN_ERROR(10000, "未知错误"),
    NO_OBJECT_FOUND_ERROR(10001, "对象不存在"),
    PARAMETER_ERROR(10002, "参数校验失败"),
    DATA_STATE_ERROR(10003, "数据状态错误"),
    DATA_DUPLICATE_ERROR(1004, "数据已存在"),
    USERNAME_DUPLICATE_ERROR(1005, "用户名已存在"),
    MOBILE_DUPLICATE_ERROR(1006, "手机号码已存在"),

    LOGIN_ERROR(20001, "用户或密码错误"),

    UPLOAD_FILE_NAME_ERROR(30001, "文件格式错误"),
    UPLOAD_FILE_EMPTY_ERROR(30002, "文件內容错误");



    public final Integer code;
    public final String message;

    Errors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
