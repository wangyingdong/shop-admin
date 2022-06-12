package com.f139.shop.admin.security.filter;

import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.common.redis.RedisClientUtil;
import com.f139.shop.admin.security.pojo.LoginUser;
import com.f139.shop.admin.security.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisClientUtil redisClientUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(request)) {
            String token = request.getHeader(JwtUtil.AUTH_HEADER_KEY).replace(JwtUtil.TOKEN_PREFIX, "");
            //解析token
            String username = JwtUtil.getTokenSubject(token);
            //从redis中获取用户信息
            LoginUser loginUser = (LoginUser) redisClientUtil.getVal("login:" + username);
            if (Objects.isNull(loginUser)) {
                throw new BusinessException(Errors.LOGIN_USER_NOT_LOGIN_ERROR);
            }
            //存入SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //放行
        filterChain.doFilter(request, response);
    }

    private boolean checkJwtToken(HttpServletRequest request) {
        String token = request.getHeader(JwtUtil.AUTH_HEADER_KEY);
        return token != null && token.startsWith(JwtUtil.TOKEN_PREFIX);
    }


}
