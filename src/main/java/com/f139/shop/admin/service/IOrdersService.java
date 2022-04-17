package com.f139.shop.admin.service;

import com.f139.shop.admin.entity.Orders;
import com.f139.shop.admin.entity.OrdersLogistic;
import com.f139.shop.admin.pojo.OrdersProduct;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IOrdersService {

    PageInfo<Orders> getOrdersList(Page<Orders> startPage, Orders order);

    List<OrdersLogistic> getOrdersLogistic(String orderId);

    List<OrdersProduct> getOrdersProduct();

}
