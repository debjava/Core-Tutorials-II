package org.jboss.docs.interest;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
This interface defines the `Remote' interface for the `Interest' EJB. Its
single method is the only method exposed to the outside world. The class
InterestBean implements the method.
*/
public interface Interest extends EJBObject 
{
   /**
   Calulates the compound interest on the sum `principle', with interest rate per
   period `rate' over `periods' time periods. This method also prints a message to
   standard output; this is picked up by the EJB server and logged. In this way we
   can demonstrate that the method is actually being executed on the server,
   rather than the client.
   */
   public double calculateCompoundInterest(double principle, 
				double rate, double periods) throws RemoteException;
}
