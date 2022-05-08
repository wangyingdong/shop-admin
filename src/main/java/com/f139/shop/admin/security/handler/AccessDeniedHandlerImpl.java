package com.f139.shop.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.f139.shop.admin.common.response.ResponseResult;
import com.f139.shop.admin.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult responseResult = ResponseResult.builder().data("权限认证失败，请重新登录").code(HttpStatus.FORBIDDEN.value()).status("error").build();
        WebUtils.renderString(response, JSON.toJSONString(responseResult));
    }
}
