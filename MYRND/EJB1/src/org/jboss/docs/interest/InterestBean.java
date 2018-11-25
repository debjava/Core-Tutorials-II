package org.jboss.docs.interest;

import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 This class contains the implementation for the 'calculateCompoundInterest'
 method exposed by this Bean. It includes empty method bodies for the methods
 prescribe by the SessionBean interface; these don't need to do anything in this
 simple example.
 */
public class InterestBean implements SessionBean
{
   /**
   Calulates the compound interest on the sum `principle', with interest rate per
   period `rate' over `periods' time periods. This method also prints a message to
   standard output; this is picked up by the EJB server and logged. In this way we
   can demonstrate that the method is actually being executed on the server,
   rather than the client.
    */
   public double calculateCompoundInterest(double principle,
      double rate, double periods)
   {
      System.out.println("Someone called `calculateCompoundInterest!'");
      return principle * Math.pow(1+rate, periods) - principle;
   }

   /** Empty method body
    */
   public void ejbCreate()
   {}
   /** Empty method body
    */
   public void ejbRemove()
   {}
   /** Empty method body
    */
   public void ejbActivate()
   {}
   /** Empty method body
    */
   public void ejbPassivate()
   {}
   /** Empty method body
    */
   public void setSessionContext(SessionContext sc)
   {}
}
