package com.myspring.test;

import com.myspring.init.ClassPathXmlApplicationContext;
import com.myspring.service.UserService;

public class Test02 {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("spring.xml");
        UserService userService = (UserService) app.getBean("userService");
        System.out.println(userService);
    }

}
