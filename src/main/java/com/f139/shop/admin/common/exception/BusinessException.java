package com.f139.shop.admin.common.exception;

public class BusinessException extends RuntimeException {
        private Integer code;
        private String message;

        public BusinessException(Errors errors) {
                this(errors.code, errors.message, null);
        }

        public BusinessException(Errors errors, Throwable throwable) {
                this(errors.code, errors.message, throwable);
        }

        public BusinessException(Integer code, String message, Throwable throwable) {
                super(throwable);
                this.code = code;
                this.message = message;
        }

        public BusinessException() {
                super();
        }

        @Override
        public String getMessage() {
                return message;
        }


        public Integer getCode() {
                return code;
        }

        public void setCode(Integer code) {
                this.code = code;
        }
}