

import java.util.Properties;

import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class JMSTopicUtil 
{
	private Properties jndiProperty = null;
	private InitialContext initalContext = null;
	private Topic topic = null ;
	private TopicConnection topicConnection = null;
	private TopicSession topicSession = null;
	
	private void setJndiProperty()
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
			setJndiProperty();
			initalContext = new InitialContext( jndiProperty );
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

	public Properties getJndiProperty() {
		return jndiProperty;
	}

	public void setJndiProperty(Properties jndiProperty) {
		this.jndiProperty = jndiProperty;
	}

	public InitialContext getInitalContext() {
		return initalContext;
	}

	public void setInitalContext(InitialContext initalContext) {
		this.initalContext = initalContext;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public TopicConnection getTopicConnection() {
		return topicConnection;
	}

	public void setTopicConnection(TopicConnection topicConnection) {
		this.topicConnection = topicConnection;
	}

	public TopicSession getSession() {
		return topicSession;
	}

	public void setSession(TopicSession session) {
		this.topicSession = session;
	}
}
