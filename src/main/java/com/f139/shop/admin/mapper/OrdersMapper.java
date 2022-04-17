package com.f139.shop.admin.mapper;

import com.f139.shop.admin.common.mapper.BasicMapper;
import com.f139.shop.admin.entity.Orders;
import com.f139.shop.admin.pojo.OrdersProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersMapper extends BasicMapper<Orders> {


    @Select("SELECT sum(amount) as amount,sum(price) as price,name FROM orders group by name")
    List<OrdersProduct> selectOrdersProduct();
}