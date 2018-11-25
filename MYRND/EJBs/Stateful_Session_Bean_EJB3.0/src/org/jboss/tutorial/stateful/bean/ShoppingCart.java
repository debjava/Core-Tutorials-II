package org.jboss.tutorial.stateful.bean;

import java.util.HashMap;

import javax.ejb.Remote;
import javax.ejb.Remove;

@Remote
public interface ShoppingCart
{
   void buy(String product, int quantity);

   HashMap<String, Integer> getCartContents();

   @Remove void checkout();
}
