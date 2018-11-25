package org.jboss.docs.interest;

import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
This interface defines the 'home' interface for the 'Interest' EJB. 
*/
public interface InterestHome extends EJBHome 
{
   /**
   Creates an instance of the `InterestBean' class on the server, and returns a
   remote reference to an Interest interface on the client. 
   */
   Interest create() throws RemoteException, CreateException;
}
