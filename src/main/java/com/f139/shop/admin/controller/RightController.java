package com.f139.shop.admin.controller;

import com.f139.shop.admin.entity.Module;
import com.f139.shop.admin.entity.Role;
import com.f139.shop.admin.entity.RoleModule;
import com.f139.shop.admin.entity.UserInfo;
import com.f139.shop.admin.entity.ViewRoleModule;
import com.f139.shop.admin.service.IRightService;
import com.github.pagehelper.PageHelper;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@ResponseBody
@RequestMapping("/rights")
public class RightController {

    @Resource
    private IRightService rightService;

    @GetMapping(value = "modules")
    public List<Module> modules() {
        return rightService.getModules(0);
    }

    @GetMapping(value = "list")
    public List<Module> list() {
        return rightService.getModulesList();
    }

    @GetMapping(value = "roles")
    public List<Role> roles() {
        return rightService.getRoles();
    }

    @GetMapping(value = "/role/{id}")
    public Role getRole(@PathVariable Integer id){
        return rightService.getRole(id);
    }

    @DeleteMapping(value="/role/{roleId}/module/{moduleId}")
    public Boolean deleteRoleModule(@PathVariable(value = "roleId") Integer roleId,@PathVariable(value="moduleId") Integer moduleId){
        return rightService.deleteRoleModule(roleId,moduleId);
    }

    @GetMapping(value="/roleModule/{roleId}")
    public List<ViewRoleModule> getRoleModule(@PathVariable(value = "roleId") Integer roleId){
        return rightService.getRoleModule(roleId);
    }

    @PostMapping(value = "/roleModule/{roleId}")
    public Boolean saveRoleModule(@PathVariable Integer roleId,@RequestBody Map<String, Object> map){
        String moduleIds = (String) map.get("moduleIds");
        Integer[] array = Arrays.stream(moduleIds.split(",")).map(s -> Integer.parseInt(s)).toArray(Integer[]::new);
        return rightService.saveRoleModule(roleId,array);
    }
}
