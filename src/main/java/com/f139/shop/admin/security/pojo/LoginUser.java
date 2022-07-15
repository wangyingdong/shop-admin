package com.f139.shop.admin.security.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.f139.shop.admin.entity.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {


    private UserInfo userInfo;

    //存储权限信息
    private List<String> permissions;

    public LoginUser(UserInfo userInfo,List<String> permissions) {
        this.userInfo = userInfo;
        this.permissions = permissions;
    }

    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    @Override
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        if(!Objects.isNull(authorities)){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
//        authorities = permissions.stream().
//                map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//        return authorities;
        return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", permissions));
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    //账号是否沒过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号是否沒锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //密码是否沒过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //账号是否可用
    @Override
    public boolean isEnabled() {
        return userInfo.getState();
    }

}
