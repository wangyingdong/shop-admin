package com.f139.shop.admin.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull(groups = {Save.class,Update.class})
    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "parent_id")
    @NotNull(groups = {Save.class,Update.class})
    private Integer parentId;

    @Column(name = "sort")
    @NotNull(groups = {Save.class})
    private Integer sort;

    @Column(name = "level")
    @NotNull(groups = {Save.class})
    private Integer level;

    @Column(name = "state")
    @NotNull(groups = {Save.class})
    private Integer state;

    @Transient //不存在的字段
    private List<Category> children;

    public interface Save{};

    public interface Update{};


}