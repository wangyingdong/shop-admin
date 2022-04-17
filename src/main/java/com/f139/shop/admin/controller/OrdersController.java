package com.f139.shop.admin.controller;

import com.f139.shop.admin.entity.Orders;
import com.f139.shop.admin.entity.OrdersLogistic;
import com.f139.shop.admin.pojo.OrdersProduct;
import com.f139.shop.admin.service.IOrdersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService orderService;



    @GetMapping
    public PageInfo<Orders> getOrdersList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                         @RequestParam(value = "query", defaultValue = "") String query){

        return orderService.getOrdersList(PageHelper.startPage(pageNum, pageSize), Orders.builder().name(query).build());
    }

    @GetMapping(value = "/{orderId}/logistic")
    private List<OrdersLogistic> getOrdersLogistic(@Validated @NotNull @PathVariable String orderId){
        return orderService.getOrdersLogistic(orderId);
    }

    @GetMapping(value = "/report")
    public List<OrdersProduct> getOrdersReport(){
        return orderService.getOrdersProduct();
    }

}
