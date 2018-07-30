package com.myspring.service.impl;


import com.myspring.annotation.ExtService;
import com.myspring.service.OrderService;

@ExtService
public class OrderServiceImpl implements OrderService {

    @Override
    public void addOrder() {
        System.out.println("OrderService: 注解方式实现IOC...");
    }
}
