package com.myspring.initIocOfxml;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class ClassPathXmlApplicationContext {

    //xml读取路径地址
    private String xmlPath;

    public ClassPathXmlApplicationContext(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public Object getBean(String beanId) throws Exception {
        if(StringUtils.isEmpty(beanId)){
            throw new Exception("beanId不能为空");
        }
        //1.解析xml文件,获取所有bean节点信息
        List<Element> readerXML = readerXML();
        if(readerXML == null || readerXML.isEmpty()){
            throw new Exception("配置文件中没有配置bean信息");
        }
        //2.使用方法参数beanid查找配置文件中bean节点的id信息是否一致
        String className = findByElementClass(readerXML,beanId);
        if(StringUtils.isEmpty(className)){
            throw new Exception("该bean对象没有配置class地址");
        }
        return newInstance(className);
    }

    // 使用方法参数beanid查找配置文件中bean节点的id信息是否一致
    public String findByElementClass(List<Element> readerXML,String beanId){
        for(Element element : readerXML){
            // 获取属性信息
            String xmlBeanId = element.attributeValue("id");
            if(StringUtils.isEmpty(xmlBeanId)){
                continue;
            }
            if(xmlBeanId.equals(beanId)){
                String xmlClass = element.attributeValue("class");
                return xmlClass;
            }
        }
        return beanId;
    }

    // 解析xml文件
    public List<Element> readerXML() throws DocumentException {
        //1.解析xml文件信息
        SAXReader saxReader = new SAXReader();
        // 读取xml文件
        Document document = saxReader.read(getResourceAsStream(xmlPath));

        //2.读取根节点
        Element rootElement = document.getRootElement();
        //3.获取根节点下所有的子节点
        List<Element> elements = rootElement.elements();
        return elements;
    }

    //初始化对象
    public Object newInstance(String className) throws Exception {
        Class<?> classInfo = Class.forName(className);
        return classInfo.newInstance();
    }

    // 获取当前上下文路径
    public InputStream getResourceAsStream(String xmlPath){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xmlPath);
        return inputStream;
    }
}
