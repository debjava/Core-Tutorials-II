import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@MessageDriven(activationConfig =
{
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination", propertyValue="queue/testQueue")
}
	)
public class MDBBeanInEJB3 implements MessageListener
{
	@Override
	public void onMessage(Message msg) 
	{
		System.out.println("--- Inside onMessage() method ---");
		try
		{
			if( msg instanceof TextMessage )
			{
				TextMessage textMessage = ( TextMessage )msg;
				System.out.println(textMessage.getText());
			}
		}
		catch( JMSException jme )
		{
			jme.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		System.out.println("--- Inside onMessage() method ---");
	}
}
