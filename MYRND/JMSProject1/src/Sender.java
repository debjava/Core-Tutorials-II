/*
Use source code downloads, example commands,
and any other techniques at your own risk.
No warranty is provided.
*/

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Sender {

	String url_;
	String name_;
	TopicConnection conn = null;
	TopicSession session = null;
	Topic topic = null;

	public Sender(String url, String name) throws JMSException, NamingException {

		url_ = url;
		name_ = name;

    		this.initializeSender();

    	}

	private void initializeSender() throws JMSException, NamingException {


		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", url_);

		Context context = new InitialContext(props);

		TopicConnectionFactory tcf = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		conn = tcf.createTopicConnection();
		topic = (Topic) context.lookup(name_);

		session = conn.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		conn.start();


	}

	 public void send(String text) throws JMSException, NamingException {

	     // Send a text msg
	     TopicPublisher send = session.createPublisher(topic);
	     TextMessage tm = session.createTextMessage(text);
	     send.publish(tm);
	     send.close();
	 }


	public void disconnect() throws JMSException {
		if(conn != null) {
			conn.stop();
		}

		if(session != null) {
			session.close();
		}

		if(conn != null) {
			conn.close();
		}
	}

	public String getTopicName() {
		return name_;
	}

	public String getTopicURL() {
		return url_;
	}
	
//	public static void setupPTP()
//	throws JMSException, NamingException
//	{
//		Queue queA = null;
//		InitialContext iniCtx = new InitialContext();
//		Object tmp = iniCtx.lookup("ConnectionFactory");
//		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
//		QueueConnection conn = qcf.createQueueConnection();//createQueueConnection();
//		queA = (Queue) iniCtx.lookup("queue/A");
////		queB = (Queue) iniCtx.lookup("queue/B");
//		QueueSession  session = conn.createQueueSession(false,
//				QueueSession.AUTO_ACKNOWLEDGE);
//		conn.start();
//		System.out.println("DONE:::::::::::");
//	}


	public static void main(String args[]) throws Exception 
	{
//		setupPTP();

		System.out.println("Starting JMS Example Sender");

//        	Sender sender = new Sender("server:1099", "topic/testTopic");
        	Sender sender = new Sender("127.0.0.1:8093", "topic/testTopic");
//        	Sender sender = new Sender("127.0.0.1:8093", "topic/testTopic");
        	System.out.println("Sending list of Adam Sandler Movies");

        	sender.send("Billy Madison 1995");
        	sender.send("Happy Gilmore 1996");
        	sender.send("The Waterboy 1998");
        	sender.send("Bid Daddy 1999");
        	sender.send("Mr.Deeds 2002");
        	sender.send("Eight Crazy Nights 2002");
       	 	sender.send("Anger Management 2003");

        	sender.disconnect();

       	 	System.out.println("JMS Example Sender Complete - list sent");

     }

}

