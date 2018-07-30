package com.myspring.initIocOfAnntation;

import com.myspring.annotation.ExtResource;
import com.myspring.annotation.ExtService;
import com.myspring.utils.ClassUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手写SpringIOC 注解版本
 */
public class ExtClassPathXmlApplicationContext {

    // 扫包的范围
    private String packageName;

    // SpringBean 容器
    private static ConcurrentHashMap<String,Object> beans = null;

    public ExtClassPathXmlApplicationContext(String packageName) throws Exception {
        beans = new ConcurrentHashMap<>();
        this.packageName = packageName;
        initBean();

        initEntryFiled();
    }

    //给类的属性赋值
    public void initEntryFiled() throws Exception {
        // 1.遍历所有bean容器对象
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            // 2.判断属性上面是否有加注解
            Object bean = entry.getValue();
            attriAssign(bean);
        }
    }

    public Object getBean(String beanId) throws Exception {
        if(StringUtils.isEmpty(beanId)){
            throw new Exception("beanId参数不能为空");
        }
        // 从spring容器获取bean
        Object object = beans.get(beanId);

        return object;
    }

    // 使用java的反射机制进行初始化
    public Object newInstance(Class<?> classInfo) throws Exception {
        return classInfo.newInstance();
    }

    // 初始化对象
    public void initBean() throws Exception {
        //1.使用java的反射机制扫包，获取当前包下所有的类
        List<Class<?>> classes = ClassUtil.getClasses(packageName);
        //2.判断类上是否存在注入bean的注解
        Map<String,Object> classExisAnnotation = findClassExisAnnotation(classes);
        if(classExisAnnotation == null || classExisAnnotation.isEmpty()){
            throw new Exception("该包下没有任何类加上注解");
        }
        //3.使用java的反射机制进行初始化
    }

    //判断类上面是否存在注入bean的注解
    public ConcurrentHashMap<String, Object> findClassExisAnnotation(List<Class<?>> classes) throws Exception {
        for(Class<?> classInfo : classes){
            //判断类上是否有注解
            ExtService annotation = classInfo.getAnnotation(ExtService.class);
            if(annotation != null){
                // 获取当前类名 ;
                String className = classInfo.getSimpleName();
                // beanId 类名小写
                String beanId = toLowerCaseFirstOne(className);
                Object newInstance = newInstance(classInfo);
                attriAssign(newInstance);
                beans.put(beanId,newInstance);
            }
        }
        return beans;
    }


    //首字母转小写
    private String toLowerCaseFirstOne(String simpleName) {
        if(Character.isLowerCase(simpleName.charAt(0)))
            return simpleName;
        else
            return (new StringBuilder()).append(Character.toLowerCase(simpleName.charAt(0))).append(simpleName.substring(1)).toString();
    }


    /**
     * 依赖注入原理：
     * 1.使用反射机制，获取当前类的所有属性
     * 2.判断当前类属性是否存在注解
     * 3.默认使用属性名称，查找bean容器对象
     */

    public void attriAssign(Object object) throws Exception {

        // 1.使用反射机制，获取当前类的所有属性
        Class<?> classInfo = object.getClass();
        Field[] declaredFields = classInfo.getDeclaredFields();

        // 2.判断当前类属性是否存在注解
        for(Field field : declaredFields){
            ExtResource extResource = field.getAnnotation(ExtResource.class);
            if(extResource != null){
                //获取属性名称
                String beanId = field.getName();
                Object bean = getBean(beanId);
                if(bean != null){
                    // 3.默认使用属性名称，查找bean容器对象
                    // object:当前对象 ; bean:给属性赋值
                    field.setAccessible(true);
                    field.set(object,bean);
                }
            }
        }
    }



}
