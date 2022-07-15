package com.f139.shop.admin.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component("authenticationPermissionService")
public class AuthenticationPermissionServiceImpl {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        if (principal instanceof UserDetails){
            UserDetails userDetails = (UserDetails) principal;
            //本次要访问的资源
            Set<String> urls = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toSet());
            for(String url:urls){
                String method = url.split(":")[0].toLowerCase();
                url = url.split(":")[1];
                if(antPathMatcher.match(url.toLowerCase(),request.getRequestURI().toLowerCase()) && method.equals(request.getMethod().toLowerCase())){
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }

}
