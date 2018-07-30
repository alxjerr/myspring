package com.myspring.test;

import com.myspring.initIocOfAnntation.ExtClassPathXmlApplicationContext;
import com.myspring.service.UserService;

public class Test04 {

    public static void main(String[] args) throws Exception {
        // 使用注解版本事务的时候，第一步需要扫包

        ExtClassPathXmlApplicationContext app =
                new ExtClassPathXmlApplicationContext("com.myspring.service.impl");

        UserService userService = (UserService) app.getBean("userServiceImpl");
        userService.add();
    }

}
