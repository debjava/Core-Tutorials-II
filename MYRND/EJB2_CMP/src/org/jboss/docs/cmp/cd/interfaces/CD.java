package org.jboss.docs.cmp.cd.interfaces;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * This interface defines the remote interface for the CD Bean
 */
public interface CD
extends EJBObject
{
   public Integer getId() throws RemoteException;

   public void setId(Integer id) throws RemoteException;

   public String getTitle() throws RemoteException;

   public void setTitle(String title) throws RemoteException;

   public String getArtist() throws RemoteException;

   public void setArtist(String artist) throws RemoteException;

   public String getType() throws RemoteException;

   public void setType(String type) throws RemoteException;

   public String getNotes() throws RemoteException;

   public void setNotes(String type) throws RemoteException;

}

