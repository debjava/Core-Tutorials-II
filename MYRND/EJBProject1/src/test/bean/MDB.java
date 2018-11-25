package test.bean;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

public class MDB implements MessageDrivenBean, MessageListener
{
    private MessageDrivenContext ctx = null;
    public MDB() {
	
    }
    public void setMessageDrivenContext(MessageDrivenContext ctx)
	throws EJBException 
	{
	this.ctx = ctx;
    }
    
    public void ejbCreate() {}

    public void ejbRemove() {ctx=null;}

    public void onMessage(Message message)
    {
	System.err.println("Bean got message" + message.toString() );
    }
} 
