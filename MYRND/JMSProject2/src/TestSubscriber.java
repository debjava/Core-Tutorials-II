import java.io.FileOutputStream;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class TestSubscriber implements MessageListener 
{


public void subscriber()
{
	try
	{
		PublishIO jp = new PublishIO();
		jp.setJndiProperty();
		TopicSession session = jp.jndiInit() ;
		TopicSubscriber subscriber=session.createSubscriber(jp.topic);
		subscriber.setMessageListener(this);
		jp.topicConnection.start();
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	
}
	
	public void onMessage(Message msg) 
	{
		System.out.println("Am i receiving any message?");
		 try
		 {
			 if(msg instanceof TextMessage )
			 {
				 TextMessage textmessage=(TextMessage)msg;
				 String ss = textmessage.getText();
				 System.out.println(ss);
			 }
			 if(msg instanceof BytesMessage)
			 {
				 System.out.println("******************* Receiving the byte message ****************");
				 BytesMessage byteMessage = (BytesMessage)msg;
				 FileOutputStream fout = new FileOutputStream("F:/Rama.jar");
				 int fileSize = byteMessage.getIntProperty("size");
				 final String fileName = byteMessage.getStringProperty("filename");
				 System.out.println("This file received ::: "+fileName);
				 byte[] buffer = new byte[fileSize];
				 byteMessage.readBytes(buffer);
				 for(int i=0;i<buffer.length;i++)
				 {
					 fout.write(buffer[i]);
				 }
			 }
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	public static void main(String[] args) 
	{
		TestSubscriber test11 = new TestSubscriber();
		test11.subscriber();
	}

}
