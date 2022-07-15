package com.f139.shop.admin.controller;

import com.f139.shop.admin.entity.Module;
import com.f139.shop.admin.entity.Role;
import com.f139.shop.admin.entity.ViewRoleModule;
import com.f139.shop.admin.service.IRightService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "authorities")
    public List<Module> authorities() {
        return rightService.getModules();
    }

    @GetMapping(value = "/modules/{userId}")
    public List<ViewRoleModule> modulesByUserName(@PathVariable(value = "userId")Long userId) {
        return rightService.getModulesByUserId(userId);
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
        Integer[] array = null;
        if(StringUtils.hasText(moduleIds)){
            array = Arrays.stream(moduleIds.split(",")).map(s -> Integer.parseInt(s)).toArray(Integer[]::new);
        }
        return rightService.saveRoleModule(roleId,array);
    }
}
