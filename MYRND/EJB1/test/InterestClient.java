import java.util.Properties;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.jboss.docs.interest.Interest;
import org.jboss.docs.interest.InterestHome;

/** This simple application tests the 'Interest' Enterprise JavaBean which is
 implemented in the package 'org.jboss.docs.interest'. For this to work, the
 Bean must be deployed on an EJB server.
 */
class InterestClient
{
	
	private static Properties jndiProperty = null;
	
	private static void setJndiProperty()
	{
		jndiProperty = new Properties();
		jndiProperty.setProperty ("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperty.setProperty ("java.naming.provider.url","jnp://localhost:1099");
		jndiProperty.setProperty ("java.naming.factory.url.pkgs",  "org.jboss.naming:org.jnp.interfaces");
	}
	
   /** This method does all the work. It creates an instance of the Interest EJB on
    the EJB server, and calls its `calculateCompoundInterest()' method, then prints
    the result of the calculation.
    */
   public static void main(String[] args)
   {     
      // Enclosing the whole process in a single `try' block is not an ideal way
      // to do exception handling, but I don't want to clutter the program up
      // with catch blocks
      try
      {
    	  setJndiProperty();
         // Get a naming context
         InitialContext jndiContext = new InitialContext( jndiProperty );
         System.out.println("Got context");
         
         // Get a reference to the Interest Bean
         Object ref  = jndiContext.lookup("interest/Interest");
         System.out.println("Got reference");
         
         // Get a reference from this to the Bean's Home interface
         InterestHome home = (InterestHome)
         PortableRemoteObject.narrow(ref, InterestHome.class);
         
         // Create an Interest object from the Home interface
         Interest interest = home.create();
         
         // call the calculateCompoundInterest() method to do the calculation
         System.out.println("Interest on 1000 units, at 10% per period, compounded over 2 periods is:");
         System.out.println(interest.calculateCompoundInterest(1000, 0.10, 2));
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
}