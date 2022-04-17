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
@Table(name = "attribute")
public class Attribute {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "category_Id")
    private Integer categoryId;

    @Column(name= "type")
    private String type;

    @Transient //不存在的字段
    private List<AttributeValue> attributeValue;
}