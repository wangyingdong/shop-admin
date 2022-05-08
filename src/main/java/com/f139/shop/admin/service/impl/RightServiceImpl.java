package com.f139.shop.admin.service.impl;

import com.f139.shop.admin.common.exception.BusinessException;
import com.f139.shop.admin.common.exception.Errors;
import com.f139.shop.admin.entity.Module;
import com.f139.shop.admin.entity.Role;
import com.f139.shop.admin.entity.RoleModule;
import com.f139.shop.admin.entity.ViewRoleModule;
import com.f139.shop.admin.mapper.ModulesMapper;
import com.f139.shop.admin.mapper.RoleMapper;
import com.f139.shop.admin.mapper.RoleModuleMapper;
import com.f139.shop.admin.mapper.ViewRoleModuleMapper;
import com.f139.shop.admin.service.IRightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RightServiceImpl implements IRightService {


    @Resource
    private ModulesMapper modulesMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleModuleMapper roleModuleMapper;

    @Resource
    private ViewRoleModuleMapper viewRoleModuleMapper;


    @Override
    public List<Module> getModules(Integer parentId) {
        Example example = new Example(Module.class);
        example.createCriteria().andEqualTo("parentId", parentId);
        List<Module> moduleList = modulesMapper.selectByExample(example);
        moduleList.stream().map(menu -> {
            menu.setChildren(getModules(menu.getId()));
            return menu;
        }).collect(Collectors.toList());
        return moduleList;
    }


    private List<ViewRoleModule> getRoleModules(Integer roleId,Integer parentId,Integer[] id, Integer level) {
        Example example = new Example(ViewRoleModule.class);
        example.createCriteria().andEqualTo("roleId",roleId).andEqualTo("parentId",parentId).andIn("id",Arrays.asList(id) ).andEqualTo("level",level);
        List<ViewRoleModule> roleModuleList = viewRoleModuleMapper.selectByExample(example);
        roleModuleList.stream().map(roleModule -> {
            roleModule.setChildren(getRoleModules(roleId,roleModule.getId(), id,level +1));
            return roleModule;
        }).collect(Collectors.toList());
        return roleModuleList;
    }




    @Override
    public List<Module> getModulesList() {
        Example example = new Example(Module.class);
        example.orderBy("sort").asc();
        List<Module> menuList = modulesMapper.selectByExample(example);
        return menuList;
    }

    @Override
    public List<Role> getRoles() {
        return roleMapper.selectAll();
    }

    @Override
    public Role getRole(Integer roleId) {
        Role role = roleMapper.selectOne(Role.builder().id(roleId).build());
        if(Objects.isNull(role)){
            throw new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        Example example = new Example(RoleModule.class);
        example.createCriteria().andEqualTo("roleId",roleId);
        List<RoleModule> roleModuleList = roleModuleMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(roleModuleList)){
            Integer[] modulesId = roleModuleList.stream().map( roleModule -> roleModule.getModuleId()).toArray(Integer[]::new);
            List<ViewRoleModule> moduleList = this.getRoleModules(roleId,0,modulesId,1);
            role.setModuleList(moduleList);
        }
        return role;
    }

    @Override
    public List<ViewRoleModule> getRoleModule(Integer roleId) {
        Example example = new Example(ViewRoleModule.class);
        example.createCriteria().andEqualTo("roleId",roleId);
        return  viewRoleModuleMapper.selectByExample(example);
    }

    @Override
    public Boolean deleteRoleModule(Integer roleId, Integer moduleId) {
        return roleModuleMapper.delete(RoleModule.builder().roleId(roleId).moduleId(moduleId).build()) != 0;
    }

    @Override
    @Transactional
    public Boolean saveRoleModule(Integer roleId, Integer[] moduleIds) {
        roleModuleMapper.delete(RoleModule.builder().roleId(roleId).build());
        List<RoleModule> roleModuleList = new ArrayList<RoleModule>();
        for(Integer m:moduleIds){
            roleModuleList.add( RoleModule.builder().moduleId(m).roleId(roleId).build());
        }
        return roleModuleMapper.insertList(roleModuleList) > 0;
    }


}
