package com.f139.shop.admin.mapper;

import com.f139.shop.admin.common.mapper.BasicMapper;
import com.f139.shop.admin.entity.RoleModule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Mapper
@Repository
public interface RoleModuleMapper extends BasicMapper<RoleModule>, InsertListMapper<RoleModule> {
}