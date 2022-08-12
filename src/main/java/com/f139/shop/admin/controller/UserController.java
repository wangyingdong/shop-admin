package com.f139.shop.admin.controller;

import com.alibaba.fastjson.JSON;
import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.entity.UserInfo;
import com.f139.shop.admin.security.util.JwtUtil;
import com.f139.shop.admin.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Maps;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@ResponseBody
@RequestMapping("/users")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping(value = "login")
    public Map<String, String> login(@RequestBody @Validated(UserInfo.Login.class) UserInfo userInfo) {
        Map<String,String> map =  userService.loginUser(userInfo);
        return map;
    }

    @PostMapping(value = "/token/refresh")
    public Map<String, String> tokenRefresh(@RequestHeader(name = JwtUtil.AUTH_HEADER_KEY) String authorization,@RequestParam String refreshToken) {
        if (JwtUtil.validateRefreshToken(refreshToken)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("accessToken", JwtUtil.createAccessTokenWithoutAccessToken(authorization.replace(JwtUtil.TOKEN_PREFIX,"")));
            map.put("refreshToken", JwtUtil.createRefreshTokenWithoutRefreshToken(refreshToken));
            return map;
        } else {
            throw new BusinessException(Errors.TOKEN_ERROR);
        }
    }

    @GetMapping(value = "logout")
    public void logout() {
        userService.logout();
    }

    @GetMapping(value = "{id}")
    public UserInfo getUserInfo(@Validated @NotNull @PathVariable Long id) {
        return userService.getUser(id);
    }


    @GetMapping
    public PageInfo<UserInfo> users(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                    @RequestParam(value = "query", defaultValue = "") String query) {
        return userService.getUsers(PageHelper.startPage(pageNum, pageSize), UserInfo.builder().username(query).build());
    }

    @PutMapping(value = "/{id}/state/{state}")
    public UserInfo updateState(@PathVariable Long id, @PathVariable Boolean state) {
        UserInfo userInfo = UserInfo.builder().state(state).id(id).build();
        return userService.updateUser(userInfo);
    }

    @PostMapping(value = "save")
    public Boolean save(@Validated(UserInfo.Save.class) @RequestBody UserInfo userInfo) {
        userInfo.setState(false);
        return userService.saveUser(userInfo);
    }

    @PostMapping(value = "update")
    public Boolean update(@Validated(UserInfo.Update.class) @RequestBody UserInfo userInfo) {
        userService.updateUser(userInfo);
        return true;
    }

    @DeleteMapping(value = "/{id}")
    public Boolean delete(@PathVariable Long id) {
        return userService.deleteUser(id);
    }


    @GetMapping(value = "/role/{id}")
    public UserInfo getUserRole(@Validated @NotNull @PathVariable Long id) {
        return userService.getUser(id);
    }


    @PutMapping(value = "/{id}/role")
    public Boolean updateRole(@PathVariable Long id, @RequestBody Map<String, List<Integer>> map) {
        List<Integer> list = map.get("roleRoleIds");
        Integer[] array = list.stream().toArray(Integer[]::new);
        return userService.updateRole(id, array);
    }
}
