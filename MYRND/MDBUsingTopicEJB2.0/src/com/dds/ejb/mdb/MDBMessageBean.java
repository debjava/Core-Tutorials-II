package com.dds.ejb.mdb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MDBMessageBean implements MessageListener,MessageDrivenBean
{
	
	private static final long serialVersionUID = -7402907448047305542L;
	private MessageDrivenContext context = null;
	
	@Override
	public void onMessage(Message msg) 
	{
		System.out.println("--- Inside onMessage method ---");
		try
		{
			if( msg instanceof TextMessage )
			{
				System.out.println("It is a text message");
				TextMessage textMessage = ( TextMessage )msg;
				System.out.println(textMessage.getText());
			}
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		System.out.println("--- Inside onMessage method ---");
	}
	
	public void ejbCreate() 
	{
		
	}
	
	@Override
	public void ejbRemove() throws EJBException 
	{
		this.context = null;
	}
	
	public void setMessageDrivenContext(MessageDrivenContext arg0)
	throws EJBException 
	{
		this.context = arg0;
	}
	
}
