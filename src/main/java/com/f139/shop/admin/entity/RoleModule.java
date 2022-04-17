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
@Table(name = "role_module")
public class RoleModule {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Id
    @Column(name = "module_id")
    private Integer moduleId;

}