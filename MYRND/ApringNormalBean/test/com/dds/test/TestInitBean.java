package com.dds.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.dds.beans.InitBean;

public class TestInitBean 
{
	public static void main(String[] args) 
	{
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"conf/initbean.xml");
		InitBean initBean = (InitBean)context.getBean("initbean");
		System.out.println("Age-------"+initBean.getAge());
		System.out.println("Adrs------"+initBean.getAdrs());
		System.out.println("Name-------"+initBean.getName());
		System.out.println("Salary----"+initBean.getSalary());
	}
}
