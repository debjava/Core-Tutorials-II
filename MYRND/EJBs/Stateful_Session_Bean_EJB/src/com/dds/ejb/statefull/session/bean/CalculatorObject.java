package com.dds.ejb.statefull.session.bean;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface CalculatorObject extends EJBObject 
{
	public int add( int a , int b ) throws RemoteException;
	public String getString( String name ) throws RemoteException;
}
