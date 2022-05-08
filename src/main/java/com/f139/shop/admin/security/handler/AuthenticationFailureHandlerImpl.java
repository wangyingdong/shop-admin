package com.f139.shop.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.f139.shop.admin.common.response.ResponseResult;
import com.f139.shop.admin.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseResult responseResult = null;
        //BadCredentialsException 这个异常一般是用户名或者密码错误
        if (exception instanceof BadCredentialsException) {
            responseResult = ResponseResult.builder().data("用户名或密码不正确").code(HttpStatus.OK.value()).status("error").build();
        }
        responseResult = ResponseResult.builder().data("登录失败").code(HttpStatus.OK.value()).status("error").build();
        WebUtils.renderString(response, JSON.toJSONString(responseResult));

    }
}
