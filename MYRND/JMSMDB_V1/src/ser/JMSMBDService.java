package ser;

import org.jboss.system.ServiceMBeanSupport;

public class JMSMBDService extends ServiceMBeanSupport implements JMSMBDServiceMBean 
{
	public void startService()
	{
		TestReceiver receiver = new TestReceiver();
		receiver.receiver();
	}
	public void stopService()
	{
		
	}

}
