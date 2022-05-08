package com.f139.shop.admin.service;

import com.f139.shop.admin.entity.UserInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface IUserService {

    Boolean saveUser(UserInfo userInfo);

    Boolean deleteUser(Long id);

    UserInfo getUser(UserInfo userInfo);

    Map<String, String> loginUser(UserInfo userInfo);

    void logout();

    UserInfo getUser(Long id);

    UserInfo updateUser(UserInfo userInfo);

    PageInfo<UserInfo> getUsers(Page page, UserInfo userInfo);

    Boolean updateRole(Long userId, Integer[] roleRoleIds);

}
