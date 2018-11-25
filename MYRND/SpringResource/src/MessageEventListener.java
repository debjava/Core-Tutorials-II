import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class MessageEventListener implements ApplicationListener {

   
    public void onApplicationEvent(ApplicationEvent event) {
       if(event instanceof MessageEvent) {
           MessageEvent msgEvt = (MessageEvent)event;
           System.out.println("Received: " + msgEvt.getMessage());
       }
    }

}
