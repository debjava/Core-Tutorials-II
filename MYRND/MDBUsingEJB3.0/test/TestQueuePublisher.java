import javax.jms.JMSException;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;


public class TestQueuePublisher 
{
	public static void main(String[] args) 
	{
		QueueSession queueSession = JMSUtil.getQueueSession();
		try 
		{
			QueueSender queueSender = queueSession.createSender(JMSUtil.getQueue());
			TextMessage textMessage = queueSession.createTextMessage();
			textMessage.setText("Hi, How are you ?");
			queueSender.send(textMessage);
		}
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
	}
}
