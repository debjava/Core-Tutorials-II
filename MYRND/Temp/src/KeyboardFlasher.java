import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener; 
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
public class KeyboardFlasher implements AWTEventListener
{
	//click on the frame to start 
	public static void main(String[] args)
	{
		Toolkit tk = Toolkit.getDefaultToolkit(); 
		KeyboardFlasher flasher = new KeyboardFlasher();
		tk.addAWTEventListener(flasher, AWTEvent.MOUSE_EVENT_MASK); 
		JFrame frame = new JFrame(); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(50,60));
		frame.pack();
		frame.setVisible(true);
	}
	public void eventDispatched(AWTEvent evt)
	{ 
		if(evt instanceof MouseEvent) 
		{ 
			MouseEvent kevt = (MouseEvent)evt;
			if(kevt.getID() == MouseEvent.MOUSE_CLICKED)
			{ 
				flipLock(); 
			} 
		} 
	} 
	public void flipLock()
	{ 
		int i = 0; Toolkit tk = Toolkit.getDefaultToolkit();
		while(i < 3)
		{
			//how many time to run 
			boolean state = tk.getLockingKeyState(KeyEvent.VK_NUM_LOCK);
			tk.setLockingKeyState(KeyEvent.VK_NUM_LOCK,!state);
			try { Thread.sleep(100); 
			} 
			catch (InterruptedException e) 
			{}
			boolean state1 = tk.getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
			tk.setLockingKeyState(KeyEvent.VK_CAPS_LOCK,!state1);
			try 
			{ 
				Thread.sleep(100); 
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			boolean state2 = tk.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
			tk.setLockingKeyState(KeyEvent.VK_SCROLL_LOCK,!state2); 
			try 
			{ 
				Thread.sleep(100);
			}
			catch 
			(InterruptedException e)
			{
				e.printStackTrace();
			}
			i++; 
		}
	}


}
