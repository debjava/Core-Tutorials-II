
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import com.ope.patu.beans.EmpBean;

public class MessagePublisher
{
	private JMSTopicUtil jmsTopicUtil = null;
	private TopicSession topicSession = null;
	private TopicPublisher topicPublisher = null;
	
	private void init()
	{
		try
		{
			jmsTopicUtil = new JMSTopicUtil();
			topicSession = jmsTopicUtil.jndiInit();
			topicPublisher = topicSession.createPublisher( jmsTopicUtil.getTopic() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	public void send()
	{
		publish();
	}
	
	public void publish( Object obj )
	{
		try
		{
			init();
			jmsTopicUtil.getTopicConnection().start();
			if( obj instanceof String )
			{
				System.out.println("It is a String message, so message will be sent as TextMessage");
				TextMessage msg = topicSession.createTextMessage();
				String msgString = ( String )obj;
				msg.setText(msgString);
				topicPublisher.publish(msg);
			}
			else if( obj instanceof java.util.Map )
			{
				System.out.println("It is a Map message, so message will be sent as MapMessage");
				MapMessage msg = topicSession.createMapMessage();
				Map dataMap = ( Map )obj;
				msg.setString("temp",(String)dataMap.get("temp"));
				topicPublisher.publish(msg);
			}
			else if( obj instanceof EmpBean )
			{
				System.out.println("It is an Object message, so message will be sent as ObjectMessage");
				EmpBean empBean = ( EmpBean )obj;
				ObjectMessage objMessage = topicSession.createObjectMessage();
				objMessage.setObject(empBean);
				topicPublisher.publish(objMessage);
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public void publish()
	{
		try
		{
			init();
			jmsTopicUtil.getTopicConnection().start();
			TextMessage msg = topicSession.createTextMessage();
			StringBuffer body = new StringBuffer();
			body.append("HI How are you ?");
			msg.setText(body.toString());
			topicPublisher.publish(msg);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
}
