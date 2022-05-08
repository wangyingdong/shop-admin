package com.f139.shop.admin.service.impl;

import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.common.redis.RedisClientUtil;
import com.f139.shop.admin.entity.UserInfo;
import com.f139.shop.admin.entity.UserRole;
import com.f139.shop.admin.entity.ViewUserRole;
import com.f139.shop.admin.mapper.RoleMapper;
import com.f139.shop.admin.mapper.UserInfoMapper;
import com.f139.shop.admin.mapper.UserRoleMapper;
import com.f139.shop.admin.mapper.ViewUserRoleMapper;
import com.f139.shop.admin.security.pojo.LoginUser;
import com.f139.shop.admin.security.util.JwtUtil;
import com.f139.shop.admin.service.IUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ViewUserRoleMapper viewUserRoleMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisClientUtil redisClientUtil ;

    @Override
    public Map<String, String> loginUser(UserInfo userInfo) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("username", userInfo.getUsername());
        UserInfo user = userInfoMapper.selectOneByExample(example);
        if (Objects.isNull(user)) {
            throw new BusinessException(Errors.LOGIN_USERNAME_ERROR);
        }
        if (!user.getPassword().equals(userInfo.getPassword())) {
            throw new BusinessException(Errors.LOGIN_PASSWORD_ERROR);
        }
        // 角色集合
        List<String> permissions = new ArrayList<>();

        List<ViewUserRole> userRoles = viewUserRoleMapper.select(ViewUserRole.builder().userId(user.getId()).build());
        // 角色必须以`ROLE_`开头，数据库中没有，则在这里加
        for (ViewUserRole role : userRoles) {
            permissions.add(("ROLE_" + role.getRoleName()));
        }
        LoginUser loginUser = new LoginUser(
                UserInfo.builder().id(user.getId()).username(user.getUsername()).password(passwordEncoder.encode(user.getPassword())).userRoleList(userRoles).build(),
                permissions
        );

        String username = loginUser.getUserInfo().getUsername().toString();
        String accessToken = JwtUtil.createAccessToken(loginUser);
        String refreshToken = JwtUtil.createRefreshToken(loginUser);
        //存入缓存
        redisClientUtil.setVal("login:" + username, loginUser);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String username = loginUser.getUserInfo().getUsername();
        redisClientUtil.del("login:" + username);
    }

    @Override
    public Boolean saveUser(UserInfo userInfo) {
        int countUserName = userInfoMapper.selectCount(UserInfo.builder().username(userInfo.getUsername()).build());
        if (countUserName > 0) {
            throw new BusinessException(Errors.USERNAME_DUPLICATE_ERROR);
        }
        int countMobile = userInfoMapper.selectCount(UserInfo.builder().mobile(userInfo.getMobile()).build());
        if (countMobile > 0) {
            throw new BusinessException(Errors.MOBILE_DUPLICATE_ERROR);
        }
        return (userInfoMapper.insert(userInfo) > 0) ? true : false;
    }

    @Override
    public Boolean deleteUser(Long id) {
        return userInfoMapper.delete(UserInfo.builder().id(id).build()) > 0 ? true : false;
    }


    @Override
    public UserInfo getUser(UserInfo userInfo) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("username", userInfo.getUsername()).andEqualTo("password", userInfo.getPassword());
        UserInfo user = userInfoMapper.selectOneByExample(example);
        if (Objects.isNull(user)) {
            throw new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        return userInfo;
    }


    @Override
    public UserInfo getUser(Long id) {
        UserInfo user = userInfoMapper.selectOne(UserInfo.builder().id(id).build());
        if (Objects.isNull(user)) {
            throw new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        return user;
    }

    @Override
    public UserInfo updateUser(UserInfo userInfo) {
        Example example = new Example(UserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", userInfo.getId());
        userInfoMapper.updateByExampleSelective(userInfo, example);
        return userInfo;
    }

    @Override
    public PageInfo<UserInfo> getUsers(Page page, UserInfo userInfo) {
        Example e = new Example(UserInfo.class);
        Example.Criteria c = e.createCriteria();
        //关键字查询部分
        String username = userInfo.getUsername();
        if (!ObjectUtils.isEmpty(username)) {
            c.andLike("username", "%" + username + "%");
        }
        List<UserInfo> list = userInfoMapper.selectByExample(e);

        List userIdList = list.stream().map(u -> u.getId()).collect(Collectors.toList());
        Example r = new Example(ViewUserRole.class);
        Example.Criteria ca = r.createCriteria();
        ca.andIn("userId", userIdList);
        List<ViewUserRole> roleList = viewUserRoleMapper.selectByExample(r);
        Map<Long, List<ViewUserRole>> map = roleList.stream().collect(Collectors.groupingBy(role -> role.getUserId()));

        list.stream().map(user -> {
            user.setUserRoleList(map.get(user.getId()));
            return user;
        }).collect(Collectors.toList());

        PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo>(list);
        return pageInfo;
    }

    @Override
    @Transactional
    public Boolean updateRole(Long userId, Integer[] roleRoleIds) {
        userRoleMapper.delete(UserRole.builder().userId(userId).build());
        List<UserRole> userRoleList = new ArrayList<UserRole>();
        for (Integer i : roleRoleIds) {
            userRoleList.add(UserRole.builder().userId(userId).roleId(i).build());
        }
        userRoleList.forEach(userRoleMapper::insertSelective);
        return true;
    }


}