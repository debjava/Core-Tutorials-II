package com.dds.core.statefull.session.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface SayMyNameHome extends EJBHome 
{
	public SayMyName create() throws RemoteException, CreateException;
}

