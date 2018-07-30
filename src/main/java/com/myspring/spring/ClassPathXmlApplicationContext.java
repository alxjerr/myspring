package com.myspring.spring;



import com.myspring.annotation.ExtService;
import com.myspring.utils.ClassUtil;
import org.apache.commons.lang.StringUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手写spring 注解版本注入bean
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ClassPathXmlApplicationContext {

    //扫包范围
    private String packageName;
    ConcurrentHashMap<String,Object> initBean = null;

    public ClassPathXmlApplicationContext(String packageName) {
        this.packageName = packageName;
    }

    //使用beanID查找对象
    public Object getBean(String beanId) throws Exception {
        //1.使用反射机制获取该包下所有的类已经存在bean的注解类
        List<Class> listClassesAnnotation = findClassExisService();
        if(listClassesAnnotation == null || listClassesAnnotation.isEmpty()){
            throw new Exception("没有需要初始化的bean");
        }
        //2.使用java反射机制初始化对象
        initBean = initBean(listClassesAnnotation);
        if (initBean == null || initBean.isEmpty()) {
            throw new Exception("初始化bean为空!");
        }

        //3.使用beanID查找对应bean对象
        Object object = initBean.get(beanId);

        //4.使用反射机制读取类的属性，赋值信息
        attriAssign(object);

        return object;
    }

    //使用反射读取类的属性，赋值信息
    private void attriAssign(Object object) throws IllegalAccessException {
        //1.获取类的属性是否存在 获取bean注解
        Class<? extends Object> classInfo = object.getClass();
        Field[] declaredFields = classInfo.getDeclaredFields();
        for(Field field : declaredFields){
            //属性名
            String name = field.getName();
            //2.使用属性名称查找bean容器赋值
            Object bean = initBean.get(name);
            if(bean != null){
                //私有访问允许访问
                field.setAccessible(true);
                field.set(object,bean);
                continue;
            }
        }

    }

    //初始化bean对象
    private ConcurrentHashMap<String,Object> initBean(List<Class> listClassesAnnotation) throws Exception {
        ConcurrentHashMap<String,Object> concurrentHashMap = new ConcurrentHashMap();
        for(Class classInfo : listClassesAnnotation){
            //初始化对象
            Object newInstance = classInfo.newInstance();
            //获取父类名称
            String beanId = toLowerCaseFirstOne(classInfo.getSimpleName());
        }
        return concurrentHashMap;
    }

    //首字母转小写
    private String toLowerCaseFirstOne(String simpleName) {
        if(Character.isLowerCase(simpleName.charAt(0)))
            return simpleName;
        else
            return (new StringBuilder()).append(Character.toLowerCase(simpleName.charAt(0))).append(simpleName.substring(1)).toString();
    }

    //使用反射机制获取该包下所有的类已经存在bean的注解类
    private List<Class> findClassExisService() throws Exception {
        //1.使用反射机制获取该包下所有的类
        if(StringUtils.isEmpty(packageName)){
            throw new Exception("扫包地址不能为空");
        }
        //2.使用反射技术获取当前包下所有的类
        List<Class<?>> classesByPackageName = ClassUtil.getClasses(packageName);
        //3.注入注解
        List<Class> exisClassesAnnotation = new ArrayList<Class>();
        //4.判断该类上是否存在注解
        for(Class classInfo : classesByPackageName){
            ExtService extService = (ExtService) classInfo.getDeclaredAnnotation(ExtService.class);
            if(extService != null){
                exisClassesAnnotation.add(classInfo);
                continue;
            }
        }
        return exisClassesAnnotation;
    }
}
