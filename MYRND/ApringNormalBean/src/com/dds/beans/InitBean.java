package com.dds.beans;

public class InitBean 
{
	private String name = null;
	private int age = 0;
	private long salary = 0L;
	private String adrs = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getSalary() {
		return salary;
	}
	public void setSalary(long salary) {
		this.salary = salary;
	}
	public String getAdrs() {
		return adrs;
	}
	public void setAdrs(String adrs) {
		this.adrs = adrs;
	}
	
	private void init()
	{
		System.out.println("Calling the init method");
		adrs = "Karnataka";
		age = 23;
	}
}
