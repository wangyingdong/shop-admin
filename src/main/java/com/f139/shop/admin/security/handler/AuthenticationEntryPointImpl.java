package com.f139.shop.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.f139.shop.admin.common.response.ResponseResult;
import com.f139.shop.admin.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult responseResult = ResponseResult.builder().data("用戶认证失败，请重新登录").code(HttpStatus.FORBIDDEN.value()).status("error").build();
        WebUtils.renderString(response, JSON.toJSONString(responseResult));
    }
}
