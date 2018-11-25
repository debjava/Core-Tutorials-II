package com.dds.rnd;

import javax.jms.TextMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

public class MessagePublisher
{
	private JMSTopicUtil jmsTopicUtil = null;
	private TopicSession topicSession = null;
	private TopicPublisher topicPublisher = null;
	
	private void init()
	{
		try
		{
			jmsTopicUtil = new JMSTopicUtil();
			topicSession = jmsTopicUtil.jndiInit();
			topicPublisher = topicSession.createPublisher( jmsTopicUtil.getTopic() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	public void send()
	{
		publish();
	}
	
	public void publish()
	{
		try
		{
			init();
			jmsTopicUtil.getTopicConnection().start();
			TextMessage msg = topicSession.createTextMessage();
			StringBuffer body = new StringBuffer();
			body.append("<?xml version=\"1.0\"?>\n");
			msg.setText(body.toString());
			topicPublisher.publish(msg);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		new MessagePublisher().publish();
	}
}
