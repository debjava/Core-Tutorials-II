package org.jboss.tutorial.stateful.client;

import java.util.HashMap;
import java.util.Properties;

import javax.naming.InitialContext;
import org.jboss.tutorial.stateful.bean.ShoppingCart;

public class Client
{
	private static Properties jndiProperty = null;

	private static void setJndiProperty()
	{
		jndiProperty = new Properties();
		jndiProperty.setProperty ("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperty.setProperty ("java.naming.provider.url","jnp://localhost:1099");
		jndiProperty.setProperty ("java.naming.factory.url.pkgs",  "org.jboss.naming:org.jnp.interfaces");
	}

	public static void main(String[] args) throws Exception
	{
		setJndiProperty();
		InitialContext ctx = new InitialContext(jndiProperty);
		ShoppingCart cart = (ShoppingCart) ctx.lookup("ShoppingCartBean/remote");

		System.out.println("Buying 1 memory stick");
		cart.buy("Memory stick", 1);
		System.out.println("Buying another memory stick");
		cart.buy("Memory stick", 1);

		System.out.println("Buying a laptop");
		cart.buy("Laptop", 1);

		System.out.println("Print cart:");
		HashMap<String, Integer> fullCart = cart.getCartContents();
		for (String product : fullCart.keySet())
		{
			System.out.println(fullCart.get(product) + "     " + product);
		}

		System.out.println("Checkout");
		cart.checkout();

		System.out.println("Should throw an object not found exception by invoking on cart after @Remove method");
		try
		{
			cart.getCartContents();
		}
		catch (Exception e)
		{
			System.out.println("Successfully caught no such object exception.");
		}


	}
}
