package com.dds.core.statefull.session.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface SayMyName extends EJBObject 
{	 	
   public String getMyName() throws RemoteException;
   public int getCount() throws RemoteException;   
   public void setMyName(String nameIn) throws RemoteException;
}
