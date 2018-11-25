import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;


public class TestJMS 
{
	public static void main(String[] args) throws Exception
	{
		InitialContext iniCtx = new InitialContext();
        Object tmp = iniCtx.lookup("UILConnectionFactory");
        QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
        QueueConnection  conn = qcf.createQueueConnection();
        Queue  que = (Queue) iniCtx.lookup("queue/testQueue");
        QueueSession  session = conn.createQueueSession(false,
                                          QueueSession.AUTO_ACKNOWLEDGE);
        conn.start();

	}
}
