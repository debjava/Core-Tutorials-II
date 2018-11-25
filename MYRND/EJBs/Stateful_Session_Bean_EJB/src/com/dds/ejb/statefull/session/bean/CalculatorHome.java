package com.dds.ejb.statefull.session.bean;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface CalculatorHome extends EJBHome 
{
	public CalculatorObject create() throws CreateException,RemoteException;
}
