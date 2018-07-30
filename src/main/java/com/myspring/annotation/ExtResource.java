package com.myspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

//自定义注解 从spring容器获取bean
@Target(value = {TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtResource {
}
