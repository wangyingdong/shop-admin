package com.f139.shop.admin.security.handler;

import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.entity.UserInfo;
import com.f139.shop.admin.entity.ViewRoleModule;
import com.f139.shop.admin.entity.ViewUserRole;
import com.f139.shop.admin.mapper.UserInfoMapper;
import com.f139.shop.admin.mapper.ViewRoleModuleMapper;
import com.f139.shop.admin.mapper.ViewUserRoleMapper;
import com.f139.shop.admin.security.pojo.LoginUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ViewRoleModuleMapper viewRoleModuleMapper;

    @Resource
    private ViewUserRoleMapper viewUserRoleMapper;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("username", username);
        UserInfo user = userInfoMapper.selectOneByExample(example);
        if (Objects.isNull(user)) {
            throw new BusinessException(Errors.LOGIN_USERNAME_ERROR);
        }
        if (!user.getState()) {
            throw new BusinessException(Errors.LOGIN_USER_STATE_ERROR);
        }
        List<ViewUserRole> userRoles = viewUserRoleMapper.select(ViewUserRole.builder().userId(user.getId()).roleState(true).build());
        // 角色必须以`ROLE_`开头，数据库中没有，则在这里加
        List<String> authorities= this.getAuthorityByRoleCode(userRoles.stream().map(role -> role.getRoleId() ).collect(Collectors.toSet()));

        LoginUser loginUser = new LoginUser(
                UserInfo.builder().id(user.getId()).state(user.getState()).username(user.getUsername()).password(passwordEncoder.encode(user.getPassword())).userRoleList(userRoles).build(),
                authorities
        );

        return loginUser;
    }



    private List<String> getAuthorityByRoleCode(Set roleId) {
        Example example = new Example(ViewRoleModule.class);
        example.createCriteria().andIn("roleId", roleId).andNotEqualTo("path","");
        example.setDistinct(true);
        example.selectProperties("path");
        example.orderBy("sort");
        List<ViewRoleModule> moduleList = viewRoleModuleMapper.selectByExample(example);
        return moduleList.stream().map( m->m.getPath()).collect(Collectors.toList());
    }
}
