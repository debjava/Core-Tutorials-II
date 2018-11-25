package test;

import org.jboss.system.ServiceMBeanSupport;

import ser.JMSMBDServiceMBean;
import ser.TestReceiver;

public class MyBeanService extends ServiceMBeanSupport implements JMSMBDServiceMBean 
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
