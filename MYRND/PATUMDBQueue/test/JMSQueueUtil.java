import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;


public class JMSQueueUtil 
{
	private Properties jndiProperty = null;
	private InitialContext initalContext = null;
	private Queue queue = null;
	private QueueConnection queueConnection = null;
	private QueueSession queueSession = null;
	
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

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public QueueConnection getQueueConnection() {
		return queueConnection;
	}

	public void setQueueConnection(QueueConnection queueConnection) {
		this.queueConnection = queueConnection;
	}

	public QueueSession getQueueSession() {
		return queueSession;
	}

	public void setQueueSession(QueueSession queueSession) {
		this.queueSession = queueSession;
	}

	private void setJndiProperty()
	{
		jndiProperty = new Properties();
		jndiProperty.setProperty ("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperty.setProperty ("java.naming.provider.url",  "127.0.0.1:1099");
		jndiProperty.setProperty ("java.naming.factory.url.pkgs",  "org.jboss.naming:org.jnp.interfaces");
	}
	
	public QueueSession jndiInit()
	{
		try
		{
			setJndiProperty();
			initalContext = new InitialContext( jndiProperty );
			QueueConnectionFactory qcf = (QueueConnectionFactory)initalContext.lookup("UIL2ConnectionFactory");
			queue = (Queue)initalContext.lookup("queue/testQueue");
			queueConnection = qcf.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return queueSession ; 
	}
	
}
