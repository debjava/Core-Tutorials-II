import java.io.FileInputStream;
import java.nio.channels.FileChannel;

import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

public class TestPublisher
{


	public static void main(String[] args) 
	{
		PublishIO jp = new PublishIO();
		jp.setJndiProperty();
		TopicSession session = jp.jndiInit() ;
		TopicPublisher publisher = null;
		
		 try
		 {
			publisher=session.createPublisher(jp.topic);
			jp.topicConnection.start();
			
			
			TextMessage msg=session.createTextMessage();
			StringBuffer body=new StringBuffer();
			body.append("<?xml version=\"1.0\"?>\n");
			body.append("<message>\n");
			body.append(" <sender>"+"HAI"+"</sender>\n");
			body.append(" <content>"+"WELCOME"+"</content>\n");
			body.append("</message>\n");
			msg.setText(body.toString());
			
			
			
//			final String fileName = "D:/JavaSoft/apache-ant-1.6.5/lib/ant.jar";
//			FileInputStream fin = new FileInputStream(fileName);
//			FileChannel fChan = fin.getChannel();
//			int size = (int)fChan.size();
//			byte[] buff = new byte[size];
//			fin.read(buff);
//			BytesMessage msg = session.createBytesMessage();
//			msg.setIntProperty("size",size);
//			final String sentFileName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
//			msg.setStringProperty("filename",sentFileName);
//			msg.writeBytes(buff);
			publisher.publish(msg);
		 }
		 
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }

	}
}
