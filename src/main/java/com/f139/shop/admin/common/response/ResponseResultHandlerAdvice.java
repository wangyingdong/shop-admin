package com.f139.shop.admin.common.response;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseResultHandlerAdvice implements ResponseBodyAdvice {

    //这个方法表示对于哪些请求要执行beforeBodyWrite，返回true执行，返回false不执行
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    //对于返回的对象如果不是最终对象ResponseResult，则选包装一下
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof String) {
            ObjectMapper om = new ObjectMapper();
            try {
                return om.writeValueAsString(ResponseResult.builder().data(body).build());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (MediaType.APPLICATION_JSON.equals(selectedContentType) || MediaType.APPLICATION_JSON_VALUE.equals(selectedContentType)) { // 判断响应的Content-Type为JSON格式的body
            if (!(body instanceof ResponseResult)) {
                //因为handler处理类的返回类型是String，为了保证一致性，这里需要将ResponseResult转回去
                if(body instanceof String) {
                    return JSON.toJSONString(ResponseResult.builder().data(body).build());
                }
            } else {
                return body;
            }
        }
        // 非JSON格式body直接返回即可
        return (ResponseResult.builder().data(body).build());
    }
}