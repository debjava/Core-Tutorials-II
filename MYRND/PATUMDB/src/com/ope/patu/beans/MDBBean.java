package com.ope.patu.beans;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

public class MDBBean implements MessageDrivenBean, MessageListener
{
	private static final long serialVersionUID = 8230680554167503465L;
	
	private MessageDrivenContext context = null;
	
	public void ejbCreate() 
	{
		
	}

	public void ejbRemove() throws EJBException 
	{
		context=null;
	}

	public void setMessageDrivenContext(MessageDrivenContext arg0)
	throws EJBException 
	{
		this.context = context;
	}

	public void onMessage( Message msg ) 
	{
		System.out.println("*******************************************");
		System.out.println("Receiving messages at server side");
		System.out.println("*******************************************");
		try
		{
			if( msg instanceof TextMessage )
				processTextMessage(msg);
			if( msg instanceof MapMessage )
				processMapMessage(msg);
			if( msg instanceof ObjectMessage )
				processObjectMessage(msg);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	private void processObjectMessage( Message msg )
	{
		try
		{
			ObjectMessage objectMessage = ( ObjectMessage )msg;
			EmpBean empBean = ( EmpBean )objectMessage.getObject();
			System.out.println("Name----"+empBean.getName());
			System.out.println("Age----"+empBean.getAge());
			System.out.println("Salary----"+empBean.getSalary());
			System.out.println("Adrs----"+empBean.getAdrs());
		}	
		catch( JMSException jme )
		{
			System.out.println("JMSException thrown");
			jme.printStackTrace();
		}
		catch (Exception e) 
		{
			System.out.println("Other Exception thrown");
			e.printStackTrace();
		}
	}
	
	private void processMapMessage( Message msg )
	{
		try 
		{
			MapMessage mapMessage = ( MapMessage )msg;
			String temp = mapMessage.getString("temp");
			System.out.println("Temp Map Value :------>>>"+temp);
		}
		catch (JMSException e) 
		{
			System.out.println("JMSException thrown");
			e.printStackTrace();
		}
		catch( Exception e )
		{
			System.out.println("Other Exception thrown");
			e.printStackTrace();
		}
	}
	
	private void processTextMessage( Message msg )
	{
		TextMessage textMessage = ( TextMessage )msg;
		try 
		{
			System.out.println("Contents of text message :--->>>"+textMessage.getText());
		} 
		catch (JMSException e) 
		{
			System.out.println("JMSException thrown");
			e.printStackTrace();
		}
		catch( Exception e )
		{
			System.out.println("Other Exception thrown");
			e.printStackTrace();
		}
	}
}
