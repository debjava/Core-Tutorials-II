package com.dds.core.jms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class JMSUtil 
{
	private static Topic topic = null ;
	private static TopicConnection topicConnection = null;
	public static TopicConnection getTopicConnection() {
		return topicConnection;
	}

	private static TopicSession session = null;
	
	public static Topic getTopic() 
	{
		return topic;
	}
	
	public static Properties getJndiProperties()
	{
		Properties jndiProp = new Properties();
		try
		{
			InputStream in = new FileInputStream("config/jbossjndi.properties");
			jndiProp.load(in);
		}
		catch( FileNotFoundException fnfe )
		{
			fnfe.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return jndiProp;
	}
	
	public static TopicSession getTopicSession()
	{
		try
		{
		
			InitialContext initalContext = new InitialContext( getJndiProperties() );
			TopicConnectionFactory tcf = (TopicConnectionFactory) initalContext.lookup("UIL2ConnectionFactory");
			topic = (Topic)initalContext.lookup("topic/testTopic");
			topicConnection = tcf.createTopicConnection();
			session = topicConnection.createTopicSession(false,TopicSession.AUTO_ACKNOWLEDGE);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return session ; 
	}
	
}
