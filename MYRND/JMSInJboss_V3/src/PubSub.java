
import javax.jms.*;
import javax.naming.*;
import java.io.*;
import java.util.Properties;

public class PubSub 
{
	public static Properties getJndiProperties()
	{
		Properties jndiProp = new Properties();
		try
		{
			InputStream in = new FileInputStream("config/jbossjndi.properties");
			jndiProp.load(in);
		}
		catch( FileNotFoundException fnfe )
		{
			fnfe.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return jndiProp;
	}
	
    public static void main(String[] args)
    {
        String                  topicName = null;
        Context                 jndiContext = null;
        TopicConnectionFactory  topicConnectionFactory = null;
        TopicConnection         topicConnection = null;
        TopicSession            topicSession = null;
        Topic                   topic = null;
        Topic                   replyTopic = null;
        TopicPublisher          topicPublisher = null;
        TopicSubscriber         topicSubscriber = null;
        TextMessage             message = null;
        InputStreamReader       inputStreamReader = null;
        char                    answer = '\0';
        final int               NUM_MSGS;
        
        topicName = "topic/testTopic";//new String(args[0]);
        System.out.println("Topic name is " + topicName);
        if (args.length == 2){
            NUM_MSGS = (new Integer(args[1])).intValue();
        } else {
            NUM_MSGS = 1;
        }
        
        /* 
         * Create a JNDI API InitialContext object if none exists
         * yet.
         */
        try {
            jndiContext = new InitialContext(getJndiProperties());
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API " +
                "context: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
        
        /* 
         * Look up connection factory and topic.  If either does
         * not exist, exit.
         */
        try {
            topicConnectionFactory = (TopicConnectionFactory)jndiContext.lookup("TopicConnectionFactory");
//                jndiContext.lookup("jms/TopicConnectionFactory");
            topic = (Topic) jndiContext.lookup(topicName);
        } catch (NamingException e) {
            System.out.println("JNDI API lookup failed: " +
                e.toString());
            e.printStackTrace();
            System.exit(1);
        }

        /*
         * Create connection.
         * Create session from connection; false means session is
         * not transacted.
         * Create publisher, temporary topic, and text message,
         *  setting JMSReplyTo field to temporary topic and 
         *  setting an id property.
         * Send messages, varying text slightly.
         * Create subscriber and set message listener to receive
         *  replies.
         * When all messages have been received, enter Q to quit.
         * Finally, close connection.
         */
        try {
            topicConnection = 
                topicConnectionFactory.createTopicConnection();
            topicSession = 
                topicConnection.createTopicSession(false, 
                    Session.AUTO_ACKNOWLEDGE);
            topicPublisher = topicSession.createPublisher(topic);
            replyTopic = topicSession.createTemporaryTopic();
            message = topicSession.createTextMessage();
            message.setJMSReplyTo(replyTopic);
            int id = 1;
            for (int i = 0; i < NUM_MSGS; i++) {
                message.setText("This is message " + id);
                message.setIntProperty("id", id);
                System.out.println("Publishing message: " + 
                    message.getText());
                topicPublisher.publish(message);
                id++;
            }

            topicSubscriber = 
                topicSession.createSubscriber(replyTopic);
          topicSubscriber.setMessageListener(new TextListener());
            topicConnection.start();
            System.out.println("To end program, enter Q or q, " +
                "then <return>");
            inputStreamReader = new InputStreamReader(System.in);
            while (!((answer == 'q') || (answer == 'Q'))) {
                try {
                    answer = (char) inputStreamReader.read();
                } catch (IOException e) {
                    System.out.println("I/O exception: " 
                        + e.toString());
                }
            }
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + 
                e.toString());
        } finally {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException e) {}
            }
        }
    }

    /**
     * The TextListener class implements the MessageListener 
     * interface by defining an onMessage method that displays 
     * the contents and id property of a TextMessage.
     *
     * This class acts as the listener for the PubSub
     * class.
     */
    static class TextListener implements MessageListener {

        /**
         * Casts the message to a TextMessage and displays its 
         * text.
         *
         * @param message     the incoming message
         */
        public void onMessage(Message message) {
            TextMessage  msg = null;
            String       txt = null;
            int          id = 0;
            
            try {
                if (message instanceof TextMessage) {
                    msg = (TextMessage) message;
                    id = msg.getIntProperty("id");
                    txt = msg.getText();
                    System.out.println("Reading message: id=" + 
                        id + ", text=" + txt);
                } else {
                    System.out.println("Message of wrong type: "
                        + message.getClass().getName());
                }
            } catch (JMSException e) {
                System.out.println("JMSException in onMessage():"
                    + e.toString());
            } catch (Throwable t) {
                System.out.println("Exception in onMessage():" +
                    t.getMessage());
            }
        }
    }
}
