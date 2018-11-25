import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import com.ope.patu.beans.EmpBean;


public class QueueMessagePublisher 
{

	private JMSQueueUtil jmsQueueUtil = null;
	private QueueSession queueSession = null;
	private QueueSender queueSender = null;
	
	
//	private TopicSession topicSession = null;
//	private TopicPublisher topicPublisher = null;
	
	private void init()
	{
		try
		{
			jmsQueueUtil = new JMSQueueUtil();
			queueSession = queueSession = jmsQueueUtil.jndiInit();
			queueSender = queueSession.createSender(jmsQueueUtil.getQueue());
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
			jmsQueueUtil.getQueueConnection().start();
			if( obj instanceof String )
			{
				System.out.println("It is a String message, so message will be sent as TextMessage");
				TextMessage msg = queueSession.createTextMessage();
				String msgString = ( String )obj;
				msg.setText(msgString);
				queueSender.send(msg);
			}
			else if( obj instanceof java.util.Map )
			{
				System.out.println("It is a Map message, so message will be sent as MapMessage");
				MapMessage msg = queueSession.createMapMessage();
				Map dataMap = ( Map )obj;
				msg.setString("temp",(String)dataMap.get("temp"));
				queueSender.send(msg);
			}
			else if( obj instanceof EmpBean )
			{
				System.out.println("It is an Object message, so message will be sent as ObjectMessage");
				EmpBean empBean = ( EmpBean )obj;
				ObjectMessage objMessage = queueSession.createObjectMessage();
				objMessage.setObject(empBean);
				queueSender.send(objMessage);
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
			jmsQueueUtil.getQueueConnection().start();
			TextMessage msg = queueSession.createTextMessage();
			StringBuffer body = new StringBuffer();
			body.append("HI How are you ?");
			msg.setText(body.toString());
			queueSender.send(msg);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	

}
