import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailWithGmail 
{
	public static void simpleMail(final String from, final String password, String to,
			String subject, String body) throws Exception {

		String host = "smtp.gmail.com";
		Properties props = System.getProperties();
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", host);
//		props.put("mail.smtp.user", from);
//		props.put("mail.smtp.password", password);
//		props.put("mail.smtp.port", "465");
//		props.put("mail.smtp.auth", "true");
		
		
		
		
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory"; 

		props.setProperty("mail.smtp.host", "smtp.gmail.com"); 
		   props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY); 
		   props.setProperty("mail.smtp.socketFactory.fallback", "false"); 
		   props.setProperty("mail.smtp.port", "465"); 
		   props.setProperty("mail.smtp.socketFactory.port", "465"); 
		   props.put("mail.smtp.auth", "true"); 
		   Session session = Session.getDefaultInstance(props, 
				     new Authenticator() { 
				      protected PasswordAuthentication getPasswordAuthentication() { 
				       return new PasswordAuthentication(from, 
				         password); 
				      } 
				     });   

		
		
		
		
		
		
		
		

//		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));

		InternetAddress toAddress = new InternetAddress(to);

		message.addRecipient(Message.RecipientType.TO, toAddress);

		message.setSubject(subject);
		message.setText(body);
		Transport transport = session.getTransport("smtp");
//		transport.connect("smtp.mail.yahoo.co.in", "pimumishra@yahoo.com", "international"); 
		transport.connect(host, from, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		simpleMail("piku.mishra@gmail.com", "international", "piku.mishra@gmail.com", "HHHH", "ABCD");
	}

}
