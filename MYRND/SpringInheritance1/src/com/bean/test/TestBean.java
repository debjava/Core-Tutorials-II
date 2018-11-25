package com.bean.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestBean 
{
	public static void main(String[] args) 
	{
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				"conf/beans.xml");
		MyBean bean = (MyBean)appContext.getBean("mybean");
		System.out.println(bean+"----"+bean.hashCode());
		MyBean bean1 = (MyBean)appContext.getBean("mybean");
		System.out.println(bean1+"----"+bean1.hashCode());
		System.out.println(bean.getName());
		
	}
}
