package com.ope.patu.beans;

import java.io.Serializable;

public class EmpBean implements Serializable
{
	
	private static final long serialVersionUID = -413551467595493148L;
	private String name;
	private int age;
	private long salary;
	private String adrs;
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
	
}
