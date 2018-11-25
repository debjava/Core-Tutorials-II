package com.dds.ejb.statefull.session.bean;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public class CalculatorBean implements SessionBean 
{

	private static final long serialVersionUID = -2789374305418468930L;

	public void ejbActivate() throws EJBException, RemoteException 
	{
		System.out.println("Calling the ejbActivate() method");
	}

	public void ejbPassivate() throws EJBException, RemoteException 
	{
		System.out.println("Calling the ejbPassivate() method");
	}

	public void ejbCreate() throws EJBException, RemoteException 
	{
		System.out.println("Calling the ejbCreate() method");
	}

	public void ejbRemove() throws EJBException, RemoteException 
	{
		System.out.println("Calling ejbRemove() method");
	}

	public void setSessionContext(SessionContext arg0) throws EJBException,
	RemoteException
	{
		System.out.println("Calling setSessionContext() method");
	}

	public int add( int a , int b )
	{
		System.out.println("Calling the add method");
		return (a+b);
	}
	
	public String getString( String ss )
	{
		return new StringBuilder( ss ).append("_Test").toString();
	}

}
