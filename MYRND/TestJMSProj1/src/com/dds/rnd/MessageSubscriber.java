package com.dds.rnd;


import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class MessageSubscriber implements MessageListener
{
	
	public void subscribe()
	{
		receive();
	}
	public void receive()
	{
		try
		{
			JMSTopicUtil jmsUtil = new JMSTopicUtil();
			TopicSession topicSession = jmsUtil.jndiInit();
			TopicSubscriber topicSubscriber = topicSession.createSubscriber(jmsUtil.getTopic());
			topicSubscriber.setMessageListener(this);
			jmsUtil.getTopicConnection().start();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void onMessage(Message msg) 
	{
		System.out.println("Listening messages");
		 try
		 {
			 if(msg instanceof TextMessage )
			 {
				 System.out.println("****************** Receiving TextMessage ******************");
				 TextMessage textmessage=(TextMessage)msg;
				 String ss = textmessage.getText();
				 System.out.println(ss);
			 }
			 if( msg instanceof MapMessage )
			 {
				 System.out.println("****************** Receiving MapMessage ******************");
				 MapMessage mapMsg = (MapMessage)msg;
			 }
			 
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	public static void main(String[] args) 
	{
		new MessageSubscriber().subscribe();
	}
}
