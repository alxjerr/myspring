package com.myspring.service.impl;

import com.myspring.annotation.ExtResource;
import com.myspring.annotation.ExtService;
import com.myspring.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@ExtService
public class UserServiceImpl implements UserService {

    @ExtResource
    private OrderServiceImpl orderServiceImpl;

    @Override
    public void add() {
        orderServiceImpl.addOrder();
        System.out.println("UserService: 使用反射初始化对象。。");
    }
}
