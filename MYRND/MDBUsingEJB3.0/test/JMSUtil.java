
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSUtil 
{
	//~~ For Topic
	private static Topic topic = null ;
	private static TopicConnection topicConnection = null;
	private static TopicSession topicSession = null;
	//~~ For Queue
	private static Queue queue = null;
	private static QueueConnection queueConnection = null;
	private static QueueSession queueSession = null;
	
	public static Queue getQueue()
	{
		return queue;
	}
	
	public static QueueConnection getQueueConnection() 
	{
		return queueConnection;
	}
	
	public static TopicConnection getTopicConnection() 
	{
		return topicConnection;
	}
	
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
			topicSession = topicConnection.createTopicSession(false,TopicSession.AUTO_ACKNOWLEDGE);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return topicSession ; 
	}
	
	public static QueueSession getQueueSession()
	{
		try 
		{
			InitialContext initalContext = new InitialContext( getJndiProperties() );
			QueueConnectionFactory queueConnFactory = (QueueConnectionFactory)initalContext.lookup("UIL2ConnectionFactory");
			queue = (Queue)initalContext.lookup("queue/testQueue");
			queueConnection = queueConnFactory.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false,TopicSession.AUTO_ACKNOWLEDGE);
		}
		catch (NamingException e) 
		{
			e.printStackTrace();
		}
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
		return queueSession;
	}
	
}
