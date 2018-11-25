
import java.util.Properties;

import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class PublishIO
{
	Properties jndiProperty = null;
	InitialContext initalContext = null;
	Topic topic = null ;
	TopicConnection topicConnection = null;
	TopicSession session = null;
	
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
//			topic = (Topic)initalContext.lookup("topic/pikuTopic");
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

