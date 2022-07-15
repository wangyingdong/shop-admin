package com.f139.shop.admin.service;

import com.f139.shop.admin.entity.Module;
import com.f139.shop.admin.entity.Role;
import com.f139.shop.admin.entity.ViewRoleModule;

import java.util.List;

public interface IRightService {

    List<Module> getModules(Integer parentId);

    List<Module> getModules();

    List<Role> getRoles();

    Role getRole(Integer roleId);

    List<ViewRoleModule> getRoleModule(Integer roleId);

    Boolean deleteRoleModule(Integer roleId, Integer moduleId);

    Boolean saveRoleModule(Integer roleId, Integer[] moduleIds);

    List<ViewRoleModule> getModulesByUserId(Long userId);
}
