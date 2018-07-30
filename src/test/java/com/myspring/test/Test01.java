package com.myspring.test;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 *  解析xml
 */
public class Test01 {

    public static void main(String[] args) throws Exception {
        /*lassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService);*/

        new Test01().test01();
    }


    public void test01() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        //读取xml文件
        Document document = saxReader.read(getResourceAsStream("student.xml"));
        //读取根节点
        Element rootElement = document.getRootElement();
        getNodes(rootElement);
    }

    public void getNodes(Element rootElement){
        System.out.println("获取节点名称：" + rootElement.getName());
        // 获取属性方法
        List<Attribute> attributes = rootElement.attributes();
        for(Attribute attribute : attributes){
            System.out.println(attribute.getName() + "---" + attribute.getText());
        }
        // 获取属性value
        String textTrim = rootElement.getTextTrim();
        if(StringUtils.isEmpty(textTrim)){
            System.out.println("textTrim:" + textTrim);
        }
        // 使用迭代器获取 子节点信息
        Iterator<Element> elementIterator = rootElement.elementIterator();
        while(elementIterator.hasNext()){
            Element next = elementIterator.next();
            getNodes(next);
        }

    }

    public InputStream getResourceAsStream(String xmlPath){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xmlPath);
        return inputStream;
    }

}
