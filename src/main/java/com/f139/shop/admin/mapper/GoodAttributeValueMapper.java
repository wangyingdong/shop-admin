package com.f139.shop.admin.mapper;

import com.f139.shop.admin.common.mapper.BasicMapper;
import com.f139.shop.admin.entity.GoodAttributeValue;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Mapper
public interface GoodAttributeValueMapper extends BasicMapper<GoodAttributeValue>, InsertListMapper<GoodAttributeValue> {
}