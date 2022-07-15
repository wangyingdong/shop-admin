package com.f139.shop.admin.security.util;

import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;


public class JwtUtil {

    //有效期为
    public static final Long TTL = 60 * 60 * 1000L;// 60 * 60 *1000  一个小时

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String AUTH_HEADER_KEY = "Authorization";

    private static final String TOKEN_ACCESS_KEY = "accesskeysufdadsfshuzhitingniasdfangcereshuzhitingnizhenbangasdfasfsafd";

    private static final String TOKEN_REFRESH_KEY = "refreshkeyafdadsfshuzhitingnizasdfnbangcereshuzhsgnizhenbangasdfasfsafd";


    //设置签名访问令牌的秘钥
    private static SecretKey getAccessKey(){
        return getSecretKey(TOKEN_ACCESS_KEY);
    }

    //设置刷新令牌的秘钥
    private static SecretKey getRefreshKey(){
       return getSecretKey(TOKEN_REFRESH_KEY);
    }

    private static SecretKey getSecretKey(String key){
        byte[] keySecretBytes = Base64.getDecoder().decode(key);
        SecretKey signingKey = new SecretKeySpec(keySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        return signingKey;
    }

    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }


    public static Boolean validateAccessToken(String token) {
        return validateToken(token, getAccessKey(), true);
    }

    public static Boolean validateAccessTokenWithoutExpiration(String token) {
        return validateToken(token, getAccessKey(), false);
    }

    public static Boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshKey(), true);
    }

    public static Boolean validateAccessTokenToken(String token) {
        return validateToken(token, getAccessKey(), true);
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
        return createJwtToken(userDetails, getAccessKey());
    }

    public static String createAccessToken(UserDetails userDetails,Key key) {
        return createJwtToken(userDetails, key);
    }

    public static String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, getRefreshKey());
    }

    public static String createAccessTokenWithoutAccessToken(String token) {
        return (parseClaims(token, getAccessKey()))
                .map(claims ->
                        Jwts.builder()
                                .setClaims(claims)
                                .setExpiration(new Date(System.currentTimeMillis() + TTL))
                                .setIssuedAt(new Date())
                                .signWith(getAccessKey(), SignatureAlgorithm.HS256)
                                .compact()
                )
                .orElseThrow(() ->
                        new BusinessException(Errors.TOKEN_ERROR));

    }

    public static String createRefreshTokenWithoutRefreshToken(String token) {
        return (parseClaims(token, getRefreshKey()))
                .map(claims ->
                        Jwts.builder()
                                .setClaims(claims)
                                .setExpiration(new Date(System.currentTimeMillis() + TTL))
                                .setIssuedAt(new Date())
                                .signWith(getRefreshKey(), SignatureAlgorithm.HS256)
                                .compact()
                )
                .orElseThrow(() ->
                        new BusinessException(Errors.TOKEN_ERROR));

    }

    public static String getTokenSubject(String token) {
        return (parseClaims(token, getAccessKey())
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
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(now + JwtUtil.TTL))
                .compact();
    }

    public static Optional<Claims> parseAccessToken(String token) {
        return parseClaims(token,getAccessKey());
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
