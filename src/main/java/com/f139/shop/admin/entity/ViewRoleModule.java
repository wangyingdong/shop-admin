package com.f139.shop.admin.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "view_role_module")
public class ViewRoleModule {
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "level")
    private Integer level;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "module_id")
    private Integer moduleId;

    private List<ViewRoleModule> children;
}