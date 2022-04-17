package com.f139.shop.admin.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.f139.shop.admin.common.validation.Mobile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @NotNull(groups = {Update.class})
    private Long id;

    @NotNull
    @Column(name = "username")
    @NotNull(groups = {Login.class,Save.class})
    @Length(min = 3, max = 20,message = "用户名长度3-20之间",groups = {Login.class,Save.class})
    private String username;

    @Column(name = "password")
    @NotNull(groups = {Login.class,Save.class})
    @Length(min = 6, max = 15,message = "密码长度6-15之间",groups = {Login.class,Save.class})
    private String password;

    @Column(name = "address")
    @NotNull(groups = {Update.class,Save.class})
    @Length(min = 3, max = 50,message = "所在地的长度在3-50之间",groups = {Update.class,Save.class})
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    @NotNull(groups = {Update.class,Save.class})
    @Mobile(groups = {Update.class,Save.class})
    @Length(min = 11, max = 11,message = "手机号码长度11",groups = {Update.class,Save.class})
    private String mobile;

    @Column(name = "state")
    private Boolean state;

    @Transient //不存在的字段
    private List<ViewUserRole> userRoleList;

    //保存的时候校验分组
    public interface Save {
    }

    //登录的时候校验分组
    public interface Login {
    }

    //更新的时候校验分组
    public interface Update {
    }
}