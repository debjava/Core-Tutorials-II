package org.jboss.docs.cmp.cd.interfaces;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * This interface defines the home interface for the CD Bean
 */
public interface CDHome
extends EJBHome
{

   /**
    * Create a new CD instance
    */
   public CD create(Integer id)
   throws RemoteException, CreateException;

   /**
    * Find the CD with the specified ID. This method is implemented by the
    * container
    */
   public CD findByPrimaryKey (Integer id)
   throws RemoteException, FinderException;

   /**
    * Finds the CD whose "type" attribute matches that specified.
    * This method is implemented by the container
    */
   public Collection findByType (String type)
   throws RemoteException, FinderException;

   /**
    * Get all CD instances. This method is implemented by the container
    */
   public Collection findAll()
   throws RemoteException, FinderException;

}

