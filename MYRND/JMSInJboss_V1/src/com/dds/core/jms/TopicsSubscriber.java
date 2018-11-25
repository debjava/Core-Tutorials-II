package com.dds.core.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class TopicsSubscriber implements MessageListener
{
	public void listenToTopic()
	{
		TopicSession topicSession = JMSUtil.getTopicSession();
		try
		{
			TopicSubscriber topicSubscriber = topicSession.createSubscriber(JMSUtil.getTopic());
			topicSubscriber.setMessageListener(this);
			JMSUtil.getTopicConnection().start();
		}
		catch( JMSException jme )
		{
			jme.printStackTrace();
		}
	}
	
	@Override
	public void onMessage(Message msg) 
	{
		if( msg instanceof TextMessage )
		{
			TextMessage textMessage = ( TextMessage )msg;
			try
			{
				System.out.println("---Text Message Received ---");
				System.out.println(textMessage.getText());
				System.out.println("---Text Message Received ---");
			}
			catch( JMSException jme )
			{
				jme.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) 
	{
		new TopicsSubscriber().listenToTopic();
	}
}
