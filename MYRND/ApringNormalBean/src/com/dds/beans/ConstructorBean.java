package com.dds.beans;

public class ConstructorBean 
{
	private String name;
	private int age ;
	
	public ConstructorBean( int age )
	{
		super();
		this.age = age ;
	}
	
	public ConstructorBean( String name )
	{
		super();
		this.name = name;
	}
	
	public ConstructorBean( String name , int age )
	{
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
}
