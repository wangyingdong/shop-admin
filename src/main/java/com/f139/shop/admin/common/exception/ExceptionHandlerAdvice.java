package com.f139.shop.admin.common.exception;

import com.f139.shop.admin.common.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {


    @ExceptionHandler(value = {Exception.class})
    public ResponseResult handleException(HttpServletRequest request, Exception ex, HttpServletResponse response) {
        log.error("exception: {}, msg: {}", ex.getStackTrace(), ex.getMessage());
        //response.setStatus(HttpStatus.BAD_REQUEST.value());
        if (ex instanceof BusinessException) {
            BusinessException exception = (BusinessException) ex;
            return ResponseResult.builder().code(exception.getCode()).status("error").data(exception.getMessage()).build();
        } else {
            return ResponseResult.builder().code(Errors.UNKNOWN_ERROR.code).status("error").data(Errors.UNKNOWN_ERROR.message).build();
        }


    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult validationError(MethodArgumentNotValidException ex) {
        for (ObjectError s : ex.getBindingResult().getAllErrors()) {
            return ResponseResult.builder().code(Errors.PARAMETER_ERROR.code).status("error").data(s.getDefaultMessage()).build();
        }
        return ResponseResult.builder().code(Errors.PARAMETER_ERROR.code).status("error").data("未知参数错误").build();
    }
}
