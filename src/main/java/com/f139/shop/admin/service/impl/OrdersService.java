package com.f139.shop.admin.service.impl;

import com.f139.shop.admin.entity.Orders;
import com.f139.shop.admin.entity.OrdersLogistic;
import com.f139.shop.admin.mapper.OrdersLogisticMapper;
import com.f139.shop.admin.mapper.OrdersMapper;
import com.f139.shop.admin.pojo.OrdersProduct;
import com.f139.shop.admin.service.IOrdersService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class OrdersService implements IOrdersService {

    @Resource
    private OrdersMapper orderMapper;

    @Resource
    private OrdersLogisticMapper ordersLogisticMapper;

    @Override
    public PageInfo<Orders> getOrdersList(Page<Orders> startPage, Orders order) {
        Example example = new Example(Orders.class);
        if(Objects.isNull(order)){
            example.createCriteria().andLike("name","%"+order.getName()+"%");
        }
        List<Orders> list = orderMapper.selectByExample(example);
        PageInfo<Orders> pageInfo = new PageInfo<Orders>(list);
        return pageInfo;
    }

    @Override
    public List<OrdersLogistic> getOrdersLogistic(String orderId) {
        Example example = new Example(OrdersLogistic.class);
        example.createCriteria().andEqualTo("orderId",orderId);
        example.orderBy("datetime");
        return ordersLogisticMapper.selectByExample(example);
    }

    @Override
    public List<OrdersProduct> getOrdersProduct() {
        return orderMapper.selectOrdersProduct();
    }
}


