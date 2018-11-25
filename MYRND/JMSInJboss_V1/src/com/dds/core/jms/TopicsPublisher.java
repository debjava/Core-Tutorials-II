package com.dds.core.jms;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

public class TopicsPublisher 
{

	public static void main(String[] args) 
	{
		try
		{
			TopicSession topicSession = JMSUtil.getTopicSession();
			TopicPublisher topicPublisher = topicSession.createPublisher(JMSUtil.getTopic());
			
			TextMessage msg = topicSession.createTextMessage();
			StringBuffer body=new StringBuffer();
			body.append("Hi How are you ????");
			msg.setText(body.toString());
			topicPublisher.publish(msg);
			
		}
		catch( JMSException jme )
		{
			jme.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}
