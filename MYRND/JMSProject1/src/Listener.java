/*
Use source code downloads, example commands,
and any other techniques at your own risk.
No warranty is provided.
*/

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Listener implements MessageListener {

	String url_;
	String name_;
	TopicConnection conn = null;
    	TopicSession session = null;
    	Topic topic = null;

    	public Listener(String url, String name) {
    		super();

    		url_ = url;
    		name_ = name;

    		try {
    			this.initializeListener();
    		} catch (Exception e) {
    			System.out.println("Error creating listener: " + e);
			e.printStackTrace();
		}

	}

	public void onMessage(Message msg) {

		TextMessage tm = (TextMessage) msg;

		try {
			System.out.println("Incoming message: " + tm.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeListener() throws JMSException, NamingException {

		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", url_);

		Context context = new InitialContext(props);
		System.out.println("performing lookup...");

		Object tmp = context.lookup("ConnectionFactory");
		System.out.println("lookup completed, making topic");

		TopicConnectionFactory tcf = (TopicConnectionFactory) tmp;
		conn = tcf.createTopicConnection();
		topic = (Topic) context.lookup(name_);

		session = conn.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		conn.start();

		TopicSubscriber recv = session.createSubscriber(topic);
		recv.setMessageListener(this);
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

	public static void main(String args[]) {

		System.out.println("Starting JMS Example Listener");
		System.out.println("Program will be active for 1 minute.");

		//change these values to your situtation:
		Listener listener = new Listener("127.0.0.1:8093", "topic/testTopic");


		//leave it open for 2 minutes:
		try {
			Thread.sleep(120000);
		} catch(Exception e) {
			System.out.println("Error sleeping: " + e);
			e.printStackTrace();
		}

		try {
			listener.disconnect();
		} catch(Exception e) {
			System.out.println("Error terminating listener JMS objects: " + e);
			e.printStackTrace();
		}

		System.out.println("Done listening");
	}

}

