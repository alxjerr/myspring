package com.myspring.test;

import com.myspring.initIocOfxml.ClassPathXmlApplicationContext;
import com.myspring.service.UserService;

//测试自己写的SpringIOC
public class Test03 {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("spring.xml");
        UserService userService = (UserService) app.getBean("userService");
        userService.add();
    }
}
