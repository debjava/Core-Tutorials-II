import java.util.Properties;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import com.dds.ejb.stateless.session.bean.CalculatorHome;
import com.dds.ejb.stateless.session.bean.CalculatorObject;


public class TestStatelessSessionBean 
{
	private static Properties jndiProperty = null;

	private static void setJndiProperty()
	{
		jndiProperty = new Properties();
		jndiProperty.setProperty ("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperty.setProperty ("java.naming.provider.url","jnp://localhost:1099");
		jndiProperty.setProperty ("java.naming.factory.url.pkgs",  "org.jboss.naming:org.jnp.interfaces");
	}

	public static void main(String[] args) 
	{
		try
		{
			setJndiProperty();
			// Get a naming context
			InitialContext jndiContext = new InitialContext( jndiProperty );
			System.out.println("Got context");

			// Get a reference to the Interest Bean
			Object ref  = jndiContext.lookup("calculator/Calculator");
			System.out.println("Got reference");

			// Get a reference from this to the Bean's Home interface
			CalculatorHome calcHome = ( CalculatorHome )PortableRemoteObject.narrow(ref, CalculatorHome.class);

			// Create an Interest object from the Home interface
			CalculatorObject calcObj = calcHome.create();

			int addnValue = calcObj.add(40, 20);

			System.out.println("Calculated value using EJB----->>>"+addnValue);

			System.out.println("------>>>"+calcObj.getString("Deba"));

		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

}
