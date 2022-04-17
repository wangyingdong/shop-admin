package com.f139.shop.admin.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "good_attribute_value")
public class GoodAttributeValue {
    @Id
    @Column(name = "good_id")
    private Integer goodId;

    @Id
    @Column(name = "attribute_value_id")
    private Integer attributeValueId;
}