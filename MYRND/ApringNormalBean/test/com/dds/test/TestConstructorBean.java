package com.dds.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.dds.beans.ConstructorBean;

public class TestConstructorBean
{
	public static void main(String[] args) 
	{
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"conf/constructorbean.xml");
		ConstructorBean conBean = ( ConstructorBean )context.getBean("constructorBean");
		System.out.println("Age-------"+conBean.getAge());
		System.out.println("Name------"+conBean.getName());
	}
}
