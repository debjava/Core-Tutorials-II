package ser;

import java.util.Properties;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class TestReceiver implements MessageListener 
{
	Properties jndiProperty = null;
	InitialContext initalContext = null;
	Topic topic = null ;
	TopicConnection topicConnection = null;
	TopicSession session = null;
	
	public void receiver()
	{
		try
		{
			setJndiProperty();
			TopicSession session = jndiInit() ;
			TopicSubscriber subscriber=session.createSubscriber(topic);
			subscriber.setMessageListener(this);
			topicConnection.start();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void setJndiProperty()
	{
		jndiProperty = new Properties();
		jndiProperty.setProperty ("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperty.setProperty ("java.naming.provider.url",  "127.0.0.1:1099");
		jndiProperty.setProperty ("java.naming.factory.url.pkgs",  "org.jboss.naming:org.jnp.interfaces");
	}
	
	public TopicSession jndiInit()
	{
		try
		{
		
			initalContext = new InitialContext( jndiProperty );
			TopicConnectionFactory tcf = (TopicConnectionFactory) initalContext.lookup("UIL2ConnectionFactory");
			topic = (Topic)initalContext.lookup("topic/liluTopic");
			topicConnection = tcf.createTopicConnection();
			session = topicConnection.createTopicSession(false,TopicSession.AUTO_ACKNOWLEDGE);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return session ; 
	}

	public void onMessage(Message msg) 
	{
		System.out.println("Message Received ......... ");
		 try
		 {
			 if(msg instanceof TextMessage )
			 {
				 TextMessage textmessage=(TextMessage)msg;
				 String ss = textmessage.getText();
				 System.out.println(ss);
			 }
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}

}
