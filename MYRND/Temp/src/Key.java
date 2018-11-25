import java.awt.Dimension; 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 
import java.awt.event.WindowEvent; 
import java.awt.event.WindowListener;
import java.io.BufferedReader; import java.io.BufferedWriter;
import java.io.File; 
import java.io.FileReader; 
import java.io.FileWriter;
import java.io.IOException; 
import javax.swing.JFrame; import sun.awt.WindowClosingListener;
public class Key extends JFrame implements KeyListener, WindowListener
{
	private File f; 
	private String text = "";
	Key()
	{ 
		f = new File("keys.txt");
		addKeyListener(this);
		addWindowListener(this); 
	} 
	public static void main(String[] args) 
	{ 
		Key f = new Key();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
	public void keyPressed(KeyEvent e)
	{ 
		text += e.getKeyChar(); 
	}
	public void keyReleased(KeyEvent e) 
	{
	} 
	public void keyTyped(KeyEvent e) 
	{
	}
	public void windowActivated(WindowEvent e) 
	{}
	public void windowClosed(WindowEvent e) 
	{} 
	public void windowClosing(WindowEvent e) 
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(text);
			bw.close();
		} 
		catch (IOException e1)
		{
			e1.printStackTrace(); 
		}
	} 
	public void windowDeactivated(WindowEvent e) 
	{} 
	public void windowDeiconified(WindowEvent e)
	{}
	public void windowIconified(WindowEvent e) 
	{}
	public void windowOpened(WindowEvent e) {}
} 