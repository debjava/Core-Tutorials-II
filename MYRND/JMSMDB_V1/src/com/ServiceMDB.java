package com;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ServiceMDB implements MessageDrivenBean , MessageListener 
{

	public void ejbCreate() throws CreateException
	{
		
	}
	public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException 
	{
		
	}

	public void ejbRemove() throws EJBException 
	{
		
	}

	public void onMessage(Message msg) 
	{
		try
		{
			System.out.println("************* Message received *****************");
			if(msg instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage)msg;
				String str = textMessage.getText();
				System.out.println("Received Values ::: "+str);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
