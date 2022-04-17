package com.f139.shop.admin.mapper;

import com.f139.shop.admin.common.mapper.BasicMapper;
import com.f139.shop.admin.entity.Module;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ModulesMapper extends BasicMapper<Module> {
}