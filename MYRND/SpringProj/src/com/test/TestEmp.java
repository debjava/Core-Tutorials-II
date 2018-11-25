package com.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class TestEmp 
{
	public static void main(String[] args) 
	{
		try
		{
			Resource  res = new ClassPathResource("temp.xml");
			BeanFactory  factory = new XmlBeanFactory(res);
//			Emp emp = (Emp)factory.getBean("myemp");
//			System.out.println(emp.getName());
//			System.out.println("Adrs----->>>"+emp.getAdrs());
			
			Company com = ( Company )factory.getBean("mycompany");
			System.out.println(com.getLocation());
			
			Emp emp = (Emp)com.getEmp();
			System.out.println("Name---->"+emp.getName());
			System.out.println("Adrs---->"+emp.getAdrs());
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
