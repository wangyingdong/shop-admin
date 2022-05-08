package com.f139.shop.admin.security.util;

import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;


public class JwtUtil {

    //有效期为
    public static final Long TTL = 60 * 60 * 1000L;// 60 * 60 *1000  一个小时
    //设置签名访问令牌的秘钥
    public static final Key ACCESS_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    //设置刷新令牌的秘钥
    public static final Key REFRESH_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }


    public static Boolean validateAccessToken(String token) {
        return validateToken(token, ACCESS_KEY, true);
    }

    public static Boolean validateAccessTokenWithoutExpiration(String token) {
        return validateToken(token, ACCESS_KEY, false);
    }

    public static Boolean validateRefreshToken(String token) {
        return validateToken(token, REFRESH_KEY, true);
    }


    private static Boolean validateToken(String token, Key key, Boolean isExpiredInvalid) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                return !isExpiredInvalid;
            }
            return false;
        }
    }


    public static String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, ACCESS_KEY);
    }

    public static String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, REFRESH_KEY);
    }

    public static String createAccessTokenWithoutRefreshToken(String token) {
        return (parseClaims(token, REFRESH_KEY))
                .map(claims ->
                        Jwts.builder()
                                .setClaims(claims)
                                .setExpiration(new Date(System.currentTimeMillis() + TTL))
                                .setIssuedAt(new Date())
                                .signWith(ACCESS_KEY, SignatureAlgorithm.HS512)
                                .compact()
                )
                .orElseThrow(() ->
                        new BusinessException(Errors.TOKEN_ERROR));


    }

    public static String getTokenSubject(String token) {
        return (parseClaims(token,ACCESS_KEY)
                    .map(claims -> claims.getSubject())
                )
                .orElseThrow( () ->
                        new BusinessException(Errors.TOKEN_ERROR));


    }

    private static String createJwtToken(UserDetails userDetails, Key key) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId(JwtUtil.getUUID())              //唯一的ID
                .setSubject(userDetails.getUsername())   // 主题  可以是JSON数据
                .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .setIssuedAt(new Date(now))      // 签发时间
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + JwtUtil.TTL))
                .compact();
    }


    /**
     * 解析
     *
     * @param token
     * @return
     * @throws Exception
     */
    private static Optional<Claims> parseClaims(String token, Key key) {
        try {
            val body = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(body);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
