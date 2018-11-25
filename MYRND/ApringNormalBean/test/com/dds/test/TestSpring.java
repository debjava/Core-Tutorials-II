package com.dds.test;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.dds.beans.EmpBean;

public class TestSpring 
{
	public static void main(String[] args) 
	{
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"conf/beans.xml");
		EmpBean empBean1 = (EmpBean) context.getBean("empBean");
		System.out.println("Name-------" + empBean1.getName());
		int age = empBean1.getAge();
		System.out.println("Name-------" + age);
	}
}
